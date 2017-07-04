package sbe.reader.marketData;

import org.junit.Test;
import sbe.builder.marketData.AddOrderBuilder;
import sbe.msg.marketData.Flags;
import sbe.msg.marketData.MessageTypeEnum;
import sbe.msg.marketData.SideEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;


/**
 * Created by dharmeshsing on 16/08/15.
 */
public class AddOrderReaderTest {

    @Test
    public void testRead() throws Exception {
        AddOrderReader addOrderReader = new AddOrderReader();
        DirectBuffer buffer = build();

        StringBuilder sb = addOrderReader.read(buffer);
        assertEquals("MessageType=AddOrderNanosecond=913353552OrderId=1Side=BuyQuantity=100InstrumentId=1Price=1000Flags=B",sb.toString());

    }


    private DirectBuffer build(){
        AddOrderBuilder addOrderBuilder = new AddOrderBuilder();
        return addOrderBuilder.messageType(MessageTypeEnum.AddOrder)
                .nanosecond(913353552)
                .orderId(1)
                .side(SideEnum.Buy)
                .quantity(100)
                .instrumentId(1)
                .price(1000)
                .isMarketOrder(Flags.B)
                .build();

    }
}