package gateway.marketdata;

import aeron.AeronPublisher;
import aeron.AeronSubscriber;
import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import sbe.msg.*;
import sbe.msg.marketData.BestBidOfferDecoder;
import sbe.msg.marketData.OrderExecutedWithPriceSizeEncoder;
import sbe.msg.marketData.SymbolStatusDecoder;
import sbe.reader.AdminReader;
import uk.co.real_logic.aeron.logbuffer.FragmentHandler;
import uk.co.real_logic.aeron.logbuffer.Header;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class NativeMarketDataGateway implements MarketDataGateway,FragmentHandler{

    protected Properties properties;
    private static final String PROPERTIES_FILE =  "MarketDataGateway.properties";
    private static AtomicBoolean running = new AtomicBoolean(false);
    private AeronSubscriber matchingEngineSubscriber;
    private AeronPublisher matchingEnginePublisher;
    private AeronPublisher multicastPublisher;
    private UnsafeBuffer encodedBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(300));
    private IntObjectMap<Client> clients;
    private MDClientProcessor mdClientProcessor;
    private Thread meThread;
    private IntObjectMap<AeronPublisher> clientPublisher;
    private AeronPublisher webPublisher;
    private MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    private AdminReader adminReader;
    private String mediaDriverConextDir;


    protected void loadProperties(String propertiesFile) throws IOException {
        try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFile)) {

            if (inputStream != null) {
                properties = new Properties();
                properties.load(inputStream);
            } else {
                throw new IOException("Unable to load properties file " + propertiesFile);
            }
        }
    }

    @Override
    public void start() {
        System.out.println("Market Data Gateway Started");
        running.set(true);
        matchingEngineSubscriber.start();
    }

    @Override
    public void initialize(){
        try {
            loadProperties(PROPERTIES_FILE);
            mediaDriverConextDir = properties.getProperty("MEDIA_DRIVER_DIR") + File.separator + "marketDataGateway";
            initClients();
            initMatchingEnginePublisher();
            initMatchingEngineSubscriber();
            initMulticastPublisher();
            initClientSubscriber();
            initClientPublisher();
            initWebPublisher();
            adminReader = new AdminReader();

        } catch (Exception e) {
            //TODO:Handle Exception
            e.printStackTrace();
        }
    }

    private void initWebPublisher(){
        webPublisher = new AeronPublisher(mediaDriverConextDir);

        String webURL = properties.getProperty("PUB_WEB_URL");
        int webStreamId = Integer.parseInt(properties.getProperty("PUB_WEB_STREAM_ID"));

        webPublisher.addPublication(webURL,webStreamId);
    }

    private void initClients() throws Exception {
        clients = Client.loadClientData(properties.get("DATA_PATH").toString());
    }

    private void initMatchingEngineSubscriber(){
        matchingEngineSubscriber = new AeronSubscriber(mediaDriverConextDir,this);

        String url = properties.getProperty("SUB_MARKET_DATA_URL");
        int streamId = Integer.parseInt(properties.getProperty("SUB_MARKET_DATA_STREAM_ID"));

        matchingEngineSubscriber.addSubscriber(url, streamId);
    }

    private void initMulticastPublisher(){
        multicastPublisher = new AeronPublisher(mediaDriverConextDir);

        String url = properties.getProperty("PUB_MARKET_DATA_URL");
        int streamId = Integer.parseInt(properties.getProperty("PUB_MARKET_DATA_STREAM_ID"));

        multicastPublisher.addPublication(url,streamId);
    }

    private void initClientSubscriber(){
        mdClientProcessor = new MDClientProcessor(mediaDriverConextDir,matchingEnginePublisher,clients);
        meThread = new Thread(mdClientProcessor);
        meThread.start();
    }

    private void initClientPublisher(){
        clientPublisher = new IntObjectHashMap<>();
        for(ObjectCursor<Client> client : clients.values()){
            AeronPublisher pub = new AeronPublisher(mediaDriverConextDir);
            pub.addPublication(client.value.getMdgOutputURL(), client.value.getMdgOutputStreamId());
            clientPublisher.put(client.value.getCompID(),pub);
        }
    }

    private void initMatchingEnginePublisher(){
        matchingEnginePublisher = new AeronPublisher(mediaDriverConextDir);

        String url = properties.getProperty("PUB_MATCHING_ENGINE_URL");
        int streamId = Integer.parseInt(properties.getProperty("PUB_MATCHING_ENGINE_STREAM_ID"));

        matchingEnginePublisher.addPublication(url, streamId);
    }


    @Override
    public void stop() {
        matchingEngineSubscriber.stop();
        multicastPublisher.stop();
        matchingEnginePublisher.stop();
        multicastPublisher.stop();
        stopClientPublisher();
        webPublisher.stop();
        running.set(false);
    }

    private void stopClientPublisher(){
        for(ObjectCursor<AeronPublisher> pub : clientPublisher.values()) {
            pub.value.stop();
        }
    }

    @Override
    public boolean status() {
        return running.get();
    }

    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
        encodedBuffer.wrap(buffer,offset,length);
        messageHeaderDecoder.wrap(encodedBuffer, 0);

        int templateId = messageHeaderDecoder.templateId();

        switch (templateId) {
            case OrderViewEncoder.TEMPLATE_ID:webPublisher.send(encodedBuffer); break;
            case OrderExecutedWithPriceSizeEncoder.TEMPLATE_ID:webPublisher.send(encodedBuffer); break;
            case VWAPEncoder.TEMPLATE_ID:publishToClient(encodedBuffer);break;
            case LOBEncoder.TEMPLATE_ID: publishToClient(encodedBuffer); break;
            case AdminDecoder.TEMPLATE_ID: processAdminMessage(encodedBuffer); break;
            case BestBidOfferDecoder.TEMPLATE_ID: multicastPublisher.sendAndNotRetry(encodedBuffer);break;
            case SymbolStatusDecoder.TEMPLATE_ID:  multicastPublisher.sendAndNotRetry(encodedBuffer);break;
//            default:multicastPublisher.sendAndNotRetry(encodedBuffer);
        }
    }

    private void publishToClient(DirectBuffer buffer){
        clientPublisher.get(messageHeaderDecoder.compID()).send(buffer);
    }

    private void processAdminMessage(DirectBuffer buffer){
        adminReader.read(buffer);

        switch(adminReader.getAdminTypeEnum()){
            case StartMessage:webPublisher.send(buffer);break;
            case EndMessage:webPublisher.send(buffer);publishToClient(buffer);break;
            case StartLOB:webPublisher.send(buffer);break;
            case EndLOB:webPublisher.send(buffer);break;
            case ShutDown:stop();break;
            default: publishToClient(buffer);
        }
    }

    public static void main(String[] args){
        NativeMarketDataGateway marketDataGateway = new NativeMarketDataGateway();
        marketDataGateway.initialize();
        marketDataGateway.start();
    }
}
