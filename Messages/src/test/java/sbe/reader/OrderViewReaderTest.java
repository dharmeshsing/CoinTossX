package sbe.reader;

import org.junit.Test;
import sbe.builder.OrderViewBuilder;
import sbe.msg.SideEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 27/04/16.
 */
public class OrderViewReaderTest {
    @Test
    public void testRead() throws Exception {
        OrderViewReader orderViewReader = new OrderViewReader();
        DirectBuffer buffer = build();

        orderViewReader.read(buffer);
        assertEquals(2,orderViewReader.getSecurityId());
        assertEquals(3,orderViewReader.getOrderId());
        assertEquals(1000,orderViewReader.getOrderQuantity());
        assertEquals(100,orderViewReader.getPrice());
        assertEquals(SideEnum.Buy,orderViewReader.getSide());


    }

    private DirectBuffer build(){
        OrderViewBuilder orderViewBuilder = new OrderViewBuilder();
        return orderViewBuilder.compID(1)
                .securityId(2)
                .orderId(3)
                .submittedTime(Instant.now().toEpochMilli())
                .orderQuantity(1000)
                .price(100)
                .side(SideEnum.Buy)
                .build();
    }

}