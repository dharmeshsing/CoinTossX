package crossing.expireRule;

import common.OrderType;
import common.TimeInForce;
import leafNode.OrderEntry;
import org.joda.time.Instant;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dharmeshsing on 30/11/15.
 */
public class ClosingAuctionExpireRuleTest {

    @Test
    public void testIsMarketOrderExpired(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getType()).thenReturn(OrderType.MARKET.getOrderType());

        ExpireRule expireRule = new ClosingAuctionExpireRule();

        boolean result = expireRule.isOrderExpired(orderEntry);
        assertEquals(true,result);
    }

    @Test
    public void testIsGTTOrderExpiredNotPastExpiryDate(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getTimeInForce()).thenReturn(TimeInForce.GTT.getValue());
        when(orderEntry.getExpireTime()).thenReturn(Instant.now().plus(1000).getMillis());

        ExpireRule expireRule = new ClosingAuctionExpireRule();

        boolean result = expireRule.isOrderExpired(orderEntry);
        assertEquals(false,result);
    }

    @Test
    public void testIsGTTOrderExpiredPastExpiryDate(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getTimeInForce()).thenReturn(TimeInForce.GTT.getValue());
        when(orderEntry.getExpireTime()).thenReturn(Instant.now().minus(1000).getMillis());

        ExpireRule expireRule = new ClosingAuctionExpireRule();

        boolean result = expireRule.isOrderExpired(orderEntry);
        assertEquals(true,result);
    }

    @Test
    public void testIsATC(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getTimeInForce()).thenReturn(TimeInForce.ATC.getValue());

        ExpireRule expireRule = new ClosingAuctionExpireRule();

        boolean result = expireRule.isOrderExpired(orderEntry);
        assertEquals(true,result);
    }


}