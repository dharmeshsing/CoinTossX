package gateway.incoming;

import aeron.AeronPublisher;
import aeron.AeronSubscriber;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import processor.ClientRequestProcessor;
import processor.MEProcessor;
import uk.co.real_logic.aeron.logbuffer.FragmentHandler;
import uk.co.real_logic.aeron.logbuffer.Header;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by dharmeshsing on 2014/12/15.
 */
public class NativeGateway implements TradingGateway,FragmentHandler{

    protected Properties properties;
    private static final String PROPERTIES_FILE =  "NativeGateway.properties";
    private static AtomicBoolean running = new AtomicBoolean(false);
    private IntObjectMap<Client> clients;
    private ClientRequestProcessor clientRequestProcessor;
    private MEProcessor meProcessor;
    private AeronSubscriber clientSubscriber;
    private AeronPublisher clientPublisher;
    private AeronPublisher matchingEnginePublisher;
    private UnsafeBuffer temp = new UnsafeBuffer(ByteBuffer.allocate(106));
    private Thread meThread;
    private String mediaDriverConextDir;

    private void loadProperties(String propertiesFile) throws IOException {
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
        System.out.println("Native Gateway Started");
        running.set(true);
        clientSubscriber.start();
    }

    @Override
    public void initialize(){
        try {

            loadProperties(PROPERTIES_FILE);
            mediaDriverConextDir = properties.getProperty("MEDIA_DRIVER_DIR") + File.separator + "tradingGateway";
            initClients();
            initGatewaySubscriber();
            initClientPublisher();
            initMatchingEnginePublisher();
            initMatchingEngineSubscriber();

            clientRequestProcessor = new ClientRequestProcessor(clientPublisher,matchingEnginePublisher,clients);

        } catch (Exception e) {
            //TODO:Handle Exception
            e.printStackTrace();
        }
    }

    private void initClients() throws Exception {
        clients = Client.loadClientData(properties.get("DATA_PATH").toString());
    }

    private void initGatewaySubscriber(){
        clientSubscriber = new AeronSubscriber(mediaDriverConextDir,this);

        for(ObjectCursor<Client> client : clients.values()){
            clientSubscriber.addSubscriber(client.value.getNgInputURL(), client.value.getNgInputStreamId());
        }
    }

    private void initClientPublisher(){
        clientPublisher = new AeronPublisher(mediaDriverConextDir);

        for(ObjectCursor<Client> client : clients.values()){
            clientPublisher.addPublication(client.value.getNgOutputURL(), client.value.getNgOutputStreamId());
        }
    }

    private void initMatchingEnginePublisher(){
        matchingEnginePublisher = new AeronPublisher(mediaDriverConextDir);

        String url = properties.getProperty("PUB_MATCHING_ENGINE_URL");
        int streamId = Integer.parseInt(properties.getProperty("PUB_MATCHING_ENGINE_STREAM_ID"));

        matchingEnginePublisher.addPublication(url, streamId);
    }

    private void initMatchingEngineSubscriber(){
        String url = properties.getProperty("SUB_MATCHING_ENGINE_URL");
        int streamId = Integer.parseInt(properties.getProperty("SUB_MATCHING_ENGINE_STREAM_ID"));

        meProcessor = new MEProcessor(mediaDriverConextDir,url,streamId,clientPublisher,clients);
        meThread = new Thread(meProcessor);
        meThread.start();
    }

    @Override
    public boolean stop() {
        running.set(false);
        clientSubscriber.stop();
        clientPublisher.stop();
        matchingEnginePublisher.stop();
        meProcessor.stop();
        meThread.stop();
        return true;
    }

    public static void setRunning(boolean value){
        running.set(value);
    }

    @Override
    public boolean status(){
        return running.get();
    }

    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
        temp.wrap(buffer, offset, length);
        try {
            clientRequestProcessor.process(temp);
            if(running.get() == false){
                stop();
            }
        } catch (UnsupportedEncodingException e) {
            //TODO: Handle error
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        NativeGateway nativeGateway = new NativeGateway();
        nativeGateway.initialize();
        nativeGateway.start();

    }
}
