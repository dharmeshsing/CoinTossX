/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.sbe.codec.java.CodecUtil;

@SuppressWarnings("all")
public class SymbolStatusDecoder
{
    public static final int BLOCK_LENGTH = 15;
    public static final int TEMPLATE_ID = 27;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final SymbolStatusDecoder parentMessage = this;
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

    public SymbolStatusDecoder wrap(
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


    public static int instrumentIdId()
    {
        return 3;
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
        return CodecUtil.uint32Get(buffer, offset + 5, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int tradingSessionId()
    {
        return 4;
    }

    public static String tradingSessionMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public TradingSessionEnum tradingSession()
    {
        return TradingSessionEnum.get(CodecUtil.uint8Get(buffer, offset + 9));
    }


    public static int haltReasonId()
    {
        return 5;
    }

    public static String haltReasonMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public HaltReasonEnum haltReason()
    {
        return HaltReasonEnum.get(CodecUtil.int32Get(buffer, offset + 10, java.nio.ByteOrder.LITTLE_ENDIAN));
    }


    public static int sessionChangedReasonId()
    {
        return 6;
    }

    public static String sessionChangedReasonMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public SessionChangedReasonEnum sessionChangedReason()
    {
        return SessionChangedReasonEnum.get(CodecUtil.uint8Get(buffer, offset + 14));
    }

}
