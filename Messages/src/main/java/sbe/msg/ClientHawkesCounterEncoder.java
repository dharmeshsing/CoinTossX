/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.sbe.codec.java.CodecUtil;
import uk.co.real_logic.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public class ClientHawkesCounterEncoder
{
    public static final int BLOCK_LENGTH = 12;
    public static final int TEMPLATE_ID = 92;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final ClientHawkesCounterEncoder parentMessage = this;
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

    public ClientHawkesCounterEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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

    public static int clientIdNullValue()
    {
        return -2147483648;
    }

    public static int clientIdMinValue()
    {
        return -2147483647;
    }

    public static int clientIdMaxValue()
    {
        return 2147483647;
    }
    public ClientHawkesCounterEncoder clientId(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int maxNullValue()
    {
        return -2147483648;
    }

    public static int maxMinValue()
    {
        return -2147483647;
    }

    public static int maxMaxValue()
    {
        return 2147483647;
    }
    public ClientHawkesCounterEncoder max(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 4, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int completeNullValue()
    {
        return -2147483648;
    }

    public static int completeMinValue()
    {
        return -2147483647;
    }

    public static int completeMaxValue()
    {
        return 2147483647;
    }
    public ClientHawkesCounterEncoder complete(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 8, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
