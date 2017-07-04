package sbe.reader.marketData;

import org.junit.Test;
import sbe.builder.marketData.TradeBuilder;
import sbe.msg.marketData.MessageTypeEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 1/11/15.
 */
public class TradeReaderTest {
    @Test
    public void testRead() throws Exception {
        TradeReader tradeReader = new TradeReader();
        DirectBuffer buffer = build();

        StringBuilder sb = tradeReader.read(buffer);
        assertEquals("MessageType=TradeNanosecond=913353552ExecutedQuantity=1000InstrumentId=1TradeId=1001Price=1000",sb.toString());
    }


    private DirectBuffer build(){
        TradeBuilder tradeBuilder = new TradeBuilder();
        return tradeBuilder.messageType(MessageTypeEnum.Trade)
                .nanosecond(913353552)
                .executedQuantity(1000)
                .instrumentId(1)
                .tradeId(1001)
                .price(1000)
                .build();

    }

}