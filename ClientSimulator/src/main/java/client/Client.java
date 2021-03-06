package client;

import gateway.client.GatewayClient;
import gateway.client.GatewayClientImpl;
import sbe.builder.OrderCancelRequestBuilder;
import sbe.builder.NewOrderBuilder;
import sbe.builder.AdminBuilder;
import sbe.builder.LogonBuilder;
import sbe.builder.BuilderUtil;
import sbe.builder.marketData.OrderModifiedBuilder;
import sbe.msg.*;

import uk.co.real_logic.agrona.DirectBuffer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

public class Client {

    private GatewayClient tradingGatewayPub;
    private TradingGatewaySubscriber tradingGatewaySubscriber;
    private MulticastMDGSubscriber marketDataGatewaySubscriber;
    private ClientMDGSubscriber clientMDGSubscriber;
    private GatewayClient marketDataGatewayPub;

    private NewOrderBuilder newOrderBuilder = new NewOrderBuilder().account("account123".getBytes()).capacity(CapacityEnum.Agency).cancelOnDisconnect(CancelOnDisconnectEnum.DoNotCancel).orderBook(OrderBookEnum.Regular);
    private OrderCancelRequestBuilder orderCancelRequestBuilder = new OrderCancelRequestBuilder();
    private OrderModifiedBuilder orderModifiedBuilder = new OrderModifiedBuilder();
    private AdminBuilder adminBuilder = new AdminBuilder();

    private ClientData clientData;
    private long bid;
    private long bidQuantity;
    private long offer;
    private long offerQuantity;
    private int securityId;
    private boolean auction = false;

    private NonBlockingSemaphore mktDataUpdateSemaphore = new NonBlockingSemaphore(1000000);
    private NonBlockingSemaphore snapShotSemaphore = new NonBlockingSemaphore(1);

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

    public void initMulticastMarketDataGatewaySub(Properties properties) {
        String url = properties.get("MDG_MULTICAST_URL").toString();
        int streamId = Integer.parseInt(properties.get("MDG_MULTICAST_STREAM_ID").toString());
        marketDataGatewaySubscriber = new MulticastMDGSubscriber(url, streamId, this, mktDataUpdateSemaphore);
        Thread thread = new Thread(marketDataGatewaySubscriber);
        thread.start();
    }

    public void initClientMarketDataGatewaySub() {
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

    public void sendStartMessage() {
        System.out.println("Session started at " + LocalDateTime.now() + ".");
        DirectBuffer buffer = adminBuilder.compID(clientData.getCompID())
                .securityId(securityId)
                .adminMessage(AdminTypeEnum.StartMessage)
                .build();
        tradingGatewayPub.send(buffer);
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
        for(int i=0; i<3; i++) {
            try {
                Thread.sleep(1000);
                System.out.println("Logging in.");
                tradingGatewayPub.send(buffer);
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        System.out.println("Logged in.");
    }

    public void sendEndMessage() {
        System.out.println("Session ended at " + LocalDateTime.now() + ".");
        DirectBuffer buffer = adminBuilder.compID(clientData.getCompID())
                .securityId(securityId)
                .adminMessage(AdminTypeEnum.EndMessage)
                .build();
        tradingGatewayPub.send(buffer);
    }

    public void close() {
        while(!clientMDGSubscriber.isStop()) {
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
        System.out.println("Logged out.");
    }

    public void waitForMarketDataUpdate() throws Exception { while(!mktDataUpdateSemaphore.acquire()){} }

    public DirectBuffer createOrder(int volume, int price, SideEnum side, OrdTypeEnum orderType, TimeInForceEnum timeInForce, int displayQuantity, int minQuantity, long stopPrice, String clientOrderId) {
        String traderMnemonic = BuilderUtil.fill("John", NewOrderEncoder.traderMnemonicLength());
        if (orderType == OrdTypeEnum.Limit) {
            DirectBuffer directBuffer = newOrderBuilder.compID(clientData.getCompID())
                    .clientOrderId(clientOrderId.getBytes())
                    .securityId(securityId)
                    .traderMnemonic(traderMnemonic.getBytes())
                    .orderType(orderType)
                    .timeInForce(timeInForce)
                    .expireTime("20211230-23:00:00".getBytes())
                    .side(side)
                    .orderQuantity(volume)
                    .displayQuantity(displayQuantity)
                    .minQuantity(minQuantity)
                    .limitPrice(price)
                    .stopPrice(stopPrice)
                    .build();
            return directBuffer;
        } else {
            DirectBuffer directBuffer = newOrderBuilder.compID(clientData.getCompID())
                    .clientOrderId(clientOrderId.getBytes())
                    .securityId(securityId)
                    .traderMnemonic(traderMnemonic.getBytes())
                    .orderType(orderType)
                    .timeInForce(timeInForce)
                    .expireTime("20211230-23:00:00".getBytes())
                    .side(side)
                    .orderQuantity(volume)
                    .displayQuantity(displayQuantity)
                    .minQuantity(minQuantity)
                    .stopPrice(stopPrice)
                    .build();
            return directBuffer;
        }
    }

    public void submitOrder(int volume, int price, String side, String orderType, String timeInForce, int displayQuantity, int minQuantity, long stopPrice) {
        String clientOrderId = LocalDateTime.now().toString();
        clientOrderId = BuilderUtil.fill(clientOrderId, NewOrderEncoder.clientOrderIdLength());
        DirectBuffer directBuffer = createOrder(volume, price, SideEnum.valueOf(side), OrdTypeEnum.valueOf(orderType), TimeInForceEnum.valueOf(timeInForce), displayQuantity, minQuantity, stopPrice, clientOrderId);
        tradingGatewayPub.send(directBuffer);
        System.out.println("Message=OrderAdd|Time=" + clientOrderId + "|Type=" + orderType + "|Side=" + side + "|Volume=" + volume + "(" + displayQuantity + ")" + "|Price=" + price + "|StopPrice=" + stopPrice + "|TIF=" + timeInForce + "|MES=" + minQuantity);
    }

    public void cancelOrder(String originalClientOrderId, String side) {
        String origClientOrderId = BuilderUtil.fill(originalClientOrderId, OrderCancelRequestEncoder.origClientOrderIdLength());
        String clientOrderId = LocalDateTime.now().toString();
        clientOrderId = BuilderUtil.fill(clientOrderId, OrderCancelRequestEncoder.clientOrderIdLength());
        String traderMnemonic = BuilderUtil.fill("John", OrderCancelRequestEncoder.traderMnemonicLength());

        DirectBuffer directBuffer = orderCancelRequestBuilder.compID(clientData.getCompID())
                .clientOrderId(clientOrderId.getBytes())
                .origClientOrderId(origClientOrderId.getBytes())
                .securityId(securityId)
                .traderMnemonic(traderMnemonic.getBytes())
                .side(SideEnum.valueOf(side))
                .orderBook(OrderBookEnum.Regular)
                .build();
        tradingGatewayPub.send(directBuffer);
        System.out.println("Message=OrderCancel|Time=" + clientOrderId + "|Side=" + side);
    }

    public long calcVWAP(String side) {
        snapShotSemaphore.acquire();
        DirectBuffer buffer = adminBuilder.compID(clientData.getCompID())
                    .securityId(securityId)
                    .adminMessage(AdminTypeEnum.VWAP)
                    .build();
        clientMDGSubscriber.setSideEnum(SideEnum.valueOf(side));
        marketDataGatewayPub.send(buffer);
        snapShotSemaphore.acquire();
        snapShotSemaphore.release();
        return clientMDGSubscriber.getVwap();
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

    public long getBidQuantity() { return bidQuantity; }

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
