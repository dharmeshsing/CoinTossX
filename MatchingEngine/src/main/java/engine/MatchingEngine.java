package engine;

import aeron.AeronPublisher;
import aeron.AeronSubscriber;
import com.carrotsearch.hppc.LongObjectHashMap;
import com.carrotsearch.hppc.ObjectArrayList;
import com.carrotsearch.hppc.cursors.LongObjectCursor;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import crossing.CrossingProcessor;
import crossing.LOBManager;
import crossing.MatchingContext;
import crossing.tradingSessions.TradingSessionFactory;
import dao.OrderBookDAO;
import dao.TraderDAO;
import data.ExecutionReportData;
import data.HDRData;
import data.MarketData;
import orderBook.OrderBook;
import sbe.msg.marketData.TradingSessionEnum;
import uk.co.real_logic.aeron.logbuffer.FragmentHandler;
import uk.co.real_logic.aeron.logbuffer.Header;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class  MatchingEngine implements FragmentHandler{

    protected Properties properties;
    private static final String PROPERTIES_FILE =  "MatchingEngine.properties";
    private AeronSubscriber subscriber;
    private AeronPublisher tradingGatewayPublisher;
    private AeronPublisher marketDataPublisher;
    private LOBManager lobManager;
    private UnsafeBuffer temp = new UnsafeBuffer(ByteBuffer.allocate(106));
    private LongObjectHashMap<OrderBook>  orderBooks;
    private static AtomicBoolean running = new AtomicBoolean(false);
    private static long startTime;

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

    public static long getStartTime(){
        return startTime;
    }

    public static void setStartTime(long value){
        startTime = value;
    }

    public void initialize(){
        try {
            loadProperties(PROPERTIES_FILE);
            initTraders();
            orderBooks = initOrderBooks();
            initCrossingProcessor(orderBooks);
            initGatewaySubscriber();
            initTradingGatewayPublisher();
            initMarketDataPublisher();
            initHDR();
            TradingSessionFactory.initTradingSessionProcessors(orderBooks);
            initOrderBookTradingSessions();
            startTime = System.currentTimeMillis();
        } catch (Exception e) {
            //TODO:Handle Exception
            e.printStackTrace();
        }
    }

    private void initHDR(){
        HDRData.INSTANCE.setDataPath(properties.getProperty("DATA_PATH"));
    }

    public boolean start() {
        System.out.println("Matching Engine Started");
        running.set(true);
        subscriber.start();
        return true;
    }

    public static void setRunning(boolean value){
        running.set(value);
    }

    private void initGatewaySubscriber(){
        String mediaDriverConextDir = properties.getProperty("MEDIA_DRIVER_DIR");
        subscriber = new AeronSubscriber(mediaDriverConextDir,this);

        String url = properties.getProperty("SUB_GATEWAY_URL");
        int streamId = Integer.parseInt(properties.getProperty("SUB_GATEWAY_STREAM_ID"));

        subscriber.addSubscriber(url, streamId);
    }

    private void initTradingGatewayPublisher(){
        String mediaDriverConextDir = properties.getProperty("MEDIA_DRIVER_DIR");
        tradingGatewayPublisher = new AeronPublisher(mediaDriverConextDir);

        String url = properties.getProperty("PUB_TRADING_GATEWAY_URL");
        int streamId = Integer.parseInt(properties.getProperty("PUB_TRADING_GATEWAY_STREAM_ID"));

        tradingGatewayPublisher.addPublication(url, streamId);
    }

    private void initMarketDataPublisher(){
        String mediaDriverConextDir = properties.getProperty("MEDIA_DRIVER_DIR");
        marketDataPublisher = new AeronPublisher(mediaDriverConextDir);

        String url = properties.getProperty("PUB_MARKET_DATA_URL");
        int streamId = Integer.parseInt(properties.getProperty("PUB_MARKET_DATA_STREAM_ID"));

        marketDataPublisher.addPublication(url, streamId);
    }

    private LongObjectHashMap<OrderBook> initOrderBooks() throws IOException {
        String dataPath = properties.getProperty("DATA_PATH");
        return OrderBookDAO.loadOrderBooks(dataPath);
    }

    private void initTraders() throws IOException {
        String dataPath = properties.getProperty("DATA_PATH");
        TraderDAO.loadTraders(dataPath);
    }

    private void initCrossingProcessor(LongObjectHashMap<OrderBook> orderBooks){
        lobManager = new CrossingProcessor(orderBooks);
    }

    private void initOrderBookTradingSessions(){
        Iterator<LongObjectCursor<OrderBook>> iterator = orderBooks.iterator();
        while (iterator.hasNext()) {
            LongObjectCursor<OrderBook> orderBook = iterator.next();
            MatchingContext.INSTANCE.setOrderBookTradingSession(orderBook.value.getSecurityId(), TradingSessionEnum.ContinuousTrading);
        }
    }

    private void clearOrderBooks(){
        Iterator<LongObjectCursor<OrderBook>> iterator = orderBooks.iterator();
        while (iterator.hasNext()) {
           iterator.next().value.freeAll();
        }
    }

    public boolean stop() {
        subscriber.stop();
        tradingGatewayPublisher.stop();
        marketDataPublisher.stop();
        clearOrderBooks();
        return true;
    }


    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
        long startTime = System.nanoTime();

        try {
            temp.wrap(buffer, offset, length);
            DirectBuffer report = lobManager.processOrder(temp);
            if (lobManager.isClientMarketDataRequest()) {
                publishClientMktData();
            } else {
                publishReportToTradingGateway(report);
                publishToMarketDataGateway();
            }

            HDRData.INSTANCE.updateHDR(startTime);
            if (running.get() == false) {
                stop();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void publishReportToTradingGateway(DirectBuffer buffer){
        tradingGatewayPublisher.send(buffer);
    }

    private void publishToMarketDataGateway(){
        DirectBuffer header = MarketData.INSTANCE.buildUnitHeader();
        marketDataPublisher.send(header);

        ObjectArrayList<DirectBuffer> messages = MarketData.INSTANCE.getMktDataMessages();
        for(ObjectCursor<DirectBuffer> cursor : messages){
            marketDataPublisher.send(cursor.value);
        }

        DirectBuffer orderViewBuffer = ExecutionReportData.INSTANCE.getOrderView();
        if(orderViewBuffer != null) {
            marketDataPublisher.send(orderViewBuffer);
        }
    }

    private void publishClientMktData(){
        DirectBuffer header = MarketData.INSTANCE.buildUnitHeader();
        marketDataPublisher.send(header);

        if(MarketData.INSTANCE.isSnapShotRequest()){
            MarketData.INSTANCE.lobSnapShot(marketDataPublisher);
        }

        ObjectArrayList<DirectBuffer> messages = MarketData.INSTANCE.getMktDataMessages();
        for(ObjectCursor<DirectBuffer> cursor : messages){
            marketDataPublisher.send(cursor.value);
        }
    }

    public static void main(String[] args){
        MatchingEngine matchingEngine = new MatchingEngine();
        matchingEngine.initialize();
        matchingEngine.start();
    }
}
