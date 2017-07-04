package sbe.reader.marketData;

import org.junit.Test;
import sbe.builder.marketData.OrderModifiedBuilder;
import sbe.msg.marketData.Flags;
import sbe.msg.marketData.MessageTypeEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 1/11/15.
 */
public class OrderModifiedReaderTest {
    @Test
    public void testRead() throws Exception {
        OrderModifiedReader orderModifiedReader = new OrderModifiedReader();
        DirectBuffer buffer = build();

        StringBuilder sb = orderModifiedReader.read(buffer);
        assertEquals("MessageType=OrderModifiedNanosecond=913353552OrderId=1NewQuantity=100NewPrice=2000Flags=C",sb.toString());
    }

    private DirectBuffer build(){
        OrderModifiedBuilder orderModifiedBuilder = new OrderModifiedBuilder();
        return orderModifiedBuilder.messageType(MessageTypeEnum.OrderModified)
                .nanosecond(913353552)
                .orderId(1)
                .newQuantity(100)
                .newPrice(2000)
                .flags(Flags.C)
                .build();

    }

}