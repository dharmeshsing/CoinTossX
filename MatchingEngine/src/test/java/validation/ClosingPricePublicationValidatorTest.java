package validation;

import common.TimeInForce;
import leafNode.OrderEntry;
import org.junit.Test;
import sbe.msg.NewOrderEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by dharmeshsing on 9/11/15.
 */
public class ClosingPricePublicationValidatorTest {

    @Test
    public void testLimitOrderWithATCRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getTimeInForce()).thenReturn(TimeInForce.ATC.getValue());

        SessionValidator closingPricePublicationValidator = new ClosingPricePublicationValidator();
        boolean result = closingPricePublicationValidator.isMessageValidForSession(orderEntry, NewOrderEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

    @Test
    public void testLimitOrderWithOPGRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getTimeInForce()).thenReturn(TimeInForce.OPG.getValue());

        SessionValidator closingPricePublicationValidator = new ClosingPricePublicationValidator();
        boolean result = closingPricePublicationValidator.isMessageValidForSession(orderEntry, NewOrderEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

    @Test
    public void testLimitOrderWithGFARejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getTimeInForce()).thenReturn(TimeInForce.GFA.getValue());

        SessionValidator closingPricePublicationValidator = new ClosingPricePublicationValidator();
        boolean result = closingPricePublicationValidator.isMessageValidForSession(orderEntry, NewOrderEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

    @Test
    public void testLimitOrderWithGFXRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getTimeInForce()).thenReturn(TimeInForce.GFX.getValue());

        SessionValidator closingPricePublicationValidator = new ClosingPricePublicationValidator();
        boolean result = closingPricePublicationValidator.isMessageValidForSession(orderEntry, NewOrderEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

    @Test
    public void testLimitOrderWithIOCRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getTimeInForce()).thenReturn(TimeInForce.IOC.getValue());

        SessionValidator closingPricePublicationValidator = new ClosingPricePublicationValidator();
        boolean result = closingPricePublicationValidator.isMessageValidForSession(orderEntry, NewOrderEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

    @Test
    public void testLimitOrderWithFOKRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        when(orderEntry.getTimeInForce()).thenReturn(TimeInForce.FOK.getValue());

        SessionValidator closingPricePublicationValidator = new ClosingPricePublicationValidator();
        boolean result = closingPricePublicationValidator.isMessageValidForSession(orderEntry, NewOrderEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

}