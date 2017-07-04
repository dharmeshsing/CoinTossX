package crossing.preProcessor;

import crossing.preProcessor.MatchingPreProcessor.MATCHING_ACTION;
import leafNode.OrderEntry;
import orderBook.OrderBook;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dharmeshsing on 11/07/15.
 */
public class MarketOrderPreProcessorTest {

    private MarketOrderPreProcessor marketOrderPreProcessor;

    @Before
    public void setup(){
        marketOrderPreProcessor = new MarketOrderPreProcessor();
    }


    @Test
    public void testBidPreProcess() throws Exception {
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getPrice()).thenReturn(100L);

        MATCHING_ACTION result = marketOrderPreProcessor.preProcess(OrderBook.SIDE.BID, orderEntry, 0, 100, 0, 100);
        assertEquals(MATCHING_ACTION.AGGRESS_ORDER,result);
    }

    @Test
    public void testOfferPreProcess() throws Exception {
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getPrice()).thenReturn(100L);

        MATCHING_ACTION result = marketOrderPreProcessor.preProcess(OrderBook.SIDE.OFFER, orderEntry, 100, 0, 100, 0);
        assertEquals(MATCHING_ACTION.AGGRESS_ORDER,result);
    }

    @Test
    public void testNoPriceSet() throws Exception {
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getPrice()).thenReturn(0L);

        MATCHING_ACTION result = marketOrderPreProcessor.preProcess(OrderBook.SIDE.BID, orderEntry, 0, 0, 0, 0);
        assertEquals(MATCHING_ACTION.NO_ACTION,result);
    }
}