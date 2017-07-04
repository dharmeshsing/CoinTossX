package crossing.preProcessor;

import common.OrderType;
import crossing.TestOrderEntryFactory;
import crossing.preProcessor.MatchingPreProcessor.MATCHING_ACTION;
import leafNode.OrderEntry;
import leafNode.OrderList;
import leafNode.OrderListImpl;
import orderBook.OrderBook;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 13/07/15.
 */
public class HiddenOrderPreProcessorTest {

    private HiddenOrderPreProcessor hiddenOrderPreProcessor;

    @Before
    public void setup(){
        hiddenOrderPreProcessor = new HiddenOrderPreProcessor();
    }

    @Test
    public void testPreProcessHO() throws Exception {
        OrderBook orderBook = new OrderBook(1);
        OrderList orderList = new OrderListImpl();
        OrderEntry orderEntry = TestOrderEntryFactory.createHOOrderEntry("10:00");
        orderList.add(orderEntry);
        orderBook.getOfferTree().put(orderEntry.getPrice(), orderList);

        MATCHING_ACTION result = hiddenOrderPreProcessor.preProcess(orderBook, 1000, 100L, OrderBook.SIDE.BID);
        assertEquals(MATCHING_ACTION.AGGRESS_ORDER,result);
    }

    @Test
    public void testPreProcessHOAggressHO() throws Exception {
        OrderBook orderBook = new OrderBook(1);
        OrderList orderList = new OrderListImpl();
        OrderEntry orderEntry = TestOrderEntryFactory.createHOOrderEntry("10:00");
        orderEntry.setMinExecutionSize(1000);
        orderList.add(orderEntry);
        orderBook.getOfferTree().put(orderEntry.getPrice(), orderList);

        MATCHING_ACTION result = hiddenOrderPreProcessor.preProcess(orderBook, 1000, 100L, OrderBook.SIDE.BID);
        assertEquals(MATCHING_ACTION.AGGRESS_ORDER,result);
    }

    @Test
    public void testPreProcessHOAddOrder() throws Exception {
        OrderBook orderBook = new OrderBook(1);
        OrderList orderList = new OrderListImpl();
        OrderEntry orderEntry = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry.setQuantity(200);
        orderList.add(orderEntry);
        orderBook.getOfferTree().put(orderEntry.getPrice(), orderList);

        MATCHING_ACTION result = hiddenOrderPreProcessor.preProcess(orderBook, 500, 100L, OrderBook.SIDE.BID);
        assertEquals(MATCHING_ACTION.ADD_ORDER,result);
    }

    @Test
    public void testPreProcessHOAggressAgainstMutipleOrders() throws Exception {
        OrderBook orderBook = new OrderBook(1);
        OrderList orderList = new OrderListImpl();
        OrderEntry orderEntry1 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry1.setQuantity(200);
        orderList.add(orderEntry1);
        orderBook.getOfferTree().put(orderEntry1.getPrice(), orderList);

        OrderEntry orderEntry2 = TestOrderEntryFactory.createOrderEntry("11:00");
        orderEntry2.setQuantity(300);
        orderList.add(orderEntry2);
        orderBook.getOfferTree().put(orderEntry2.getPrice(), orderList);

        MATCHING_ACTION result = hiddenOrderPreProcessor.preProcess(orderBook, 500, 100L, OrderBook.SIDE.BID);
        assertEquals(MATCHING_ACTION.AGGRESS_ORDER,result);
    }

    @Test
    public void testPreProcessHOAggressAgainstMutipleOrdersHO() throws Exception {
        OrderBook orderBook = new OrderBook(1);
        OrderList orderList = new OrderListImpl();
        OrderEntry orderEntry1 = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry1.setQuantity(200);
        orderList.add(orderEntry1);
        orderBook.getOfferTree().put(orderEntry1.getPrice(), orderList);

        OrderEntry orderEntry2 = TestOrderEntryFactory.createOrderEntry("11:00");
        orderEntry2.setQuantity(300);
        orderList.add(orderEntry2);
        orderBook.getOfferTree().put(orderEntry2.getPrice(), orderList);

        //The test should skip this hidden order
        OrderEntry orderEntry3 = TestOrderEntryFactory.createOrderEntry("11:00");
        orderEntry3.setQuantity(2000);
        orderEntry3.setType(OrderType.HIDDEN_LIMIT.getOrderType());
        orderEntry3.setMinExecutionSize(2000);
        orderList.add(orderEntry3);
        orderBook.getOfferTree().put(orderEntry3.getPrice(), orderList);

        OrderEntry orderEntry4 = TestOrderEntryFactory.createOrderEntry("11:00");
        orderEntry4.setQuantity(500);
        orderEntry4.setType(OrderType.HIDDEN_LIMIT.getOrderType());
        orderEntry4.setMinExecutionSize(500);
        orderList.add(orderEntry4);
        orderBook.getOfferTree().put(orderEntry4.getPrice(), orderList);

        MATCHING_ACTION result = hiddenOrderPreProcessor.preProcess(orderBook, 1000, 100L, OrderBook.SIDE.BID);
        assertEquals(MATCHING_ACTION.AGGRESS_ORDER,result);
    }
}