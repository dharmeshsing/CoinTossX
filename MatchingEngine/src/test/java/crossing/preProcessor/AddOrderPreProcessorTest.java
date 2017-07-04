package crossing.preProcessor;

import common.OrderType;
import crossing.preProcessor.MatchingPreProcessor.MATCHING_ACTION;
import orderBook.OrderBook;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 9/07/15.
 */
public class AddOrderPreProcessorTest {

    private AddOrderPreProcessor addOrderPreProcessor;

    @Before
    public void setup(){
        addOrderPreProcessor = new AddOrderPreProcessor();
    }

    @Test
    public void testAcceptMarketOrder() throws Exception {
        MATCHING_ACTION result = addOrderPreProcessor.preProcess(OrderType.MARKET, OrderBook.SIDE.BID, 10L, 10L,100L, 50L,90L,40L, 100L);
        assertEquals(MATCHING_ACTION.AGGRESS_ORDER, result);
    }

    @Test
    public void testAcceptEmptyOfferTree() throws Exception {
        MATCHING_ACTION result = addOrderPreProcessor.preProcess(OrderType.LIMIT, OrderBook.SIDE.BID, 0L, 10L, 100L, 50L,90L,40L, 100L);
        assertEquals(MATCHING_ACTION.ADD_ORDER, result);
    }

    @Test
    public void testAcceptEmptyBidTree() throws Exception {
        MATCHING_ACTION result = addOrderPreProcessor.preProcess(OrderType.LIMIT, OrderBook.SIDE.OFFER, 10L, 0L, 100L, 50L,90L,40L, 100L);
        assertEquals(MATCHING_ACTION.ADD_ORDER, result);
    }

    @Test
    public void testAcceptBidPriceLessBestBid() throws Exception {
        MATCHING_ACTION result = addOrderPreProcessor.preProcess(OrderType.LIMIT, OrderBook.SIDE.BID, 10L, 10L, 100L, 50L,90L,40L, 80L);
        assertEquals(MATCHING_ACTION.ADD_ORDER, result);
    }

    @Test
    public void testAcceptOfferPriceGreaterBestOffer() throws Exception {
        MATCHING_ACTION result = addOrderPreProcessor.preProcess(OrderType.LIMIT, OrderBook.SIDE.OFFER, 10L, 10L, 100L, 50L,90L,40L, 60L);
        assertEquals(MATCHING_ACTION.ADD_ORDER, result);
    }


}