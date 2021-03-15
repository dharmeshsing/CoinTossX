/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.sbe.codec.java.*;
import uk.co.real_logic.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public class LogonEncoder
{
    public static final int BLOCK_LENGTH = 24;
    public static final int TEMPLATE_ID = 1;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final LogonEncoder parentMessage = this;
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

    public LogonEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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
    public LogonEncoder compID(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
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

    public void password(final int index, final byte value)
    {
        if (index < 0 || index >= 10)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 4 + (index * 1), value);
    }

    public static String passwordCharacterEncoding()
    {
        return "UTF-8";
    }
    public LogonEncoder putPassword(final byte[] src, final int srcOffset)
    {
        final int length = 10;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 4, src, srcOffset, length);
        return this;
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

    public void newPassword(final int index, final byte value)
    {
        if (index < 0 || index >= 10)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 14 + (index * 1), value);
    }

    public static String newPasswordCharacterEncoding()
    {
        return "UTF-8";
    }
    public LogonEncoder putNewPassword(final byte[] src, final int srcOffset)
    {
        final int length = 10;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 14, src, srcOffset, length);
        return this;
    }
}
