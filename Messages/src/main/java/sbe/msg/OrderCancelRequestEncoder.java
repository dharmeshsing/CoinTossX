/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.agrona.MutableDirectBuffer;
import uk.co.real_logic.sbe.codec.java.CodecUtil;

@SuppressWarnings("all")
public class OrderCancelRequestEncoder
{
    public static final int BLOCK_LENGTH = 67;
    public static final int TEMPLATE_ID = 10;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final OrderCancelRequestEncoder parentMessage = this;
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

    public OrderCancelRequestEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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
    public OrderCancelRequestEncoder putClientOrderId(final byte[] src, final int srcOffset)
    {
        final int length = 20;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 0, src, srcOffset, length);
        return this;
    }

    public static byte origClientOrderIdNullValue()
    {
        return (byte)0;
    }

    public static byte origClientOrderIdMinValue()
    {
        return (byte)32;
    }

    public static byte origClientOrderIdMaxValue()
    {
        return (byte)126;
    }

    public static int origClientOrderIdLength()
    {
        return 20;
    }

    public void origClientOrderId(final int index, final byte value)
    {
        if (index < 0 || index >= 20)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 20 + (index * 1), value);
    }

    public static String origClientOrderIdCharacterEncoding()
    {
        return "UTF-8";
    }
    public OrderCancelRequestEncoder putOrigClientOrderId(final byte[] src, final int srcOffset)
    {
        final int length = 20;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 20, src, srcOffset, length);
        return this;
    }

    public static int orderIdNullValue()
    {
        return -2147483648;
    }

    public static int orderIdMinValue()
    {
        return -2147483647;
    }

    public static int orderIdMaxValue()
    {
        return 2147483647;
    }
    public OrderCancelRequestEncoder orderId(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 40, value, java.nio.ByteOrder.LITTLE_ENDIAN);
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
    public OrderCancelRequestEncoder securityId(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 44, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static byte traderMnemonicNullValue()
    {
        return (byte)0;
    }

    public static byte traderMnemonicMinValue()
    {
        return (byte)32;
    }

    public static byte traderMnemonicMaxValue()
    {
        return (byte)126;
    }

    public static int traderMnemonicLength()
    {
        return 17;
    }

    public void traderMnemonic(final int index, final byte value)
    {
        if (index < 0 || index >= 17)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 48 + (index * 1), value);
    }

    public static String traderMnemonicCharacterEncoding()
    {
        return "UTF-8";
    }
    public OrderCancelRequestEncoder putTraderMnemonic(final byte[] src, final int srcOffset)
    {
        final int length = 17;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 48, src, srcOffset, length);
        return this;
    }
    public OrderCancelRequestEncoder side(final SideEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 65, value.value());
        return this;
    }
    public OrderCancelRequestEncoder orderBook(final OrderBookEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 66, value.value());
        return this;
    }
}
