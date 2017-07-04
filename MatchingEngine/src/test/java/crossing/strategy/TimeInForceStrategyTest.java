package crossing.strategy;

import bplusTree.BPlusTree;
import common.TimeInForce;
import crossing.TestOrderEntryFactory;
import leafNode.OrderEntry;
import leafNode.OrderList;
import leafNode.OrderListImpl;
import orderBook.OrderBook;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 29/07/15.
 */
public class TimeInForceStrategyTest {

    @Test
    public void testRemoveOrderIOC() throws Exception {
        boolean result = TimeInForceStrategy.removeOrder(100,TimeInForce.IOC.getValue());
        assertEquals(true,result);
    }

    @Test
    public void testRemoveOrderDAY() throws Exception {
        boolean result = TimeInForceStrategy.removeOrder(100,TimeInForce.DAY.getValue());
        assertEquals(false,result);
    }

    @Test
    public void testCanFOKOrderBeFilled() throws Exception {
        OrderBook orderBook = new OrderBook(1);
        OrderList orderList = new OrderListImpl();
        OrderEntry orderEntry = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry.setQuantity(1000);
        orderList.add(orderEntry);
        orderBook.getOfferTree().put(orderEntry.getPrice(), orderList);
        BPlusTree.BPlusTreeIterator iterator = orderBook.getPriceIterator(OrderBook.SIDE.OFFER);

        boolean result = TimeInForceStrategy.canFOKOrderBeFilled(iterator, 500, 100, -1, OrderBook.SIDE.BID);
        assertEquals(true,result);
    }

    @Test
    public void testFOKOrderCannotBeFilled() throws Exception {
        OrderBook orderBook = new OrderBook(1);
        OrderList orderList = new OrderListImpl();
        OrderEntry orderEntry = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry.setQuantity(1000);
        orderList.add(orderEntry);
        orderBook.getOfferTree().put(orderEntry.getPrice(), orderList);
        BPlusTree.BPlusTreeIterator iterator = orderBook.getPriceIterator(OrderBook.SIDE.OFFER);

        boolean result = TimeInForceStrategy.canFOKOrderBeFilled(iterator,2000,100,-1, OrderBook.SIDE.BID);
        assertEquals(false,result);
    }

    @Test
    public void testExpireOrder() throws Exception {
        OrderBook orderBook = new OrderBook(1);
        OrderList orderList = new OrderListImpl();
        OrderEntry orderEntry = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry.setTimeInForce(TimeInForce.OPG.getValue());
        orderList.add(orderEntry);
        orderBook.getOfferTree().put(orderEntry.getPrice(), orderList);

        TimeInForceStrategy.expireOrders(orderBook, TimeInForce.OPG);
        int result = orderBook.getOfferTree().size();

        assertEquals(0, result);
    }

    @Test
    public void testIsPastMaxDuration() throws Exception{
        DateTime submittedDate = new DateTime(2015,3,1,0,0,0);
        boolean result = TimeInForceStrategy.isPastMaxDuration(submittedDate.getMillis());
        assertEquals(true, result);
    }

    @Test
    public void testIsPastExpiryDateOrMaxDuration() throws Exception{
        DateTime submittedDate = new DateTime(2015,3,1,0,0,0);
        DateTime expiryDate = new DateTime(2015,3,2,0,0,0);
        boolean result = TimeInForceStrategy.isPastExpiryDateOrMaxDuration(expiryDate.getMillis(), submittedDate.getMillis());
        assertEquals(true, result);
    }
}