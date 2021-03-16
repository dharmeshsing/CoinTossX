/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

import uk.co.real_logic.sbe.codec.java.CodecUtil;
import uk.co.real_logic.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public class BestBidOfferEncoder
{
    public static final int BLOCK_LENGTH = 29;
    public static final int TEMPLATE_ID = 26;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final BestBidOfferEncoder parentMessage = this;
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

    public BestBidOfferEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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
    public BestBidOfferEncoder messageType(final MessageTypeEnum value)
    {
        CodecUtil.charPut(buffer, offset + 0, value.value());
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
    public BestBidOfferEncoder instrumentId(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 1, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    private final PriceEncoder bid = new PriceEncoder();

    public PriceEncoder bid()
    {
        bid.wrap(buffer, offset + 5);
        return bid;
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
    public BestBidOfferEncoder bidQuantity(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 13, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    private final PriceEncoder offer = new PriceEncoder();

    public PriceEncoder offer()
    {
        offer.wrap(buffer, offset + 17);
        return offer;
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
    public BestBidOfferEncoder offerQuantity(final long value)
    {
        CodecUtil.uint32Put(buffer, offset + 25, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
