package receiver;

import aeron.AeronSubscriber;
import dao.OffHeapStorage;
import sbe.builder.LOBBuilder;
import sbe.msg.*;
import sbe.msg.marketData.OrderExecutedWithPriceSizeEncoder;
import sbe.reader.AdminReader;
import sbe.reader.LOBReader;
import sbe.reader.OrderViewReader;
import sbe.reader.marketData.OrderExecutedWithPriceSizeReader;
import uk.co.real_logic.aeron.logbuffer.FragmentHandler;
import uk.co.real_logic.aeron.logbuffer.Header;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;
import vo.OrderVO;

import java.nio.ByteBuffer;
import java.util.Properties;

/**
 * Created by dharmeshsing on 29/12/16.
 */
public class MDGClientProcessor implements FragmentHandler,Runnable {

    private MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    private sbe.msg.marketData.MessageHeaderDecoder mktDataMessageHeader = new sbe.msg.marketData.MessageHeaderDecoder();
    private UnsafeBuffer temp = new UnsafeBuffer(ByteBuffer.allocateDirect(106));
    private OrderExecutedWithPriceSizeReader orderExecutedReader = new OrderExecutedWithPriceSizeReader();

    private AdminReader adminReader = new AdminReader();
    private OrderViewReader orderViewReader = new OrderViewReader();
    private LOBReader lobReader = new LOBReader();

    private OrderVO orderFlyweight = new OrderVO();
    private LOBBuilder.Order lobFlyweight = new LOBBuilder.Order();

    private OffHeapStorage offHeapStorage;
    private Properties properties;
    private AeronSubscriber marketDataSubscriber;
    private String mediaDriverConextDir;
    private OrderVOHandler orderVOHandler;
    private OrderVODisruptor orderVODisruptor;
    private TradeVOHandler tradeVOHandler;
    private TradeVODisruptor tradeVODisruptor;

    public MDGClientProcessor(OffHeapStorage offHeapStorage,Properties properties,String mediaDriverConextDir){
        this.offHeapStorage = offHeapStorage;
        this.properties = properties;
        this.mediaDriverConextDir = mediaDriverConextDir;
    }

    public void init(){
        initMarketDataSubscriber();
        initDisruptor();
    }

    private void initMarketDataSubscriber(){
        marketDataSubscriber = new AeronSubscriber(mediaDriverConextDir,this);

        String url = properties.get("MARKET_DATA_SUB_URL").toString();
        int streamId = Integer.parseInt(properties.get("MARKET_DATA_SUB_STREAM_ID").toString());

        marketDataSubscriber.addSubscriber(url,streamId);
    }

    private void initDisruptor(){
        orderVOHandler = new OrderVOHandler(offHeapStorage);
        orderVODisruptor = new OrderVODisruptor(orderVOHandler,1_000_000);

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
                case OrderViewEncoder.TEMPLATE_ID: readOrderView();break;
                case OrderExecutedWithPriceSizeEncoder.TEMPLATE_ID: readTrade();break;
                case AdminDecoder.TEMPLATE_ID: readAdminMessage(); break;
                case LOBEncoder.TEMPLATE_ID: readLob();break;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void readTrade() throws Exception {
        orderExecutedReader.readBuffer(temp);

        tradeVODisruptor.addTradeVO(orderExecutedReader.getInstrumentId(),orderExecutedReader.getTradeId(),
                (int)orderExecutedReader.getPrice(),orderExecutedReader.getExecutedQuantity());
    }

    private void readOrderView() throws Exception {
        orderViewReader.read(temp);

        orderVODisruptor.addOrderVO(orderViewReader.getSecurityId(),orderViewReader.getOrderId(),
                orderViewReader.getSide(),orderViewReader.getSubmittedTime(),
                orderViewReader.getOrderQuantity(),orderViewReader.getPrice());
    }

    private void readAdminMessage() throws Exception {
        adminReader.read(temp);
        processAdminMessage(adminReader.getCompId(),adminReader.getSecurityId(),adminReader.getAdminTypeEnum());
    }

    private void readLob() throws Exception {
        lobReader.read(temp);
        readLOB(lobReader);
    }

    public void processAdminMessage(int clientId,long securityId,AdminTypeEnum adminTypeEnum) {
        if(adminTypeEnum.equals(AdminTypeEnum.StartMessage)) {
            offHeapStorage.updateHawkesSimulationStatus(true);
            offHeapStorage.updateHawkesSimulation(clientId, true);
        }else if(adminTypeEnum.equals(AdminTypeEnum.EndMessage)) {
            offHeapStorage.updateHawkesSimulation(clientId, false);
        }else if(adminTypeEnum == AdminTypeEnum.WarmUpComplete){
            clear();
            offHeapStorage.updateWarmupSimulation(clientId, false);
        }else if(adminTypeEnum.equals(AdminTypeEnum.StartLOB)){
            offHeapStorage.setLOBStatus(false);
            offHeapStorage.clearBidOrders((int)securityId);
            offHeapStorage.clearOfferOrders((int) securityId);
        }else if(adminTypeEnum.equals(AdminTypeEnum.EndLOB)){
            offHeapStorage.setLOBStatus(true);
        }else if(adminTypeEnum.equals(AdminTypeEnum.SimulationComplete)){
            offHeapStorage.updateHawkesSimulationStatus(false);
        }

        offHeapStorage.commit();
    }

    public void readLOB(LOBReader lobReader) {
        while (lobReader.hasNext()) {
            lobReader.next(lobFlyweight);
            short side = lobFlyweight.getSide().value();

            orderFlyweight.setOrderId(lobFlyweight.getOrderId());
            orderFlyweight.setPrice(lobFlyweight.getPrice());
            orderFlyweight.setVolume(lobFlyweight.getOrderQuantity());

            offHeapStorage.addPrice(lobReader.getSecurityId(),lobFlyweight.getPrice());

            if(side == 1) {
                offHeapStorage.addBidOrder(lobReader.getSecurityId(), orderFlyweight);
            }else{
                offHeapStorage.addOfferOrder(lobReader.getSecurityId(), orderFlyweight);
            }
        }
        offHeapStorage.commit();
    }

    private void clear() {
        offHeapStorage.clearTrades();
        offHeapStorage.clearOrders();
        offHeapStorage.clearPrices();
    }

    public void stop(){
        marketDataSubscriber.stop();
    }

    private void start(){
        marketDataSubscriber.start();
    }


    @Override
    public void run() {
        start();
    }
}
