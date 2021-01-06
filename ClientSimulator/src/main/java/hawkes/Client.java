package hawkes;

import gateway.client.GatewayClient;
import gateway.client.GatewayClientImpl;
import org.apache.commons.math3.distribution.NormalDistribution;
import sbe.builder.AdminBuilder;
import sbe.builder.BuilderUtil;
import sbe.builder.LogonBuilder;
import sbe.builder.NewOrderBuilder;
import sbe.msg.*;
import uk.co.real_logic.agrona.DirectBuffer;
import client.ClientData;
import client.TradingGatewaySubscriber;
import client.NonBlockingSemaphore;
import client.ClientMDGSubscriber;

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
    private List<HawkesData> hawkesData;

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

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private long uniqueOrderId;

    private Random random = new Random();
    private int securityId;
    private NonBlockingSemaphore mktDataUpdateSemaphore = new NonBlockingSemaphore(1);
    private NonBlockingSemaphore snapShotSemaphore = new NonBlockingSemaphore(1);

    private static long LOWEST_BID = 25000;
    private static long STARTING_BID = 25034;

    private static long HIGHEST_OFFER = 25100;
    private static long STARTING_OFFER = 25057;

    private boolean auction = false;
    private int count;

    public Client(ClientData clientData,List<HawkesData> hawkesData,int securityId){
        this.clientData = clientData;
        this.hawkesData = hawkesData;
        this.securityId = securityId;
    }

    public void initTradingGatewaySub(){
        String url = clientData.getNgOutputURL();
        int streamId = clientData.getNgOutputStreamId();
        tradingGatewaySubscriber = new TradingGatewaySubscriber(url,streamId);
        Thread thread = new Thread(tradingGatewaySubscriber);
        thread.start();
    }

    public void initMarketDataGatewayPub() {
        String url = clientData.getMdgInputURL();
        int streamId = clientData.getMdgInputStreamId();
        marketDataGatewayPub = new GatewayClientImpl();
        marketDataGatewayPub.connectInput(url,streamId);
    }

    public void initMulticastMarketDataGatewaySub(Properties properties){
        String url = properties.get("MDG_MULTICAST_URL").toString();
        int streamId = Integer.parseInt(properties.get("MDG_MULTICAST_STREAM_ID").toString());

        marketDataGatewaySubscriber = new MulticastMDGSubscriber(url,streamId,this,mktDataUpdateSemaphore);
        Thread thread = new Thread(marketDataGatewaySubscriber);
        thread.start();
    }

    public void initClientMarketDataGatewaySub(){
        String url = clientData.getMdgOutputURL();
        int streamId = clientData.getMdgOutputStreamId();

        clientMDGSubscriber = new ClientMDGSubscriber(url,streamId,snapShotSemaphore,securityId);
        Thread thread = new Thread(clientMDGSubscriber);
        thread.start();
    }

    public void init(Properties properties) throws Exception {
        initClientMarketDataGatewaySub();
        initMulticastMarketDataGatewaySub(properties);
        initTradingGatewaySub();
        loginToTradingGatewayPub();
        initMarketDataGatewayPub();
        initLimitOrderBook();
    }

    private void close(){
        tradingGatewayPub.disconnectInput();
        tradingGatewaySubscriber.close();
        marketDataGatewaySubscriber.close();
        clientMDGSubscriber.close();
        marketDataGatewayPub.disconnectInput();
    }

    public void process() {
        System.out.println("Start at " + LocalDateTime.now());
        sendStartMessage();
        System.out.println("Size = " + hawkesData.size());
        execute(hawkesData);
        sendEndMessage();
        while(!clientMDGSubscriber.isStop()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        close();
        System.out.println("Complete at " + LocalDateTime.now());
        System.exit(0);
    }

    private void execute(List<HawkesData> hawkesTimes){
        try {
            boolean sent = false;
            for(int i=0; i<hawkesTimes.size(); i++){

                waitForMarketDataUpdate();

                HawkesData hd = hawkesTimes.get(i);
                long start = System.nanoTime();
                if(i != 0) {
                    long delay = hd.getDelay() - hawkesTimes.get(i-1).getDelay();
                    while(System.nanoTime() - start < delay);
                }

                switch(hd.getType()){
                    case 1 :  sent = aggressiveBuyTrade(); break;
                    case 2 :  sent = aggressiveSellTrade(); break;
                    case 3 :  sent = aggressiveBuyQuotes(); break;
                    case 4 :  sent = aggressiveSellQuotes(); break;
                    case 5 :  sent = passiveBuyTrade(); break;
                    case 6 :  sent = passiveSellTrade(); break;
                    case 7 :  sent = passiveBuyQuote(); break;
                    case 8 :  sent = passiveSellQuote();
                        break;
                }

                if(!sent){
                    mktDataUpdateSemaphore.release();
                }
                count = i;

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void waitForMarketDataUpdate() throws Exception{
       // if(!isAuction()) {
            long startTime = System.currentTimeMillis();
            while(!mktDataUpdateSemaphore.acquire()){
                if(System.currentTimeMillis() - startTime > 100_000){
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

        for(int i=0; i<3; i++){
            try {
                System.out.println("Message login");
                tradingGatewayPub.send(buffer);
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    public DirectBuffer createNewOrder(long volume, long price,SideEnum side,OrdTypeEnum orderType){
        String clientOrderId = LocalDateTime.now().format(formatter) + "_" + uniqueOrderId++;
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
                .timeInForce(TimeInForceEnum.Day)
                .expireTime("20150813-23:00:00".getBytes())
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

    private void initLimitOrderBook(){
        tradingGatewayPub.send(createNewOrder(1200, STARTING_BID, SideEnum.Buy, OrdTypeEnum.Limit));
        tradingGatewayPub.send(createNewOrder(1000, STARTING_OFFER, SideEnum.Sell, OrdTypeEnum.Limit));

        while(bid <= 0 && offer <= 0){
            System.out.println("waiting for initialization");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean aggressiveBuyTrade(){
        IntPredicate predicate = x -> x >= offerQuantity && (x % 100 == 0);
        long vol = getVolume(offerQuantity,predicate);
        long vwap = calcVWAP(SideEnum.Sell);

        if(vol > 0 && vwap > 0){
            tradingGatewayPub.send(createNewOrder(vol, vwap, SideEnum.Buy, OrdTypeEnum.Market));
            return true;
        }
        return false;
    }

    public boolean aggressiveSellTrade(){
        IntPredicate predicate = x -> x >= bidQuantity && (x % 100 == 0);
        long vol = getVolume(bidQuantity, predicate);
        long vwap = calcVWAP(SideEnum.Buy);

        if(vol > 0 && vwap > 0){
            tradingGatewayPub.send(createNewOrder(vol ,vwap, SideEnum.Sell,OrdTypeEnum.Market));
            return true;
        }
        return false;
    }

    public boolean aggressiveBuyQuotes(){
        DoublePredicate pricePredicate = null;
        long meanPrice = bid;

        if(bid !=0 && offer != 0) {
            pricePredicate = x -> x > bid && x <= offer && x >= LOWEST_BID;
            meanPrice = bid;
        }else if(bid == 0 && offer != 0){
            pricePredicate = x -> x > bid && x <= offer && x >= LOWEST_BID;
            meanPrice = offer;
        }else if(bid != 0 && offer == 0){
            pricePredicate = x -> x > bid && x >= LOWEST_BID;
            meanPrice = bid;
        }else if(bid == 0 && offer == 0){
            pricePredicate = x -> x > bid && x >= LOWEST_BID;
            meanPrice = STARTING_BID;
        }

        long price = getPrice(meanPrice, pricePredicate);

        IntPredicate volPredicate = x -> x > 0  && (x % 100 == 0);
        long vol = getVolume(bidQuantity, volPredicate);

        if(price > 0 && vol > 0) {
            tradingGatewayPub.send(createNewOrder(vol, price, SideEnum.Buy, OrdTypeEnum.Limit));
            return true;
        }
        return false;
    }

    public boolean aggressiveSellQuotes(){
        DoublePredicate pricePredicate = null;
        long meanPrice = offer;

        if(bid !=0 && offer != 0) {
            pricePredicate = x -> x < offer && x >= bid && x <= HIGHEST_OFFER;
            meanPrice = offer;
        }else if(bid == 0 && offer != 0){
            pricePredicate = x -> x < offer && x >= bid && x <= HIGHEST_OFFER;
            meanPrice = offer;
        }else if(bid != 0 && offer == 0){
            pricePredicate = x -> x >= bid && x <= HIGHEST_OFFER;
            meanPrice = bid;
        }else if(bid == 0 && offer == 0){
            pricePredicate = x -> x >= bid && x <= HIGHEST_OFFER;
            meanPrice = STARTING_OFFER;
        }

        long price = getPrice(meanPrice, pricePredicate);

        IntPredicate volPredicate = x -> x > 0  && (x % 100 == 0);
        long vol = getVolume(offerQuantity, volPredicate);

        if(price > 0 && vol > 0) {
            tradingGatewayPub.send(createNewOrder(vol, price, SideEnum.Sell, OrdTypeEnum.Limit));
            return true;
        }
        return false;
    }

    public boolean passiveBuyTrade(){
        IntPredicate predicate = x -> x < offerQuantity && x > 0  && (x % 100 == 0);
        long vol = getVolume(offerQuantity, predicate);
        if(vol > 0 && offer > 0){
            tradingGatewayPub.send(createNewOrder(vol, offer, SideEnum.Buy, OrdTypeEnum.Market));
            return true;
        }
        return false;
    }

    public boolean passiveSellTrade(){
        IntPredicate predicate = x -> x < bidQuantity && x > 0  && (x % 100 == 0);
        long vol = getVolume(bidQuantity, predicate);
        if(vol > 0 && bid > 0){
            tradingGatewayPub.send(createNewOrder(vol ,bid, SideEnum.Sell,OrdTypeEnum.Market));
            return true;
        }
        return false;
    }

    public boolean passiveBuyQuote(){
        DoublePredicate pricePredicate = null;
        long meanPrice = bid;

        if(bid !=0 && offer != 0) {
            pricePredicate = x -> x < bid && x <= offer && x >= LOWEST_BID;
            meanPrice = bid;
        }else if(bid == 0 && offer != 0){
            pricePredicate = x -> x <= offer && x >= LOWEST_BID;
            meanPrice = offer;
        }else if(bid != 0 && offer == 0){
            pricePredicate = x -> x < bid && x >= LOWEST_BID;
            meanPrice = bid;
        }else if(bid == 0 && offer == 0){
            pricePredicate = x -> x >= LOWEST_BID && x<= HIGHEST_OFFER;
            meanPrice = STARTING_BID;
        }

        long price = getPrice(meanPrice, pricePredicate);

        IntPredicate volPredicate = x -> x > 0  && (x % 100 == 0);
        long vol = getVolume(bidQuantity, volPredicate);

        if(price > 0 && vol > 0) {
            tradingGatewayPub.send(createNewOrder(vol, price, SideEnum.Buy, OrdTypeEnum.Limit));
            return true;
        }
        return false;
    }

    public boolean passiveSellQuote(){
        DoublePredicate pricePredicate = null;
        long meanPrice = offer;

        if(bid !=0 && offer != 0) {
            pricePredicate = x -> x > offer && x >= bid && x <= HIGHEST_OFFER;
            meanPrice = offer;
        }else if(bid == 0 && offer != 0){
            pricePredicate = x -> x > offer && x >= bid && x <= HIGHEST_OFFER;
            meanPrice = offer;
        }else if(bid != 0 && offer == 0){
            pricePredicate = x -> x > offer && x >= bid && x <= HIGHEST_OFFER;
            meanPrice = bid;
        }else if(bid == 0 && offer == 0){
            pricePredicate = x -> x >= LOWEST_BID && x<= HIGHEST_OFFER;
            meanPrice = STARTING_OFFER;
        }

        long price = getPrice(meanPrice, pricePredicate);

        IntPredicate volPredicate = x -> x > 0  && (x % 100 == 0);
        long vol = getVolume(offerQuantity, volPredicate);

        if(price > 0 && vol > 0) {
            tradingGatewayPub.send(createNewOrder(vol, price, SideEnum.Sell, OrdTypeEnum.Limit));
            return true;
        }
        return false;
    }

    private long getVolume(long volume,IntPredicate predicate){
        NormalDistribution volumenND = new NormalDistribution(volume, 1000);
        int[] volumes = Arrays.stream(volumenND.sample(1000)).mapToInt(e -> (int)Math.ceil(e)).filter(predicate).toArray();
        if(volumes.length > 0) {
            int index = random.ints(1, 0, volumes.length).findAny().getAsInt();
            return volumes[index];
        }

        return volume;
    }

    public long getPrice(double price,DoublePredicate predicate){
        NormalDistribution priceND = new NormalDistribution(price, 200);
        double[] prices = priceND.sample(1000);
        try {
            prices = Arrays.stream(prices).filter(predicate).toArray();
            if (prices.length > 0) {
                int index = random.ints(1, 0, prices.length).findAny().getAsInt();
                return Double.valueOf(prices[index]).longValue();
            }
        }catch(Exception e){
            //ignore
        }

        return  Double.valueOf(price).longValue();
    }

    public long calcVWAP(SideEnum sideEnum){
        snapShotSemaphore.acquire();
        DirectBuffer buffer = adminBuilder.compID(clientData.getCompID())
                    .securityId(securityId)
                    .adminMessage(AdminTypeEnum.VWAP)
                    .build();

        clientMDGSubscriber.setSideEnum(sideEnum);
        marketDataGatewayPub.send(buffer);

        //Wait for VWAP to be calculated
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
