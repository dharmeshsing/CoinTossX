package sbe.builder;

import org.junit.Assert;
import org.junit.Test;
import sbe.msg.*;
import uk.co.real_logic.agrona.DirectBuffer;

/**
 * Created by dharmeshsing on 18/02/17.
 */
public class NewOrderBuilderTest {
    private NewOrderBuilder newOrderBuilder = new NewOrderBuilder();

    @Test
    public void testNewOrder(){
        LogonBuilder logonBuilder = new LogonBuilder();
        DirectBuffer buffer = createNewOrder(1200, 2500, SideEnum.Buy, OrdTypeEnum.Limit);
        Assert.assertNotNull(buffer);
    }

    public DirectBuffer createNewOrder(long volume, long price,SideEnum side,OrdTypeEnum orderType){
        String clientOrderId = "1234";

        DirectBuffer directBuffer = newOrderBuilder.compID(1)
                .clientOrderId(clientOrderId)
                .account("account123".getBytes())
                .capacity(CapacityEnum.Agency)
                .cancelOnDisconnect(CancelOnDisconnectEnum.DoNotCancel)
                .orderBook(OrderBookEnum.Regular)
                .securityId(1)
                .traderMnemonic("John")
                .orderType(orderType)
                .timeInForce(TimeInForceEnum.Day)
                .expireTime("20150813-23:00:00".getBytes())
                .side(side)
                .orderQuantity((int) volume)
                .displayQuantity((int) volume)
                .minQuantity(0)
                .limitPrice(price)
                .stopPrice(0)
                .build();

        return directBuffer;
    }

}