/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.sbe.codec.java.CodecUtil;
import uk.co.real_logic.agrona.DirectBuffer;

@SuppressWarnings("all")
public class LogonDecoder
{
    public static final int BLOCK_LENGTH = 24;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final LogonDecoder parentMessage = this;
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

    public LogonDecoder wrap(
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

    public static int compIDId()
    {
        return 1;
    }

    public static String compIDMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static int compIDNullValue()
    {
        return -2147483648;
    }

    public static int compIDMinValue()
    {
        return -2147483647;
    }

    public static int compIDMaxValue()
    {
        return 2147483647;
    }

    public int compID()
    {
        return CodecUtil.int32Get(buffer, offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int passwordId()
    {
        return 2;
    }

    public static String passwordMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte passwordNullValue()
    {
        return (byte)0;
    }

    public static byte passwordMinValue()
    {
        return (byte)32;
    }

    public static byte passwordMaxValue()
    {
        return (byte)126;
    }

    public static int passwordLength()
    {
        return 10;
    }

    public byte password(final int index)
    {
        if (index < 0 || index >= 10)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 4 + (index * 1));
    }


    public static String passwordCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getPassword(final byte[] dst, final int dstOffset)
    {
        final int length = 10;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 4, dst, dstOffset, length);
        return length;
    }


    public static int newPasswordId()
    {
        return 3;
    }

    public static String newPasswordMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte newPasswordNullValue()
    {
        return (byte)0;
    }

    public static byte newPasswordMinValue()
    {
        return (byte)32;
    }

    public static byte newPasswordMaxValue()
    {
        return (byte)126;
    }

    public static int newPasswordLength()
    {
        return 10;
    }

    public byte newPassword(final int index)
    {
        if (index < 0 || index >= 10)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 14 + (index * 1));
    }


    public static String newPasswordCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getNewPassword(final byte[] dst, final int dstOffset)
    {
        final int length = 10;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 14, dst, dstOffset, length);
        return length;
    }

}
