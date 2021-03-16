/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.sbe.codec.java.CodecUtil;
import uk.co.real_logic.agrona.DirectBuffer;

@SuppressWarnings("all")
public class RejectDecoder
{
    public static final int BLOCK_LENGTH = 55;
    public static final int TEMPLATE_ID = 4;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final RejectDecoder parentMessage = this;
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

    public RejectDecoder wrap(
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

    public static int rejectCodeId()
    {
        return 1;
    }

    public static String rejectCodeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static int rejectCodeNullValue()
    {
        return -2147483648;
    }

    public static int rejectCodeMinValue()
    {
        return -2147483647;
    }

    public static int rejectCodeMaxValue()
    {
        return 2147483647;
    }

    public int rejectCode()
    {
        return CodecUtil.int32Get(buffer, offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int rejectReasonId()
    {
        return 2;
    }

    public static String rejectReasonMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte rejectReasonNullValue()
    {
        return (byte)0;
    }

    public static byte rejectReasonMinValue()
    {
        return (byte)32;
    }

    public static byte rejectReasonMaxValue()
    {
        return (byte)126;
    }

    public static int rejectReasonLength()
    {
        return 30;
    }

    public byte rejectReason(final int index)
    {
        if (index < 0 || index >= 30)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 4 + (index * 1));
    }


    public static String rejectReasonCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getRejectReason(final byte[] dst, final int dstOffset)
    {
        final int length = 30;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 4, dst, dstOffset, length);
        return length;
    }


    public static int messageTypeId()
    {
        return 3;
    }

    public static String messageTypeMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte messageTypeNullValue()
    {
        return (byte)0;
    }

    public static byte messageTypeMinValue()
    {
        return (byte)32;
    }

    public static byte messageTypeMaxValue()
    {
        return (byte)126;
    }

    public byte messageType()
    {
        return CodecUtil.charGet(buffer, offset + 34);
    }


    public static int clientOrderIdId()
    {
        return 4;
    }

    public static String clientOrderIdMetaAttribute(final MetaAttribute metaAttribute)
    {
        switch (metaAttribute)
        {
            case EPOCH: return "unix";
            case TIME_UNIT: return "nanosecond";
            case SEMANTIC_TYPE: return "";
        }

        return "";
    }

    public static byte clientOrderIdNullValue()
    {
        return (byte)0;
    }

    public static byte clientOrderIdMinValue()
    {
        return (byte)32;
    }

    public static byte clientOrderIdMaxValue()
    {
        return (byte)126;
    }

    public static int clientOrderIdLength()
    {
        return 20;
    }

    public byte clientOrderId(final int index)
    {
        if (index < 0 || index >= 20)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        return CodecUtil.charGet(buffer, this.offset + 35 + (index * 1));
    }


    public static String clientOrderIdCharacterEncoding()
    {
        return "UTF-8";
    }

    public int getClientOrderId(final byte[] dst, final int dstOffset)
    {
        final int length = 20;
        if (dstOffset < 0 || dstOffset > (dst.length - length))
        {
            throw new IndexOutOfBoundsException("dstOffset out of range for copy: offset=" + dstOffset);
        }

        CodecUtil.charsGet(buffer, this.offset + 35, dst, dstOffset, length);
        return length;
    }

}
