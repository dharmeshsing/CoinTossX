package validation;

import common.OrderType;
import common.TimeInForce;
import leafNode.OrderEntry;
import org.junit.Test;
import sbe.msg.NewOrderEncoder;
import sbe.msg.OrderCancelReplaceRequestEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dharmeshsing on 9/11/15.
 */
public class IntraDayAuctionCallValidatorTest {
    
    @Test
    public void testOPGOrderRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getType()).thenReturn(OrderType.LIMIT.getOrderType());
        when(orderEntry.getTimeInForce()).thenReturn(TimeInForce.OPG.getValue());

        SessionValidator intraDayAuctionCallValidator = new IntraDayAuctionCallValidator();
        boolean result = intraDayAuctionCallValidator.isMessageValidForSession(orderEntry, NewOrderEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

    @Test
    public void testNewHiddenOrderRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getType()).thenReturn(OrderType.HIDDEN_LIMIT.getOrderType());

        SessionValidator intraDayAuctionCallValidator = new IntraDayAuctionCallValidator();
        boolean result = intraDayAuctionCallValidator.isMessageValidForSession(orderEntry, NewOrderEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

    @Test
    public void testAmendHiddenOrderRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getType()).thenReturn(OrderType.HIDDEN_LIMIT.getOrderType());

        SessionValidator intraDayAuctionCallValidator = new IntraDayAuctionCallValidator();
        boolean result = intraDayAuctionCallValidator.isMessageValidForSession(orderEntry, OrderCancelReplaceRequestEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

    @Test
    public void testLimitOrderWithIOCRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getType()).thenReturn(OrderType.LIMIT.getOrderType());
        when(orderEntry.getTimeInForce()).thenReturn(TimeInForce.IOC.getValue());

        SessionValidator intraDayAuctionCallValidator = new IntraDayAuctionCallValidator();
        boolean result = intraDayAuctionCallValidator.isMessageValidForSession(orderEntry, NewOrderEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

    @Test
    public void testLimitOrderWithFOKRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getType()).thenReturn(OrderType.LIMIT.getOrderType());
        when(orderEntry.getTimeInForce()).thenReturn(TimeInForce.FOK.getValue());

        SessionValidator intraDayAuctionCallValidator = new IntraDayAuctionCallValidator();
        boolean result = intraDayAuctionCallValidator.isMessageValidForSession(orderEntry, NewOrderEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

    @Test
    public void testMarketOrderWithIOCRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getType()).thenReturn(OrderType.MARKET.getOrderType());
        when(orderEntry.getTimeInForce()).thenReturn(TimeInForce.IOC.getValue());

        SessionValidator intraDayAuctionCallValidator = new IntraDayAuctionCallValidator();
        boolean result = intraDayAuctionCallValidator.isMessageValidForSession(orderEntry, NewOrderEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

    @Test
    public void testMarketOrderWithFOKRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getType()).thenReturn(OrderType.MARKET.getOrderType());
        when(orderEntry.getTimeInForce()).thenReturn(TimeInForce.FOK.getValue());

        SessionValidator intraDayAuctionCallValidator = new IntraDayAuctionCallValidator();
        boolean result = intraDayAuctionCallValidator.isMessageValidForSession(orderEntry, NewOrderEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

}