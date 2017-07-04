/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

import uk.co.real_logic.agrona.MutableDirectBuffer;
import uk.co.real_logic.sbe.codec.java.CodecUtil;

@SuppressWarnings("all")
public class OrderExecutedEncoder
{
    public static final int BLOCK_LENGTH = 21;
    public static final int TEMPLATE_ID = 22;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final OrderExecutedEncoder parentMessage = this;
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

    public OrderExecutedEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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
    public OrderExecutedEncoder messageType(final MessageTypeEnum value)
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
    public OrderExecutedEncoder nanosecond(final long value)
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
    public OrderExecutedEncoder orderId(final long value)
    {
        CodecUtil.uint64Put(buffer, offset + 5, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static long executedQuantityNullValue()
    {
        return 4294967294L;
    }

    public static long executedQuantityMinValue()
    {
        return 0L;
    }

    public static long executedQuantityMaxValue()
    {
        return 4294967293L;
    }
    public OrderExecutedEncoder executedQuantity(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 13, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static long tradeIdNullValue()
    {
        return 4294967294L;
    }

    public static long tradeIdMinValue()
    {
        return 0L;
    }

    public static long tradeIdMaxValue()
    {
        return 4294967293L;
    }
    public OrderExecutedEncoder tradeId(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 17, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
