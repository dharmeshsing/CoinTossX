package sbe.reader;

import org.junit.Test;
import sbe.builder.BuilderUtil;
import sbe.builder.OrderCancelRequestBuilder;
import sbe.msg.OrderBookEnum;
import sbe.msg.OrderCancelRequestEncoder;
import sbe.msg.SideEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 21/08/15.
 */
public class OrderCancelRequestReaderTest {

    @Test
    public void testRead() throws Exception {
        OrderCancelRequestReader orderCancelRequestReader = new OrderCancelRequestReader();
        DirectBuffer buffer = build();

        StringBuilder sb = orderCancelRequestReader.read(buffer);
        assertEquals("ClientOrderId=2                   " +
                     "OrigClientOrderId=1                   " +
                     "OrderId=0SecurityId=1TraderMnemonic=test             " +
                     "Side=BuyOrderBook=Regular",sb.toString());
    }

    private DirectBuffer build(){
        OrderCancelRequestBuilder orderCancelRequestBuilder = new OrderCancelRequestBuilder();
        orderCancelRequestBuilder.compID(1);

        String clientOrderId = BuilderUtil.fill("2",OrderCancelRequestEncoder.clientOrderIdLength());
        orderCancelRequestBuilder.clientOrderId(clientOrderId.getBytes());

        String origClientOrderId = BuilderUtil.fill("1",OrderCancelRequestEncoder.origClientOrderIdLength());
        orderCancelRequestBuilder.origClientOrderId(origClientOrderId.getBytes());
        orderCancelRequestBuilder.securityId(1);

        String trader = BuilderUtil.fill("test", OrderCancelRequestEncoder.traderMnemonicLength());

        orderCancelRequestBuilder.traderMnemonic(trader.getBytes())
                                 .side(SideEnum.Buy)
                                 .orderBook(OrderBookEnum.Regular);

        return orderCancelRequestBuilder.build();

    }

}