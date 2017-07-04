package sbe.reader.marketData;

import org.junit.Test;
import sbe.builder.marketData.OrderExecutedBuilder;
import sbe.msg.marketData.MessageTypeEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 1/11/15.
 */
public class OrderExecutedReaderTest {
    @Test
    public void testRead() throws Exception {
        OrderExecutedReader orderExecutedReader = new OrderExecutedReader();
        DirectBuffer buffer = build();

        StringBuilder sb = orderExecutedReader.read(buffer);
        assertEquals("MessageType=OrderExecutedNanosecond=913353552OrderId=1ExecutedQuantity=1000TradeId=1001",sb.toString());
    }

    private DirectBuffer build(){
        OrderExecutedBuilder orderExecutedBuilder = new OrderExecutedBuilder();
        return orderExecutedBuilder.messageType(MessageTypeEnum.OrderExecuted)
                .nanosecond(913353552)
                .orderId(1)
                .executedQuantity(1000)
                .tradeId(1001)
                .build();

    }

}