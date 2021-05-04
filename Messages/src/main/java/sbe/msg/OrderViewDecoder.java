/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.sbe.codec.java.CodecUtil;
import uk.co.real_logic.agrona.DirectBuffer;

@SuppressWarnings("all")
public class OrderViewDecoder
{
    public static final int BLOCK_LENGTH = 49;
    public static final int TEMPLATE_ID = 93;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final OrderViewDecoder parentMessage = this;
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

    public OrderViewDecoder wrap(
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

    public static int securityIdId()
    {
        return 1;
    }

    public static String securityIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static int securityIdNullValue()
    {
        return -2147483648;
    }

    public static int securityIdMinValue()
    {
        return -2147483647;
    }

    public static int securityIdMaxValue()
    {
        return 2147483647;
    }

    public int securityId()
    {
        return CodecUtil.int32Get(buffer, offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int clientOrderIdId()
    {
        return 2;
    }

    public static String clientOrderIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte clientOrderIdNullValue()
    {
        return (byte)0;
    }

    public static byte clientOrderIdMinValue()
    {
        return (byte)32;
    }

    public static byte clientOrderIdMaxValue()
    {
        return (byte)126;
    }

    public static int clientOrderIdLength()
    {
        return 20;
    }

    public byte clientOrderId(final int index)
    {
        if (index < 0 || index >= 20)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 4 + (index * 1));
    }


    public static String clientOrderIdCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getClientOrderId(final byte[] dst, final int dstOffset)
    {
        final int length = 20;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 4, dst, dstOffset, length);
        return length;
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

    public static int orderIdNullValue()
    {
        return -2147483648;
    }

    public static int orderIdMinValue()
    {
        return -2147483647;
    }

    public static int orderIdMaxValue()
    {
        return 2147483647;
    }

    public int orderId()
    {
        return CodecUtil.int32Get(buffer, offset + 24, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int submittedTimeId()
    {
        return 4;
    }

    public static String submittedTimeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long submittedTimeNullValue()
    {
        return 0xffffffffffffffffL;
    }

    public static long submittedTimeMinValue()
    {
        return 0x0L;
    }

    public static long submittedTimeMaxValue()
    {
        return 0xfffffffffffffffeL;
    }

    public long submittedTime()
    {
        return CodecUtil.uint64Get(buffer, offset + 28, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int priceId()
    {
        return 5;
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
        price.wrap(buffer, offset + 36);
        return price;
    }

    public static int orderQuantityId()
    {
        return 6;
    }

    public static String orderQuantityMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static int orderQuantityNullValue()
    {
        return -2147483648;
    }

    public static int orderQuantityMinValue()
    {
        return -2147483647;
    }

    public static int orderQuantityMaxValue()
    {
        return 2147483647;
    }

    public int orderQuantity()
    {
        return CodecUtil.int32Get(buffer, offset + 44, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int sideId()
    {
        return 7;
    }

    public static String sideMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public SideEnum side()
    {
        return SideEnum.get(CodecUtil.uint8Get(buffer, offset + 48));
    }

}
