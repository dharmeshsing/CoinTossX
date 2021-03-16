package gateway.client;

import aeron.AeronPublisher;
import aeron.AeronSubscriber;
import aeron.LowLatencyMediaDriver;
import com.carrotsearch.hppc.ObjectArrayList;
import gateway.client.disruptor.BidAskDisruptor;
import gateway.client.disruptor.BidAskHandler;
import sbe.msg.MessageHeaderDecoder;
import sbe.reader.AdminReader;
import sbe.reader.VWAPReader;
import sbe.reader.marketData.BestBidAskReader;
import sbe.reader.marketData.SymbolStatusReader;
import uk.co.real_logic.aeron.driver.MediaDriver;
import uk.co.real_logic.aeron.logbuffer.FragmentHandler;
import uk.co.real_logic.aeron.logbuffer.Header;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class GatewayClientImpl implements GatewayClient,FragmentHandler {

    private MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    private sbe.msg.marketData.MessageHeaderDecoder mktDataMessageHeader = new sbe.msg.marketData.MessageHeaderDecoder();
    private MediaDriver driver = LowLatencyMediaDriver.startMediaDriver();
    private AeronPublisher publisher;
    private AeronSubscriber subscriber;
    private ObjectArrayList<GatewayListener> listeners = new ObjectArrayList<>();
    private int listenerSize;
    private UnsafeBuffer temp = new UnsafeBuffer(ByteBuffer.allocateDirect(106));

    private BestBidAskReader bidAskReader = new BestBidAskReader();
    private AdminReader adminReader = new AdminReader();
    private SymbolStatusReader symbolStatusReader = new SymbolStatusReader();
    private VWAPReader vwapReader = new VWAPReader();

    private BidAskDisruptor bidAskDisruptor;
    private BidAskHandler bidAskHandler;

    public void addListener(GatewayListener subscriber) {
        listeners.add(subscriber);
        listenerSize++;
    }

    private void initDisruptor(){
        bidAskHandler = new BidAskHandler(listeners,listenerSize);
        bidAskDisruptor = new BidAskDisruptor(bidAskHandler,1_000_000);
    }

    @Override
    public void send(DirectBuffer buffer) {
        publisher.send(buffer);
    }

    @Override
    public void connectInput(String inputURL,int inputStreamId) {
        publisher = new AeronPublisher(driver.aeronDirectoryName());
        publisher.addPublication(inputURL, inputStreamId);
    }

    @Override
    public void connectOutput(String outputURL,int outputStreamId) {
        subscriber = new AeronSubscriber(driver.aeronDirectoryName(),this);
        subscriber.addSubscriber(outputURL, outputStreamId);
        initDisruptor();
        subscriber.start();
    }

    public void disconnectInput(){
        publisher.stop();
    }

    public void disconnectOutput(){
        subscriber.stop();
    }

    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
        temp.wrap(buffer,offset,length);

        try {
            messageHeaderDecoder.wrap(temp, 0);
            int templateId = messageHeaderDecoder.templateId();
            if (templateId == 0) {
                mktDataMessageHeader.wrap(temp,0);
                templateId = mktDataMessageHeader.templateId();
            }

            switch (templateId) {
                case 26: readBidAsk();break;
                case 27: readSymbolStatus();break;
                case 95: readVWAP();break;
                case 91: readAdminMessage(); break;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void readBidAsk() throws Exception {
        bidAskReader.read(temp);

        bidAskDisruptor.addBidAsk(bidAskReader.getInstrumentId(),bidAskReader.getBid(),bidAskReader.getBidQuantity(),
                bidAskReader.getOffer(),bidAskReader.getOfferQuantity());
    }

    private void readAdminMessage() throws Exception {
        adminReader.read(temp);
        for (int i = 0; i < listenerSize; i++) {
            listeners.get(i).processAdminMessage(adminReader.getCompId(),adminReader.getSecurityId(),adminReader.getAdminTypeEnum());
        }
    }

    private void readSymbolStatus() throws Exception {
        symbolStatusReader.read(temp);
        for (int i = 0; i < listenerSize; i++) {
            listeners.get(i).symbolStatus(symbolStatusReader.getSecurityId(), symbolStatusReader.getSessionChangedReason(), symbolStatusReader.getTradingSession());
        }
    }

    private void readVWAP() throws Exception {
        vwapReader.read(temp);
        for (int i = 0; i < listenerSize; i++) {
            listeners.get(i).readVWAP(vwapReader);
        }
    }
}
