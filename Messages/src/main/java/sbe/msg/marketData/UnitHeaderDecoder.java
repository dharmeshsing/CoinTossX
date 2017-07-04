/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.sbe.codec.java.CodecUtil;

@SuppressWarnings("all")
public class UnitHeaderDecoder
{
    public static final int BLOCK_LENGTH = 5;
    public static final int TEMPLATE_ID = 18;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final UnitHeaderDecoder parentMessage = this;
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

    public UnitHeaderDecoder wrap(
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

    public static int messageCountId()
    {
        return 1;
    }

    public static String messageCountMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static int messageCountNullValue()
    {
        return 65535;
    }

    public static int messageCountMinValue()
    {
        return 0;
    }

    public static int messageCountMaxValue()
    {
        return 65534;
    }

    public int messageCount()
    {
        return CodecUtil.uint16Get(buffer, offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int marketDataGroupId()
    {
        return 2;
    }

    public static String marketDataGroupMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte marketDataGroupNullValue()
    {
        return (byte)-128;
    }

    public static byte marketDataGroupMinValue()
    {
        return (byte)-127;
    }

    public static byte marketDataGroupMaxValue()
    {
        return (byte)127;
    }

    public byte marketDataGroup()
    {
        return CodecUtil.int8Get(buffer, offset + 2);
    }


    public static int sequenceNumberId()
    {
        return 3;
    }

    public static String sequenceNumberMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static int sequenceNumberNullValue()
    {
        return 65535;
    }

    public static int sequenceNumberMinValue()
    {
        return 0;
    }

    public static int sequenceNumberMaxValue()
    {
        return 65534;
    }

    public int sequenceNumber()
    {
        return CodecUtil.uint16Get(buffer, offset + 3, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

}
