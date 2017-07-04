package crossing.preProcessor;

import common.OrderType;
import crossing.TestOrderEntryFactory;
import crossing.preProcessor.MatchingPreProcessor.MATCHING_ACTION;
import leafNode.OrderEntry;
import orderBook.OrderBook;
import org.junit.Before;
import org.junit.Test;
import unsafe.UnsafeUtil;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 17/07/15.
 */
public class StopOrderPreProcessorTest {

    private StopOrderPreProcessor stopOrderPreProcessor;

    @Before
    public void setup(){
        stopOrderPreProcessor = new StopOrderPreProcessor();
    }

    @Test
    public void testAggressOrder() throws Exception {
        OrderEntry orderEntry = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry.setType(OrderType.STOP.getOrderType());
        orderEntry.setStopPrice(200L);

        MATCHING_ACTION result = stopOrderPreProcessor.preProcess(300L, OrderBook.SIDE.BID, orderEntry);
        assertEquals(MATCHING_ACTION.AGGRESS_ORDER,result);

        UnsafeUtil.freeOrderEntryMemory(orderEntry);
    }

    @Test
    public void testParkOrder() throws Exception {
        OrderEntry orderEntry = TestOrderEntryFactory.createOrderEntry("10:00");
        orderEntry.setType(OrderType.STOP.getOrderType());
        orderEntry.setStopPrice(200L);

        MATCHING_ACTION result = stopOrderPreProcessor.preProcess(0L, OrderBook.SIDE.BID, orderEntry);
        assertEquals(MATCHING_ACTION.PARK_ORDER,result);

        UnsafeUtil.freeOrderEntryMemory(orderEntry);
    }
}