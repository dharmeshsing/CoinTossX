/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

import uk.co.real_logic.agrona.MutableDirectBuffer;
import uk.co.real_logic.sbe.codec.java.CodecUtil;

@SuppressWarnings("all")
public class AddOrderEncoder
{
    public static final int BLOCK_LENGTH = 31;
    public static final int TEMPLATE_ID = 19;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final AddOrderEncoder parentMessage = this;
    private MutableDirectBuffer buffer;
    protected int offset;
    protected int limit;
    protected int actingBlockLength;
    protected int actingVersion;

    public int sbeBlockLength()
    {
        return BLOCK_LENGTH;
    }

    public int sbeTemplateId()
    {
        return TEMPLATE_ID;
    }

    public int sbeSchemaId()
    {
        return SCHEMA_ID;
    }

    public int sbeSchemaVersion()
    {
        return SCHEMA_VERSION;
    }

    public String sbeSemanticType()
    {
        return "";
    }

    public int offset()
    {
        return offset;
    }

    public AddOrderEncoder wrap(final MutableDirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;
        limit(offset + BLOCK_LENGTH);
        return this;
    }

    public int encodedLength()
    {
        return limit - offset;
    }

    public int limit()
    {
        return limit;
    }

    public void limit(final int limit)
    {
        buffer.checkLimit(limit);
        this.limit = limit;
    }
    public AddOrderEncoder messageType(final MessageTypeEnum value)
    {
        CodecUtil.charPut(buffer, offset + 0, value.value());
        return this;
    }

    public static long nanosecondNullValue()
    {
        return 4294967294L;
    }

    public static long nanosecondMinValue()
    {
        return 0L;
    }

    public static long nanosecondMaxValue()
    {
        return 4294967293L;
    }
    public AddOrderEncoder nanosecond(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 1, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static long orderIdNullValue()
    {
        return 0xffffffffffffffffL;
    }

    public static long orderIdMinValue()
    {
        return 0x0L;
    }

    public static long orderIdMaxValue()
    {
        return 0xfffffffffffffffeL;
    }
    public AddOrderEncoder orderId(final long value)
    {
        CodecUtil.uint64Put(buffer, offset + 5, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
    public AddOrderEncoder side(final SideEnum value)
    {
        CodecUtil.charPut(buffer, offset + 13, value.value());
        return this;
    }

    public static long quantityNullValue()
    {
        return 4294967294L;
    }

    public static long quantityMinValue()
    {
        return 0L;
    }

    public static long quantityMaxValue()
    {
        return 4294967293L;
    }
    public AddOrderEncoder quantity(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 14, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static long instrumentIdNullValue()
    {
        return 4294967294L;
    }

    public static long instrumentIdMinValue()
    {
        return 0L;
    }

    public static long instrumentIdMaxValue()
    {
        return 4294967293L;
    }
    public AddOrderEncoder instrumentId(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 18, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    private final PriceEncoder price = new PriceEncoder();

    public PriceEncoder price()
    {
        price.wrap(buffer, offset + 22);
        return price;
    }
    public AddOrderEncoder flags(final Flags value)
    {
        CodecUtil.uint8Put(buffer, offset + 30, value.value());
        return this;
    }
}
