package hdr;

import com.carrotsearch.hppc.LongObjectHashMap;
import com.carrotsearch.hppc.cursors.LongObjectCursor;
import crossing.CrossingProcessor;
import dao.OrderBookDAO;
import dao.TraderDAO;
import orderBook.OrderBook;
import org.HdrHistogram.Histogram;
import sbe.builder.NewOrderBuilder;
import sbe.msg.*;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by dharmeshsing on 17/08/15.
 */
public class CrossingProcessorHDRTest {
    public Properties properties;
    public NewOrderBuilder newOrderBuilder = new NewOrderBuilder();

    static Histogram histogram = new Histogram(3600000000000L, 3);
    static long WARMUP_EXECUTIONS = 1000;
    static long RUN_EXECUTIONS = 2000;

    public  void loadProperties(String propertiesFile) throws IOException {
        //try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFile)) {
        try(InputStream inputStream = new FileInputStream(Paths.get("").toAbsolutePath().getParent() + "/MatchingEngine/build/install/MatchingEngine/resources/" + propertiesFile)) {

            if (inputStream != null) {
                properties = new Properties();
                properties.load(inputStream);
            } else {
                throw new IOException("Unable to load properties file " + propertiesFile);
            }
        }
    }

    public DirectBuffer createNewOrder(){
        return newOrderBuilder.compID(1)
                .clientOrderId("1                   ".getBytes())
                .account("account123".getBytes())
                .capacity(CapacityEnum.Agency)
                .cancelOnDisconnect(CancelOnDisconnectEnum.DoNotCancel)
                .orderBook(OrderBookEnum.Regular)
                .securityId(1)
                .traderMnemonic("John             ".getBytes())
                .orderType(OrdTypeEnum.Limit)
                .timeInForce(TimeInForceEnum.Day)
                .expireTime("20150813-23:00:00".getBytes())
                .side(SideEnum.Buy)
                .orderQuantity(10)
                .displayQuantity(10)
                .minQuantity(1000)
                .limitPrice(1000)
                .stopPrice(0)
                .build();
    }

    public void addOrder(CrossingProcessor crossingProcessor) throws Exception{
        long startTime = System.nanoTime();

        crossingProcessor.processOrder(createNewOrder());

        long endTime = System.nanoTime();
        histogram.recordValue(endTime - startTime);
    }

    public static void main(String[] args) throws Exception {
        CrossingProcessorHDRTest test = new CrossingProcessorHDRTest();
        test.loadProperties("MatchingEngine.properties");

        String dataPath = test.properties.getProperty("DATA_PATH");
        LongObjectHashMap<OrderBook> orderBooks = OrderBookDAO.loadOrderBooks(dataPath);
        TraderDAO.loadTraders(dataPath);
        CrossingProcessor crossingProcessor = new CrossingProcessor(orderBooks);

        for(int i=0 ;i< WARMUP_EXECUTIONS; i++){
            test.addOrder(crossingProcessor);
        }

        histogram.reset();

        for(LongObjectCursor<OrderBook> objectCursor: orderBooks){
            objectCursor.value.freeAll();
        }

        for(int i=0 ;i< RUN_EXECUTIONS; i++){
            test.addOrder(crossingProcessor);
        }

        histogram.outputPercentileDistribution(System.out, 1000.0);
    }
}
