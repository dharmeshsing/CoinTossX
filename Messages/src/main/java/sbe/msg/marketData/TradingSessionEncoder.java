/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

import uk.co.real_logic.agrona.MutableDirectBuffer;
import uk.co.real_logic.sbe.codec.java.CodecUtil;

@SuppressWarnings("all")
public class TradingSessionEncoder
{
    public static final int BLOCK_LENGTH = 5;
    public static final int TEMPLATE_ID = 90;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final TradingSessionEncoder parentMessage = this;
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

    public TradingSessionEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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
    public TradingSessionEncoder tradingSession(final TradingSessionEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 0, value.value());
        return this;
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
    public TradingSessionEncoder securityId(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 1, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
