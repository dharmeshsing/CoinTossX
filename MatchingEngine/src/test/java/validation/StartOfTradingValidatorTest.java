package validation;

import leafNode.OrderEntry;
import org.junit.Test;
import sbe.msg.NewOrderEncoder;
import sbe.msg.OrderCancelReplaceRequestEncoder;
import sbe.msg.OrderCancelRequestEncoder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by dharmeshsing on 5/11/15.
 */
public class StartOfTradingValidatorTest {

    @Test
    public void testNewOrderRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        StartOfTradingValidator startOfTradingValidator = new StartOfTradingValidator();
        boolean result = startOfTradingValidator.isMessageValidForSession(orderEntry, NewOrderEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

    @Test
    public void testAmendRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        StartOfTradingValidator startOfTradingValidator = new StartOfTradingValidator();
        boolean result = startOfTradingValidator.isMessageValidForSession(orderEntry, OrderCancelReplaceRequestEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

    @Test
    public void testCancelOrderRejected(){
        OrderEntry orderEntry = mock(OrderEntry.class);
        StartOfTradingValidator startOfTradingValidator = new StartOfTradingValidator();
        boolean result = startOfTradingValidator.isMessageValidForSession(orderEntry, OrderCancelRequestEncoder.TEMPLATE_ID);

        assertEquals(false,result);
    }

}