package sbe.reader.marketData;

import org.junit.Test;
import sbe.builder.marketData.OrderExecutedWithPriceSizeBuilder;
import sbe.msg.marketData.MessageTypeEnum;
import sbe.msg.marketData.PrintableEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 1/11/15.
 */
public class OrderExecutedWithPriceSizeReaderTest {
    public static DateTimeFormatter creationTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Test
    public void testRead() throws Exception {
        OrderExecutedWithPriceSizeReader orderExecutedWithPriceSizeReader = new OrderExecutedWithPriceSizeReader();
        DirectBuffer buffer = build();

        orderExecutedWithPriceSizeReader.readBuffer(buffer);
        assertEquals(1,orderExecutedWithPriceSizeReader.getInstrumentId());
        assertEquals(1001,orderExecutedWithPriceSizeReader.getTradeId());
        assertEquals(1,orderExecutedWithPriceSizeReader.getClientOrderId());
        assertEquals(1000,orderExecutedWithPriceSizeReader.getPrice());
        assertEquals(1000,orderExecutedWithPriceSizeReader.getExecutedQuantity());
        System.out.println(creationTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(orderExecutedWithPriceSizeReader.getExecutedTime()), ZoneId.systemDefault())));
        //assertEquals("MessageType=OrderExecutedPriceSizeNanosecond=913353552OrderId=1ExecutedQuantity=1000DisplayQuantity=100TradeId=1001Printable=PrintablePrice=1000InstrumentId=1",sb.toString());
    }


    private DirectBuffer build(){
        OrderExecutedWithPriceSizeBuilder orderExecutedWithPriceSizeBuilder = new OrderExecutedWithPriceSizeBuilder();
        return orderExecutedWithPriceSizeBuilder.messageType(MessageTypeEnum.OrderExecutedPriceSize)
                .nanosecond(913353552)
                .orderId(1)
                .clientOrderId(1)
                .executedQuantity(1000)
                .displayQuantity(100)
                .tradeId(1001)
                .printable(PrintableEnum.Printable)
                .price(1000)
                .instrumentId(1)
                .executedTime(java.time.Instant.now().toEpochMilli())
                .build();

    }

}