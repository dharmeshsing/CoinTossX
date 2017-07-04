package crossing.strategy;

import com.carrotsearch.hppc.ObjectArrayList;
import crossing.*;
import leafNode.OrderEntry;
import orderBook.Stock;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import orderBook.OrderBook;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import crossing.preProcessor.MatchingPreProcessor.MATCHING_ACTION;
import unsafe.UnsafeUtil;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * Created by dharmeshsing on 25/07/15.
 */
@RunWith(JUnitParamsRunner.class)
public class FilterAndUncrossStrategyTest {

    private static final int STOCK_ID = 1;
    private OrderBook orderBook;
    private OrderBook expectedOrderBook;
    private PriceTimePriorityStrategy priceTimePriorityStrategy;
    private FilterAndUncrossStrategy filterAndUncrossStrategy;

    @Before
    public void setup(){
        orderBook = new OrderBook(STOCK_ID);
        expectedOrderBook = new OrderBook(STOCK_ID);
        priceTimePriorityStrategy = new PriceTimePriorityStrategy();
        filterAndUncrossStrategy = new FilterAndUncrossStrategy();
    }

    @After
    public void tearDown(){
        orderBook.freeAll();
        expectedOrderBook.freeAll();
    }

    @Test
    @Parameters(method = "provideFilterData")
    public void testProcess(OrderData orderData) throws Exception {
        ObjectArrayList<OrderEntry> initStateList = null;
        ObjectArrayList<OrderEntry> aggOrderList = null;
        ObjectArrayList<OrderEntry> expStateList = null;

        try {

            if (orderData.getTestNumber() == 3) {
                System.out.println("test");
            }

            Stock stock = orderData.getStock();
            orderBook.setStock(stock);
            expectedOrderBook.setStock(stock);

            initStateList = orderData.getInitState();
            Object[] initArr = initStateList.buffer;
            for(int i=0; i<initStateList.size(); i++){
                OrderEntry oe = (OrderEntry)initArr[i];
                priceTimePriorityStrategy.process(MATCHING_ACTION.ADD_ORDER,orderBook,oe);
            }

            aggOrderList = orderData.getAggOrder();
            Object[] aggArr = aggOrderList.buffer;
            for (int i = 0; i < aggOrderList.size(); i++) {
                OrderEntry oe = (OrderEntry) aggArr[i];
                priceTimePriorityStrategy.process(MATCHING_ACTION.ADD_ORDER,orderBook,oe);
            }

            if(orderBook.isBestBidOfferChanged()){
                filterAndUncrossStrategy.process(priceTimePriorityStrategy,orderBook);
                orderBook.setBestBidOfferChanged(false);
            }

            expStateList = orderData.getExpState();
            Object[] expArr = expStateList.buffer;
            for(int i=0; i<expStateList.size(); i++){
                OrderEntry oe = (OrderEntry)expArr[i];
                priceTimePriorityStrategy.process(MATCHING_ACTION.ADD_ORDER,expectedOrderBook,oe);
            }

            assertEquals("Test Order Book " + orderData.getTestNumber() + " failed", expectedOrderBook, orderBook);
            assertEquals("Test Trades" + orderData.getTestNumber() + " failed", orderData.getTrades(), orderBook.getTrades());

        }finally{
            UnsafeUtil.freeOrderEntryMemory(initStateList);
            UnsafeUtil.freeOrderEntryMemory(aggOrderList);
            UnsafeUtil.freeOrderEntryMemory(expStateList);
        }
    }

    public static Object[] provideFilterData() {
        OrderLoader orderLoader = new OrderLoader();
        try {
            ObjectArrayList<OrderData> orderDataList = orderLoader.getFilterAndUncrossOrders();
            //TODO:Remove this. Only used for testing
            orderDataList.trimToSize();
            Object[] arr = orderDataList.buffer;
            //org.apache.commons.lang3.ArrayUtils.reverse(arr);
            return arr;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}