package crossing;

import common.OrderType;
import leafNode.OrderEntry;
import orderBook.OrderBook;
import org.junit.Test;
import unsafe.UnsafeUtil;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 13/07/15.
 */
public class MatchingUtilTest {

    @Test
    public void testConvertStopOrderToMarketOrder() throws Exception {
        OrderEntry orderEntry = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry.setType(OrderType.STOP.getOrderType());

        MatchingUtil.convertStopOrderToMarketOrLimitOrder(orderEntry);
        assertEquals(OrderType.MARKET.getOrderType(), orderEntry.getType());

        UnsafeUtil.freeOrderEntryMemory(orderEntry);
    }

    @Test
    public void testConvertStopLimitOrderToLimitOrder() throws Exception {
        OrderEntry orderEntry = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry.setType(OrderType.STOP_LIMIT.getOrderType());

        MatchingUtil.convertStopOrderToMarketOrLimitOrder(orderEntry);
        assertEquals(OrderType.LIMIT.getOrderType(), orderEntry.getType());

        UnsafeUtil.freeOrderEntryMemory(orderEntry);
    }

    @Test
    public void testCanConverStopOrderLastTradePriceZero() throws Exception {
        boolean result = MatchingUtil.canConverStopOrder(0L, OrderBook.SIDE.BID, 100L);
        assertEquals(false, result);
    }

    @Test
    public void testCanConverStopOrderLastTradePriceLessThanStopPrice() throws Exception {
        boolean result = MatchingUtil.canConverStopOrder(100L, OrderBook.SIDE.BID, 200L);
        assertEquals(false, result);
    }

    @Test
    public void testCanConverStopOrderLastTradePriceGreaterThanStopPrice() throws Exception {
        boolean result = MatchingUtil.canConverStopOrder(300L, OrderBook.SIDE.OFFER,200L);
        assertEquals(false,result);
    }

    @Test
    public void testCanConverStopOrderLastTradePriceGreaterThanBIDStopPrice() throws Exception {
        boolean result = MatchingUtil.canConverStopOrder(300L, OrderBook.SIDE.BID,200L);
        assertEquals(true,result);
    }

    @Test
    public void testCanConverStopOrderLastTradePriceLessThanOFFERStopPrice() throws Exception {
        boolean result = MatchingUtil.canConverStopOrder(100L, OrderBook.SIDE.OFFER,200L);
        assertEquals(true,result);
    }

    @Test
    public void testBidGreaterThanExistingPrice() throws Exception {
        boolean result = MatchingUtil.isValidProcessingPrice(200, 100, OrderBook.SIDE.BID);
        assertEquals(true,result);
    }

    @Test
    public void testBidLessThanExistingPrice() throws Exception {
        boolean result = MatchingUtil.isValidProcessingPrice(50, 100, OrderBook.SIDE.BID);
        assertEquals(false,result);
    }

    @Test
    public void testBidEqualToExistingPrice() throws Exception {
        boolean result = MatchingUtil.isValidProcessingPrice(100, 100, OrderBook.SIDE.BID);
        assertEquals(true,result);
    }

    @Test
    public void testOfferGreaterThanExistingPrice() throws Exception {
        boolean result = MatchingUtil.isValidProcessingPrice(200,100, OrderBook.SIDE.OFFER);
        assertEquals(false,result);
    }

    @Test
    public void testOfferLessThanExistingPrice() throws Exception {
        boolean result = MatchingUtil.isValidProcessingPrice(50,100, OrderBook.SIDE.OFFER);
        assertEquals(true,result);
    }

    @Test
    public void testExecutionQuantityAggOrderGreaterCurrentOrder() throws Exception {
        int result = MatchingUtil.getExecutionQuantity(100, 50, -1, 0);
        assertEquals(50,result);
    }

    @Test
    public void testExecutionQuantityCurrentOrderGreaterAggOrder() throws Exception {
        int result = MatchingUtil.getExecutionQuantity(50, 100, -1, 0);
        assertEquals(50,result);
    }

    @Test
    public void testExecutionWithCurrentMESGreaterThanExecutionQuantity() throws Exception {
        int result = MatchingUtil.getExecutionQuantity(50,100, 60,0);
        assertEquals(-1,result);
    }

    @Test
    public void testExecutionWithCurrentMESLessThanExecutionQuantity() throws Exception {
        int result = MatchingUtil.getExecutionQuantity(50,100, 40,0);
        assertEquals(50,result);
    }

    @Test
    public void testExecutionWithAggMESGreaterThanExecutionQuantity() throws Exception {
        int result = MatchingUtil.getExecutionQuantity(50,100, 0,60);
        assertEquals(-1,result);
    }

    @Test
    public void testExecutionWithAggMESLessThanExecutionQuantity() throws Exception {
        int result = MatchingUtil.getExecutionQuantity(50,100, 0,40);
        assertEquals(50,result);
    }

    @Test
    public void testExecutionCurrentMESGreatherThanAggMES() throws Exception {
        int result = MatchingUtil.getExecutionQuantity(50,100, 50,40);
        assertEquals(50,result);
    }

    @Test
    public void testExecutionAggMESGreatherThanCurrentMES() throws Exception {
        int result = MatchingUtil.getExecutionQuantity(50,100, 10,60);
        assertEquals(-1,result);
    }
}