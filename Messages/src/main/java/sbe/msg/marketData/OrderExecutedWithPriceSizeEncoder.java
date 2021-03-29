/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

import uk.co.real_logic.sbe.codec.java.CodecUtil;
import uk.co.real_logic.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public class OrderExecutedWithPriceSizeEncoder
{
    public static final int BLOCK_LENGTH = 46;
    public static final int TEMPLATE_ID = 23;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final OrderExecutedWithPriceSizeEncoder parentMessage = this;
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

    public OrderExecutedWithPriceSizeEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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
    public OrderExecutedWithPriceSizeEncoder messageType(final MessageTypeEnum value)
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
    public OrderExecutedWithPriceSizeEncoder nanosecond(final long value)
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
    public OrderExecutedWithPriceSizeEncoder orderId(final long value)
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
    public OrderExecutedWithPriceSizeEncoder executedQuantity(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 13, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static long displayQuantityNullValue()
    {
        return 4294967294L;
    }

    public static long displayQuantityMinValue()
    {
        return 0L;
    }

    public static long displayQuantityMaxValue()
    {
        return 4294967293L;
    }
    public OrderExecutedWithPriceSizeEncoder displayQuantity(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 17, value, java.nio.ByteOrder.LITTLE_ENDIAN);
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
    public OrderExecutedWithPriceSizeEncoder tradeId(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 21, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
    public OrderExecutedWithPriceSizeEncoder printable(final PrintableEnum value)
    {
        CodecUtil.charPut(buffer, offset + 25, value.value());
        return this;
    }

    private final PriceEncoder price = new PriceEncoder();

    public PriceEncoder price()
    {
        price.wrap(buffer, offset + 26);
        return price;
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
    public OrderExecutedWithPriceSizeEncoder instrumentId(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 34, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static long clientOrderIdNullValue()
    {
        return 0xffffffffffffffffL;
    }

    public static long clientOrderIdMinValue()
    {
        return 0x0L;
    }

    public static long clientOrderIdMaxValue()
    {
        return 0xfffffffffffffffeL;
    }
    public OrderExecutedWithPriceSizeEncoder clientOrderId(final long value)
    {
        CodecUtil.uint64Put(buffer, offset + 38, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
