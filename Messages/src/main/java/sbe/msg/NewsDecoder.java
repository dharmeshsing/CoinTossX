/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.sbe.codec.java.CodecUtil;

@SuppressWarnings("all")
public class NewsDecoder
{
    public static final int BLOCK_LENGTH = 964;
    public static final int TEMPLATE_ID = 16;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final NewsDecoder parentMessage = this;
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

    public NewsDecoder wrap(
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

    public static int partitionIdId()
    {
        return 1;
    }

    public static String partitionIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public short partitionId()
    {
        return CodecUtil.uint8Get(buffer, offset + 0);
    }


    public static int sequenceNumberId()
    {
        return 2;
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

    public int sequenceNumber()
    {
        return CodecUtil.int32Get(buffer, offset + 1, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int origTimeId()
    {
        return 3;
    }

    public static String origTimeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public byte origTime(final int index)
    {
        if (index < 0 || index >= 8)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 5 + (index * 1));
    }


    public static String origTimeCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getOrigTime(final byte[] dst, final int dstOffset)
    {
        final int length = 8;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 5, dst, dstOffset, length);
        return length;
    }


    public static int urgencyId()
    {
        return 4;
    }

    public static String urgencyMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public UrgencyEnum urgency()
    {
        return UrgencyEnum.get(CodecUtil.charGet(buffer, offset + 13));
    }


    public static int headLineId()
    {
        return 5;
    }

    public static String headLineMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public byte headLine(final int index)
    {
        if (index < 0 || index >= 100)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 14 + (index * 1));
    }


    public static String headLineCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getHeadLine(final byte[] dst, final int dstOffset)
    {
        final int length = 100;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 14, dst, dstOffset, length);
        return length;
    }


    public static int textId()
    {
        return 6;
    }

    public static String textMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public byte text(final int index)
    {
        if (index < 0 || index >= 750)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 114 + (index * 1));
    }


    public static String textCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getText(final byte[] dst, final int dstOffset)
    {
        final int length = 750;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 114, dst, dstOffset, length);
        return length;
    }


    public static int instrumentsId()
    {
        return 7;
    }

    public static String instrumentsMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
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

    public byte instruments(final int index)
    {
        if (index < 0 || index >= 100)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 864 + (index * 1));
    }


    public static String instrumentsCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getInstruments(final byte[] dst, final int dstOffset)
    {
        final int length = 100;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 864, dst, dstOffset, length);
        return length;
    }

}
