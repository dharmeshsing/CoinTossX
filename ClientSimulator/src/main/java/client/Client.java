package client;

import gateway.client.GatewayClient;
import gateway.client.GatewayClientImpl;
import sbe.builder.AdminBuilder;
import sbe.builder.BuilderUtil;
import sbe.builder.LogonBuilder;
import sbe.builder.NewOrderBuilder;
import sbe.msg.*;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;

/**
 * Created by dharmeshsing on 13/03/16.
 */
public class Client {
    private ClientData clientData;

    private GatewayClient tradingGatewayPub;
    private TradingGatewaySubscriber tradingGatewaySubscriber;
    private MulticastMDGSubscriber marketDataGatewaySubscriber;
    private ClientMDGSubscriber clientMDGSubscriber;
    private GatewayClient marketDataGatewayPub;

    private NewOrderBuilder newOrderBuilder = new NewOrderBuilder();
    private AdminBuilder adminBuilder = new AdminBuilder();

    private long bid;
    private long bidQuantity;
    private long offer;
    private long offerQuantity;
    private long uniqueOrderId;
    private int securityId;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private NonBlockingSemaphore mktDataUpdateSemaphore = new NonBlockingSemaphore(1);
    private NonBlockingSemaphore snapShotSemaphore = new NonBlockingSemaphore(1);

    private boolean auction = false;
    private int count;

    public Client(ClientData clientData, int securityId){
        this.clientData = clientData;
        this.securityId = securityId;
    }

    public void initTradingGatewaySub(){
        String url = clientData.getNgOutputURL();
        int streamId = clientData.getNgOutputStreamId();
        tradingGatewaySubscriber = new TradingGatewaySubscriber(url, streamId);
        Thread thread = new Thread(tradingGatewaySubscriber);
        thread.start();
    }

    public void initMarketDataGatewayPub() {
        String url = clientData.getMdgInputURL();
        int streamId = clientData.getMdgInputStreamId();
        marketDataGatewayPub = new GatewayClientImpl();
        marketDataGatewayPub.connectInput(url, streamId);
    }

    public void initMulticastMarketDataGatewaySub(Properties properties){
        String url = properties.get("MDG_MULTICAST_URL").toString();
        int streamId = Integer.parseInt(properties.get("MDG_MULTICAST_STREAM_ID").toString());
        marketDataGatewaySubscriber = new MulticastMDGSubscriber(url, streamId, this, mktDataUpdateSemaphore);
        Thread thread = new Thread(marketDataGatewaySubscriber);
        thread.start();
    }

    public void initClientMarketDataGatewaySub(){
        String url = clientData.getMdgOutputURL();
        int streamId = clientData.getMdgOutputStreamId();
        clientMDGSubscriber = new ClientMDGSubscriber(url, streamId, snapShotSemaphore, securityId);
        Thread thread = new Thread(clientMDGSubscriber);
        thread.start();
    }

    public void init(Properties properties) throws Exception {
        initClientMarketDataGatewaySub();
        initMulticastMarketDataGatewaySub(properties);
        initTradingGatewaySub();
        loginToTradingGatewayPub();
        initMarketDataGatewayPub();
    }

    public void close(){
        while(!clientMDGSubscriber.isStop()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        tradingGatewayPub.disconnectInput();
        tradingGatewaySubscriber.close();
        marketDataGatewaySubscriber.close();
        clientMDGSubscriber.close();
        marketDataGatewayPub.disconnectInput();
    }

    private void waitForMarketDataUpdate() throws Exception{
       // if(!isAuction()) {
            long startTime = System.currentTimeMillis();
            while(!mktDataUpdateSemaphore.acquire()){
                if(System.currentTimeMillis() - startTime > 10000){
                    System.out.println("Market Data Time Out " + count);
                    break;
                }
            }
      //  }
    }

    public void loginToTradingGatewayPub() throws IOException {
        String url = clientData.getNgInputURL();
        int streamId = clientData.getNgInputStreamId();
        int compId = clientData.getCompID();
        String password = clientData.getPassword();

        tradingGatewayPub = new GatewayClientImpl();
        tradingGatewayPub.connectInput(url,streamId);

        LogonBuilder logonBuilder = new LogonBuilder();
        DirectBuffer buffer = logonBuilder.compID(compId)
                .password(password.getBytes())
                .newPassword(password.getBytes())
                .build();
        try {
            System.out.println("Message login");
            tradingGatewayPub.send(buffer);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    public DirectBuffer createNewOrder(long volume, long price, SideEnum side, OrdTypeEnum orderType, TimeInForceEnum timeInForce, String expireTime){
        String clientOrderId = LocalDateTime.now().format(formatter);
        clientOrderId = BuilderUtil.fill(clientOrderId, NewOrderEncoder.clientOrderIdLength());

        DirectBuffer directBuffer = newOrderBuilder.compID(clientData.getCompID())
                .clientOrderId(clientOrderId.getBytes())
                .account("account123".getBytes())
                .capacity(CapacityEnum.Agency)
                .cancelOnDisconnect(CancelOnDisconnectEnum.DoNotCancel)
                .orderBook(OrderBookEnum.Regular)
                .securityId(securityId)
                .traderMnemonic("John             ".getBytes())
                .orderType(orderType)
                .timeInForce(timeInForce)
                .expireTime("20211230-23:00:00".getBytes())
                .side(side)
                .orderQuantity((int) volume)
                .displayQuantity((int) volume)
                .minQuantity(0)
                .limitPrice(price)
                .stopPrice(0)
                .build();

        return directBuffer;
    }

    public void sendEndMessage(){
        System.out.println("Send end message");
        DirectBuffer buffer = adminBuilder.compID(clientData.getCompID())
                .securityId(securityId)
                .adminMessage(AdminTypeEnum.EndMessage)
                .build();

        tradingGatewayPub.send(buffer);

    }

    public void sendStartMessage(){
        System.out.println("Send start message");
        DirectBuffer buffer = adminBuilder.compID(clientData.getCompID())
                .securityId(securityId)
                .adminMessage(AdminTypeEnum.StartMessage)
                .build();
        tradingGatewayPub.send(buffer);

    }

    public void submitOrder(long volume, long price, String side, String orderType, String tif){
        tradingGatewayPub.send(createNewOrder(volume, price, SideEnum.valueOf(side), OrderTypeEnum.valueOf(orderType), TimeInForceEnum.valueOf(tif));

    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    public long getBid(){
        return bid;
    }

    public long getOffer(){
        return offer;
    }

    public void setOffer(long offer) {
        this.offer = offer;
    }

    public long getBidQuantity() {
        return bidQuantity;
    }

    public void setBidQuantity(long bidQuantity) {
        this.bidQuantity = bidQuantity;
    }

    public long getOfferQuantity() {
        return offerQuantity;
    }

    public void setOfferQuantity(long offerQuantity) {
        this.offerQuantity = offerQuantity;
    }

    public int getSecurityId() {
        return securityId;
    }

    public void setSecurityId(int securityId) {
        this.securityId = securityId;
    }

    public GatewayClient getTradingGatewayPub() {
        return tradingGatewayPub;
    }

    public boolean isAuction() {
        return auction;
    }

    public void setAuction(boolean auction) {
        this.auction = auction;
    }

    public int getClientId(){
        return clientData.getCompID();
    }
}
