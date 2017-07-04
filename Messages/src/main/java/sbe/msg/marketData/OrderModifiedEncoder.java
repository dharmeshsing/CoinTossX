/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

import uk.co.real_logic.agrona.MutableDirectBuffer;
import uk.co.real_logic.sbe.codec.java.CodecUtil;

@SuppressWarnings("all")
public class OrderModifiedEncoder
{
    public static final int BLOCK_LENGTH = 26;
    public static final int TEMPLATE_ID = 21;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final OrderModifiedEncoder parentMessage = this;
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

    public OrderModifiedEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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
    public OrderModifiedEncoder messageType(final MessageTypeEnum value)
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
    public OrderModifiedEncoder nanosecond(final long value)
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
    public OrderModifiedEncoder orderId(final long value)
    {
        CodecUtil.uint64Put(buffer, offset + 5, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static long newQuantityNullValue()
    {
        return 4294967294L;
    }

    public static long newQuantityMinValue()
    {
        return 0L;
    }

    public static long newQuantityMaxValue()
    {
        return 4294967293L;
    }
    public OrderModifiedEncoder newQuantity(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 13, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    private final PriceEncoder newPrice = new PriceEncoder();

    public PriceEncoder newPrice()
    {
        newPrice.wrap(buffer, offset + 17);
        return newPrice;
    }
    public OrderModifiedEncoder flags(final Flags value)
    {
        CodecUtil.uint8Put(buffer, offset + 25, value.value());
        return this;
    }
}
