package crossing.expireRule;

import common.OrderType;
import leafNode.OrderEntry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dharmeshsing on 1/12/15.
 */
public class MarketOrderExpireRuleTest {

    @Test
    public void testIsMarketOrderExpired(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getType()).thenReturn(OrderType.MARKET.getOrderType());

        ExpireRule expireRule = new MarketOrderExpireRule();

        boolean result = expireRule.isOrderExpired(orderEntry);
        assertEquals(true,result);
    }
}