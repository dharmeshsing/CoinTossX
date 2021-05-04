/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.sbe.codec.java.CodecUtil;
import uk.co.real_logic.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public class LogonResponseEncoder
{
    public static final int BLOCK_LENGTH = 8;
    public static final int TEMPLATE_ID = 2;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final LogonResponseEncoder parentMessage = this;
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

    public LogonResponseEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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
    public LogonResponseEncoder rejectCode(final RejectCode value)
    {
        CodecUtil.int32Put(buffer, offset + 0, value.value(), java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int passwordExpiryNullValue()
    {
        return -2147483648;
    }

    public static int passwordExpiryMinValue()
    {
        return -2147483647;
    }

    public static int passwordExpiryMaxValue()
    {
        return 2147483647;
    }
    public LogonResponseEncoder passwordExpiry(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 4, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
