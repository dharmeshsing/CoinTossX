/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

import uk.co.real_logic.sbe.codec.java.*;
import uk.co.real_logic.agrona.DirectBuffer;

@SuppressWarnings("all")
public class OrderExecutedWithPriceSizeDecoder
{
    public static final int BLOCK_LENGTH = 38;
    public static final int TEMPLATE_ID = 23;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final OrderExecutedWithPriceSizeDecoder parentMessage = this;
    private DirectBuffer buffer;
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

    public OrderExecutedWithPriceSizeDecoder wrap(
        final DirectBuffer buffer, final int offset, final int actingBlockLength, final int actingVersion)
    {
        this.buffer = buffer;
        this.offset = offset;
        this.actingBlockLength = actingBlockLength;
        this.actingVersion = actingVersion;
        limit(offset + actingBlockLength);

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

    public static int messageTypeId()
    {
        return 1;
    }

    public static String messageTypeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public MessageTypeEnum messageType()
    {
        return MessageTypeEnum.get(CodecUtil.charGet(buffer, offset + 0));
    }


    public static int nanosecondId()
    {
        return 2;
    }

    public static String nanosecondMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public long nanosecond()
    {
        return CodecUtil.uint32Get(buffer, offset + 1, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int orderIdId()
    {
        return 3;
    }

    public static String orderIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public long orderId()
    {
        return CodecUtil.uint64Get(buffer, offset + 5, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int executedQuantityId()
    {
        return 4;
    }

    public static String executedQuantityMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public long executedQuantity()
    {
        return CodecUtil.uint32Get(buffer, offset + 13, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int displayQuantityId()
    {
        return 5;
    }

    public static String displayQuantityMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public long displayQuantity()
    {
        return CodecUtil.uint32Get(buffer, offset + 17, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int tradeIdId()
    {
        return 6;
    }

    public static String tradeIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public long tradeId()
    {
        return CodecUtil.uint32Get(buffer, offset + 21, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int printableId()
    {
        return 7;
    }

    public static String printableMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public PrintableEnum printable()
    {
        return PrintableEnum.get(CodecUtil.charGet(buffer, offset + 25));
    }


    public static int priceId()
    {
        return 8;
    }

    public static String priceMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    private final PriceDecoder price = new PriceDecoder();

    public PriceDecoder price()
    {
        price.wrap(buffer, offset + 26);
        return price;
    }

    public static int instrumentIdId()
    {
        return 6;
    }

    public static String instrumentIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public long instrumentId()
    {
        return CodecUtil.uint32Get(buffer, offset + 34, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

}
