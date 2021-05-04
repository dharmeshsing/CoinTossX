/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.sbe.codec.java.CodecUtil;
import uk.co.real_logic.agrona.DirectBuffer;

@SuppressWarnings("all")
public class LogoutDecoder
{
    public static final int BLOCK_LENGTH = 20;
    public static final int TEMPLATE_ID = 3;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final LogoutDecoder parentMessage = this;
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

    public LogoutDecoder wrap(
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

    public static int reasonId()
    {
        return 1;
    }

    public static String reasonMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte reasonNullValue()
    {
        return (byte)0;
    }

    public static byte reasonMinValue()
    {
        return (byte)32;
    }

    public static byte reasonMaxValue()
    {
        return (byte)126;
    }

    public static int reasonLength()
    {
        return 20;
    }

    public byte reason(final int index)
    {
        if (index < 0 || index >= 20)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 0 + (index * 1));
    }


    public static String reasonCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getReason(final byte[] dst, final int dstOffset)
    {
        final int length = 20;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 0, dst, dstOffset, length);
        return length;
    }

}
