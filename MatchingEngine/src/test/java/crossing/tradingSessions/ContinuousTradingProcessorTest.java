package crossing.tradingSessions;

import com.carrotsearch.hppc.LongObjectHashMap;
import com.carrotsearch.hppc.ObjectArrayList;
import crossing.MatchingUtil;
import crossing.OrderData;
import crossing.OrderProvider;
import dao.OrderBookDAO;
import dao.TraderDAO;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import leafNode.OrderEntry;
import orderBook.OrderBook;
import orderBook.Stock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import sbe.msg.marketData.TradingSessionEnum;
import unsafe.UnsafeUtil;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 4/12/15.
 */
@RunWith(JUnitParamsRunner.class)
public class ContinuousTradingProcessorTest {
    private static final int STOCK_ID = 1;
    private TradingSessionProcessor continuousTradingProcessor;
    private LongObjectHashMap<OrderBook> orderBooks;
    private LongObjectHashMap<OrderBook> expectedOrderBooks;

    @Before
    public void setup() throws IOException {
        String dataPath = "/home/ivanjericevich/CoinTossX/data";
        orderBooks = OrderBookDAO.loadOrderBooks(dataPath);
        expectedOrderBooks = OrderBookDAO.loadOrderBooks(dataPath);
        TraderDAO.loadTraders(dataPath);
        TradingSessionFactory.initTradingSessionProcessors(orderBooks);
        continuousTradingProcessor = TradingSessionFactory.getTradingSessionProcessor(TradingSessionEnum.ContinuousTrading);
    }

    @After
    public void tearDown(){
        orderBooks.get(STOCK_ID).freeAll();
        TradingSessionFactory.reset();
    }

    @Test
    @Parameters(source = OrderProvider.class)
    public void testCrossingProcessor(OrderData orderData) {
        ObjectArrayList<OrderEntry> initStateList = null;
        ObjectArrayList<OrderEntry> aggOrderList = null;
        ObjectArrayList<OrderEntry> expStateList = null;
        MatchingUtil.setEnableCircuitBreaker(false);

        try {
            Stock stock = orderData.getStock();
            OrderBook orderBook = orderBooks.get(STOCK_ID);
            orderBook.setStock(stock);

            OrderBook expectedOrderBook = expectedOrderBooks.get(STOCK_ID);
            expectedOrderBook.setStock(stock);

            //expectedCrossingProcessor.getOrderBook(STOCK_ID).setStock(stock);

            if (orderData.getType().equals("Hidden Order") && orderData.getTestNumber() == 7) {
                System.out.println("test");
            }

            initStateList = orderData.getInitState();
            if (initStateList != null) {
                Object[] initArr = initStateList.buffer;
                for (int i = 0; i < initStateList.size(); i++) {
                    OrderEntry oe = (OrderEntry) initArr[i];
                    continuousTradingProcessor.process(orderBook,oe);
                }
            }

            aggOrderList = orderData.getAggOrder();
            Object[] aggArr = aggOrderList.buffer;
            for (int i = 0; i < aggOrderList.size(); i++) {
                OrderEntry oe = (OrderEntry) aggArr[i];
                continuousTradingProcessor.process(orderBook,oe);
            }


            expStateList = orderData.getExpState();
            Object[] expArr = expStateList.buffer;
            for (int i = 0; i < expStateList.size(); i++) {
                OrderEntry oe = (OrderEntry) expArr[i];
                continuousTradingProcessor.process(expectedOrderBook,oe);
            }


            assertEquals("Test Type = " + orderData.getType() + " , Test Order Book " + orderData.getTestNumber() + " failed", expectedOrderBook, orderBook);
            assertEquals("Test Type = " + orderData.getType() + " , Test Trades " + orderData.getTestNumber() + " failed", orderData.getTrades(), orderBook.getTrades());
        }finally{
            UnsafeUtil.freeOrderEntryMemory(initStateList);
            UnsafeUtil.freeOrderEntryMemory(aggOrderList);
            UnsafeUtil.freeOrderEntryMemory(expStateList);

        }
    }
}
