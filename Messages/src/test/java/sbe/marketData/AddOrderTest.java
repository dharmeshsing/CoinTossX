package sbe.marketData;

import org.junit.Test;
import sbe.msg.marketData.*;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 15/03/02.
 */
public class AddOrderTest {

    /*@Test
    public void testEncodeAddOrderMessage() throws UnsupportedEncodingException {
        UnsafeBuffer data = encodeAddOrderMessage();
        assertNotNull(data);
    }*/

    @Test
    public void testDecodeAddOrderMessage() throws UnsupportedEncodingException {
        UnsafeBuffer decodeDirectBuffer = encodeAddOrderMessage();

        int bufferIndex = 0;
        AddOrderDecoder addOrderDecoder = new AddOrderDecoder();
        MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();

        messageHeaderDecoder.wrap(decodeDirectBuffer, bufferIndex);

        final int actingBlockLength = messageHeaderDecoder.blockLength();
        final int actingVersion = messageHeaderDecoder.version();

        bufferIndex += messageHeaderDecoder.encodedLength();
        addOrderDecoder.wrap(decodeDirectBuffer, bufferIndex, actingBlockLength, actingVersion);

        assertEquals(MessageTypeEnum.AddOrder,addOrderDecoder.messageType());
        assertEquals(10,addOrderDecoder.nanosecond());
        assertEquals(1,addOrderDecoder.orderId());
        assertEquals(SideEnum.Buy,addOrderDecoder.side());
        assertEquals(100,addOrderDecoder.quantity());
        assertEquals(10,addOrderDecoder.instrumentId());
        assertEquals(100,addOrderDecoder.price().mantissa());
        assertEquals("B",addOrderDecoder.flags().toString());

    }

    private UnsafeBuffer encodeAddOrderMessage() throws UnsupportedEncodingException {

        int bufferIndex = 0;
        AddOrderEncoder addOrderEncoder = new AddOrderEncoder();
        MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
        ByteBuffer buffer = ByteBuffer.allocateDirect(111);
        UnsafeBuffer encodeBuffer = new UnsafeBuffer(buffer);

        messageHeaderEncoder.wrap(encodeBuffer, bufferIndex)
                .blockLength(addOrderEncoder.sbeBlockLength())
                .templateId(addOrderEncoder.sbeTemplateId())
                .schemaId(addOrderEncoder.sbeSchemaId())
                .version(addOrderEncoder.sbeSchemaVersion());

        bufferIndex += messageHeaderEncoder.encodedLength();
        addOrderEncoder = addOrderEncoder.wrap(encodeBuffer, bufferIndex)
                .messageType(MessageTypeEnum.AddOrder)
                .nanosecond(10)
                .orderId(1)
                .side(SideEnum.Buy)
                .quantity(100)
                .instrumentId(10);

        PriceEncoder price = addOrderEncoder.price();
        price.mantissa(100);

        addOrderEncoder.flags(Flags.B);

        return encodeBuffer;

    }


}
