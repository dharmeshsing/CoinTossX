package crossing.preProcessor;

import com.carrotsearch.hppc.LongLongHashMap;
import com.carrotsearch.hppc.LongLongMap;
import com.carrotsearch.hppc.ObjectArrayList;
import crossing.MatchingContext;
import crossing.MatchingUtil;
import crossing.OrderData;
import crossing.OrderLoader;
import crossing.postProcessor.StopOrderPostProcessor;
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
import unsafe.UnsafeUtil;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 25/07/15.
 */
@RunWith(JUnitParamsRunner.class)
public class ReplaceOrderPreProcessorTest {

    private static final int STOCK_ID = 1;
    private OrderBook orderBook;
    private OrderBook expectedOrderBook;
    private ReplaceOrderPreProcessor replaceOrderStrategy;
    private PriceTimePriorityStrategy priceTimePriorityStrategy;
    private StopOrderPostProcessor stopOrderPostProcessor;

    @Before
    public void setup(){
        orderBook = new OrderBook(STOCK_ID);
        expectedOrderBook = new OrderBook(STOCK_ID);
        priceTimePriorityStrategy = new PriceTimePriorityStrategy();
        replaceOrderStrategy = new ReplaceOrderPreProcessor();
        stopOrderPostProcessor = new StopOrderPostProcessor();
    }

    @After
    public void tearDown(){
        orderBook.freeAll();
        expectedOrderBook.freeAll();
    }

    @Test
    @Parameters(method = "provideReplaceOrderData")
    public void testProcess(OrderData orderData) throws Exception {
        ObjectArrayList<OrderEntry> initStateList = null;
        ObjectArrayList<OrderEntry> aggOrderList = null;
        ObjectArrayList<OrderEntry> expStateList = null;
        LongLongMap orderPriceMap = new LongLongHashMap();

        try {

            Stock stock = orderData.getStock();
            orderBook.setStock(stock);
            expectedOrderBook.setStock(stock);

            if (orderData.getTestNumber() == 23) {
                System.out.println("test");
            }

            initStateList = orderData.getInitState();
            Object[] initArr = initStateList.buffer;
            for(int i=0; i<initStateList.size(); i++){
                OrderEntry oe = (OrderEntry)initArr[i];
                if(MatchingUtil.isParkedOrder(oe)){
                    priceTimePriorityStrategy.process(MatchingPreProcessor.MATCHING_ACTION.PARK_ORDER, orderBook, oe);
                }else {
                    priceTimePriorityStrategy.process(MatchingPreProcessor.MATCHING_ACTION.AGGRESS_ORDER, orderBook, oe);
                }

                orderPriceMap.put(oe.getOrderId(),oe.getPrice());
            }


            MatchingPreProcessor.MATCHING_ACTION action;
            aggOrderList = orderData.getAggOrder();
            Object[] aggArr = aggOrderList.buffer;
            for (int i = 0; i < aggOrderList.size(); i++) {
                OrderEntry oe = (OrderEntry) aggArr[i];
                //long currentOrderPrice = orderPriceMap.get(oe.getOrderId());

                MatchingContext mc = MatchingContext.INSTANCE;
                mc.setTemplateId(OrderCancelReplaceRequestEncoder.TEMPLATE_ID);
                mc.setOrderBook(orderBook);
                mc.setOrderEntry(oe);

                replaceOrderStrategy.preProcess(mc);
                priceTimePriorityStrategy.process(mc.getAction(), orderBook, oe);
            }

            stopOrderPostProcessor.postProcess(priceTimePriorityStrategy,orderBook);

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

    public static Object[] provideReplaceOrderData() {
        OrderLoader orderLoader = new OrderLoader();
        try {
            ObjectArrayList<OrderData> orderDataList = orderLoader.getReplaceOrders();
            //TODO:Remove this. Only used for testing
            orderDataList.trimToSize();
            Object[] arr = orderDataList.buffer;
           // org.apache.commons.lang3.ArrayUtils.reverse(arr);
            return arr;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}