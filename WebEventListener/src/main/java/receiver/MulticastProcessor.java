package receiver;

import aeron.AeronSubscriber;
import dao.OffHeapStorage;
import sbe.msg.MessageHeaderDecoder;
import sbe.msg.marketData.SymbolStatusDecoder;
import sbe.reader.marketData.OrderExecutedWithPriceSizeReader;
import sbe.reader.marketData.SymbolStatusReader;
import uk.co.real_logic.aeron.logbuffer.FragmentHandler;
import uk.co.real_logic.aeron.logbuffer.Header;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;
import java.util.Properties;

public class MulticastProcessor implements FragmentHandler,Runnable {

    private MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    private sbe.msg.marketData.MessageHeaderDecoder mktDataMessageHeader = new sbe.msg.marketData.MessageHeaderDecoder();
    private UnsafeBuffer temp = new UnsafeBuffer(ByteBuffer.allocateDirect(106));
    private SymbolStatusReader symbolStatusReader = new SymbolStatusReader();
    private OrderExecutedWithPriceSizeReader orderExecutedReader = new OrderExecutedWithPriceSizeReader();

    private OffHeapStorage offHeapStorage;
    private Properties properties;
    private AeronSubscriber multiCastMarketDataSubscriber;
    private String mediaDriverConextDir;
    private TradeVOHandler tradeVOHandler;
    private TradeVODisruptor tradeVODisruptor;

    public MulticastProcessor(OffHeapStorage offHeapStorage,Properties properties,String mediaDriverConextDir){
        this.offHeapStorage = offHeapStorage;
        this.properties = properties;
        this.mediaDriverConextDir = mediaDriverConextDir;
    }

    public void init(){
        initMarketDataSubscriber();
        initDisruptor();
    }

    private void initMarketDataSubscriber(){
        multiCastMarketDataSubscriber = new AeronSubscriber(mediaDriverConextDir,this);

        String multiCastURL = properties.get("MARKET_DATA_URL").toString();
        int multicastStreamId = Integer.parseInt(properties.get("MARKET_DATA_STREAM_ID").toString());

        multiCastMarketDataSubscriber.addSubscriber(multiCastURL,multicastStreamId);
    }

    private void initDisruptor(){
        tradeVOHandler = new TradeVOHandler(offHeapStorage);
        tradeVODisruptor = new TradeVODisruptor(tradeVOHandler,1_000_000);
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
//                case OrderExecutedWithPriceSizeEncoder.TEMPLATE_ID: readTrade();break;
                case SymbolStatusDecoder.TEMPLATE_ID: readSymbolStatus();break;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void readTrade() throws Exception {
        orderExecutedReader.readBuffer(temp);

        tradeVODisruptor.addTradeVO(orderExecutedReader.getInstrumentId(),orderExecutedReader.getTradeId(),orderExecutedReader.getClientOrderId(),
                (int)orderExecutedReader.getPrice(),orderExecutedReader.getExecutedQuantity(),orderExecutedReader.getExecutedTime());
    }

    private void readSymbolStatus() throws Exception {
        symbolStatusReader.read(temp);
        offHeapStorage.setSymbolStatus(symbolStatusReader.getSecurityId(), symbolStatusReader.getTradingSession());
    }

    public void stop(){
        multiCastMarketDataSubscriber.stop();
    }

    private void start(){
        multiCastMarketDataSubscriber.start();
    }

    @Override
    public void run() {
        start();
    }
}
