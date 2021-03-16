/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

import uk.co.real_logic.sbe.codec.java.CodecUtil;
import uk.co.real_logic.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public class UnitHeaderEncoder
{
    public static final int BLOCK_LENGTH = 5;
    public static final int TEMPLATE_ID = 18;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final UnitHeaderEncoder parentMessage = this;
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

    public UnitHeaderEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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
    public UnitHeaderEncoder messageCount(final int value)
    {
        CodecUtil.uint16Put(buffer, offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
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
    public UnitHeaderEncoder marketDataGroup(final byte value)
    {
        CodecUtil.int8Put(buffer, offset + 2, value);
        return this;
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
    public UnitHeaderEncoder sequenceNumber(final int value)
    {
        CodecUtil.uint16Put(buffer, offset + 3, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
