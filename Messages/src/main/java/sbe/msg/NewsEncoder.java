/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.sbe.codec.java.*;
import uk.co.real_logic.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public class NewsEncoder
{
    public static final int BLOCK_LENGTH = 964;
    public static final int TEMPLATE_ID = 16;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final NewsEncoder parentMessage = this;
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

    public NewsEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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

    public static short partitionIdNullValue()
    {
        return (short)255;
    }

    public static short partitionIdMinValue()
    {
        return (short)0;
    }

    public static short partitionIdMaxValue()
    {
        return (short)254;
    }
    public NewsEncoder partitionId(final short value)
    {
        CodecUtil.uint8Put(buffer, offset + 0, value);
        return this;
    }

    public static int sequenceNumberNullValue()
    {
        return -2147483648;
    }

    public static int sequenceNumberMinValue()
    {
        return -2147483647;
    }

    public static int sequenceNumberMaxValue()
    {
        return 2147483647;
    }
    public NewsEncoder sequenceNumber(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 1, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static byte origTimeNullValue()
    {
        return (byte)0;
    }

    public static byte origTimeMinValue()
    {
        return (byte)32;
    }

    public static byte origTimeMaxValue()
    {
        return (byte)126;
    }

    public static int origTimeLength()
    {
        return 8;
    }

    public void origTime(final int index, final byte value)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 5 + (index * 1), value);
    }

    public static String origTimeCharacterEncoding()
    {
        return "UTF-8";
    }
    public NewsEncoder putOrigTime(final byte[] src, final int srcOffset)
    {
        final int length = 8;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 5, src, srcOffset, length);
        return this;
    }
    public NewsEncoder urgency(final UrgencyEnum value)
    {
        CodecUtil.charPut(buffer, offset + 13, value.value());
        return this;
    }

    public static byte headLineNullValue()
    {
        return (byte)0;
    }

    public static byte headLineMinValue()
    {
        return (byte)32;
    }

    public static byte headLineMaxValue()
    {
        return (byte)126;
    }

    public static int headLineLength()
    {
        return 100;
    }

    public void headLine(final int index, final byte value)
    {
        if (index < 0 || index >= 100)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 14 + (index * 1), value);
    }

    public static String headLineCharacterEncoding()
    {
        return "UTF-8";
    }
    public NewsEncoder putHeadLine(final byte[] src, final int srcOffset)
    {
        final int length = 100;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 14, src, srcOffset, length);
        return this;
    }

    public static byte textNullValue()
    {
        return (byte)0;
    }

    public static byte textMinValue()
    {
        return (byte)32;
    }

    public static byte textMaxValue()
    {
        return (byte)126;
    }

    public static int textLength()
    {
        return 750;
    }

    public void text(final int index, final byte value)
    {
        if (index < 0 || index >= 750)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 114 + (index * 1), value);
    }

    public static String textCharacterEncoding()
    {
        return "UTF-8";
    }
    public NewsEncoder putText(final byte[] src, final int srcOffset)
    {
        final int length = 750;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 114, src, srcOffset, length);
        return this;
    }

    public static byte instrumentsNullValue()
    {
        return (byte)0;
    }

    public static byte instrumentsMinValue()
    {
        return (byte)32;
    }

    public static byte instrumentsMaxValue()
    {
        return (byte)126;
    }

    public static int instrumentsLength()
    {
        return 100;
    }

    public void instruments(final int index, final byte value)
    {
        if (index < 0 || index >= 100)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 864 + (index * 1), value);
    }

    public static String instrumentsCharacterEncoding()
    {
        return "UTF-8";
    }
    public NewsEncoder putInstruments(final byte[] src, final int srcOffset)
    {
        final int length = 100;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 864, src, srcOffset, length);
        return this;
    }
}
