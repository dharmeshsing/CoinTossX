package crossing.preProcessor;

import com.carrotsearch.hppc.ObjectArrayList;
import crossing.MatchingContext;
import crossing.MatchingUtil;
import crossing.OrderData;
import crossing.OrderLoader;
import crossing.strategy.PriceTimePriorityStrategy;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import leafNode.OrderEntry;
import orderBook.OrderBook;
import orderBook.Stock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import sbe.msg.OrderCancelReplaceRequestEncoder;
import sbe.msg.OrderCancelRequestEncoder;
import unsafe.UnsafeUtil;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 25/07/15.
 */
@RunWith(JUnitParamsRunner.class)
public class CancelOrderPreProcessorTest {

    private static final int STOCK_ID = 1;
    private OrderBook orderBook;
    private OrderBook expectedOrderBook;
    private CancelOrderPreProcessor cancelOrderPreProcessor;
    private PriceTimePriorityStrategy priceTimePriorityStrategy;

    @Before
    public void setup(){
        orderBook = new OrderBook(STOCK_ID);
        expectedOrderBook = new OrderBook(STOCK_ID);
        priceTimePriorityStrategy = new PriceTimePriorityStrategy();
        cancelOrderPreProcessor = new CancelOrderPreProcessor();
    }

    @After
    public void tearDown(){
        orderBook.freeAll();
        expectedOrderBook.freeAll();
    }

    @Test
    @Parameters(method = "provideCancelOrderData")
    public void testProcess(OrderData orderData) throws Exception {
        ObjectArrayList<OrderEntry> initStateList = null;
        ObjectArrayList<OrderEntry> aggOrderList = null;
        ObjectArrayList<OrderEntry> expStateList = null;

        try {

            Stock stock = orderData.getStock();
            orderBook.setStock(stock);
            expectedOrderBook.setStock(stock);

            initStateList = orderData.getInitState();
            Object[] initArr = initStateList.buffer;
            for(int i=0; i<initStateList.size(); i++){
                OrderEntry oe = (OrderEntry)initArr[i];
                if(MatchingUtil.isParkedOrder(oe)){
                    priceTimePriorityStrategy.process(MatchingPreProcessor.MATCHING_ACTION.PARK_ORDER, orderBook, oe);
                }else {
                    priceTimePriorityStrategy.process(MatchingPreProcessor.MATCHING_ACTION.ADD_ORDER, orderBook, oe);
                }
            }

            aggOrderList = orderData.getAggOrder();
            Object[] aggArr = aggOrderList.buffer;
            for (int i = 0; i < aggOrderList.size(); i++) {
                OrderEntry oe = (OrderEntry) aggArr[i];

                MatchingContext mc = MatchingContext.INSTANCE;
                mc.setTemplateId(OrderCancelRequestEncoder.TEMPLATE_ID);
                mc.setOrderBook(orderBook);
                mc.setOrderEntry(oe);

                cancelOrderPreProcessor.preProcess(mc);
            }

            expStateList = orderData.getExpState();
            Object[] expArr = expStateList.buffer;
            for(int i=0; i<expStateList.size(); i++){
                OrderEntry oe = (OrderEntry)expArr[i];
                priceTimePriorityStrategy.process(MatchingPreProcessor.MATCHING_ACTION.ADD_ORDER,expectedOrderBook,oe);
            }

            assertEquals("Test Order Book " + orderData.getTestNumber() + " failed", expectedOrderBook, orderBook);
            assertEquals("Test Trades" + orderData.getTestNumber() + " failed", orderData.getTrades(), orderBook.getTrades());

        }finally{
            UnsafeUtil.freeOrderEntryMemory(initStateList);
            UnsafeUtil.freeOrderEntryMemory(aggOrderList);
            UnsafeUtil.freeOrderEntryMemory(expStateList);
        }
    }

    public static Object[] provideCancelOrderData() {
        OrderLoader orderLoader = new OrderLoader();
        try {
            ObjectArrayList<OrderData> orderDataList = orderLoader.getCancelOrders();
            //TODO:Remove this. Only used for testing
            orderDataList.trimToSize();
            Object[] arr = orderDataList.buffer;
            org.apache.commons.lang3.ArrayUtils.reverse(arr);
            return arr;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}