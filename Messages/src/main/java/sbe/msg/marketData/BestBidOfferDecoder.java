/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.sbe.codec.java.CodecUtil;

@SuppressWarnings("all")
public class BestBidOfferDecoder
{
    public static final int BLOCK_LENGTH = 29;
    public static final int TEMPLATE_ID = 26;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final BestBidOfferDecoder parentMessage = this;
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

    public BestBidOfferDecoder wrap(
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


    public static int instrumentIdId()
    {
        return 2;
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
        return CodecUtil.uint32Get(buffer, offset + 1, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int bidId()
    {
        return 3;
    }

    public static String bidMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    private final PriceDecoder bid = new PriceDecoder();

    public PriceDecoder bid()
    {
        bid.wrap(buffer, offset + 5);
        return bid;
    }

    public static int bidQuantityId()
    {
        return 4;
    }

    public static String bidQuantityMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long bidQuantityNullValue()
    {
        return 4294967294L;
    }

    public static long bidQuantityMinValue()
    {
        return 0L;
    }

    public static long bidQuantityMaxValue()
    {
        return 4294967293L;
    }

    public long bidQuantity()
    {
        return CodecUtil.uint32Get(buffer, offset + 13, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int offerId()
    {
        return 5;
    }

    public static String offerMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    private final PriceDecoder offer = new PriceDecoder();

    public PriceDecoder offer()
    {
        offer.wrap(buffer, offset + 17);
        return offer;
    }

    public static int offerQuantityId()
    {
        return 6;
    }

    public static String offerQuantityMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static long offerQuantityNullValue()
    {
        return 4294967294L;
    }

    public static long offerQuantityMinValue()
    {
        return 0L;
    }

    public static long offerQuantityMaxValue()
    {
        return 4294967293L;
    }

    public long offerQuantity()
    {
        return CodecUtil.uint32Get(buffer, offset + 25, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

}
