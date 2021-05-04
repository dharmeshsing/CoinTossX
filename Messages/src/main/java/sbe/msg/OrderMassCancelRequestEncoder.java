/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.sbe.codec.java.CodecUtil;
import uk.co.real_logic.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public class OrderMassCancelRequestEncoder
{
    public static final int BLOCK_LENGTH = 33;
    public static final int TEMPLATE_ID = 11;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final OrderMassCancelRequestEncoder parentMessage = this;
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

    public OrderMassCancelRequestEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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

    public void clientOrderId(final int index, final byte value)
    {
        if (index < 0 || index >= 20)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 0 + (index * 1), value);
    }

    public static String clientOrderIdCharacterEncoding()
    {
        return "UTF-8";
    }
    public OrderMassCancelRequestEncoder putClientOrderId(final byte[] src, final int srcOffset)
    {
        final int length = 20;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 0, src, srcOffset, length);
        return this;
    }
    public OrderMassCancelRequestEncoder massCancelRequestType(final MassCancelRequestTypeEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 20, value.value());
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
    public OrderMassCancelRequestEncoder securityId(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 21, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static byte segmentNullValue()
    {
        return (byte)0;
    }

    public static byte segmentMinValue()
    {
        return (byte)32;
    }

    public static byte segmentMaxValue()
    {
        return (byte)126;
    }

    public static int segmentLength()
    {
        return 6;
    }

    public void segment(final int index, final byte value)
    {
        if (index < 0 || index >= 6)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 25 + (index * 1), value);
    }

    public static String segmentCharacterEncoding()
    {
        return "UTF-8";
    }
    public OrderMassCancelRequestEncoder putSegment(final byte[] src, final int srcOffset)
    {
        final int length = 6;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 25, src, srcOffset, length);
        return this;
    }
    public OrderMassCancelRequestEncoder orderSubType(final OrderSubTypeEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 31, value.value());
        return this;
    }
    public OrderMassCancelRequestEncoder orderBook(final OrderBookEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 32, value.value());
        return this;
    }
}
