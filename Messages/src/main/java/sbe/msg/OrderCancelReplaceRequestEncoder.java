/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.agrona.MutableDirectBuffer;
import uk.co.real_logic.sbe.codec.java.CodecUtil;

@SuppressWarnings("all")
public class OrderCancelReplaceRequestEncoder
{
    public static final int BLOCK_LENGTH = 124;
    public static final int TEMPLATE_ID = 12;
    public static final int SCHEMA_ID = 1;
    public static final int SCHEMA_VERSION = 0;

    private final OrderCancelReplaceRequestEncoder parentMessage = this;
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

    public OrderCancelReplaceRequestEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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
    public OrderCancelReplaceRequestEncoder putClientOrderId(final byte[] src, final int srcOffset)
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
    public OrderCancelReplaceRequestEncoder putOrigClientOrderId(final byte[] src, final int srcOffset)
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
    public OrderCancelReplaceRequestEncoder orderId(final int value)
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
    public OrderCancelReplaceRequestEncoder securityId(final int value)
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
    public OrderCancelReplaceRequestEncoder putTraderMnemonic(final byte[] src, final int srcOffset)
    {
        final int length = 17;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 48, src, srcOffset, length);
        return this;
    }

    public static byte accountNullValue()
    {
        return (byte)0;
    }

    public static byte accountMinValue()
    {
        return (byte)32;
    }

    public static byte accountMaxValue()
    {
        return (byte)126;
    }

    public static int accountLength()
    {
        return 10;
    }

    public void account(final int index, final byte value)
    {
        if (index < 0 || index >= 10)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 65 + (index * 1), value);
    }

    public static String accountCharacterEncoding()
    {
        return "UTF-8";
    }
    public OrderCancelReplaceRequestEncoder putAccount(final byte[] src, final int srcOffset)
    {
        final int length = 10;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 65, src, srcOffset, length);
        return this;
    }
    public OrderCancelReplaceRequestEncoder orderType(final OrdTypeEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 75, value.value());
        return this;
    }
    public OrderCancelReplaceRequestEncoder timeInForce(final TimeInForceEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 76, value.value());
        return this;
    }

    public static byte expireTimeNullValue()
    {
        return (byte)0;
    }

    public static byte expireTimeMinValue()
    {
        return (byte)32;
    }

    public static byte expireTimeMaxValue()
    {
        return (byte)126;
    }

    public static int expireTimeLength()
    {
        return 17;
    }

    public void expireTime(final int index, final byte value)
    {
        if (index < 0 || index >= 17)
        {
            throw new IndexOutOfBoundsException("index out of range: index=" + index);
        }

        CodecUtil.charPut(buffer, this.offset + 77 + (index * 1), value);
    }

    public static String expireTimeCharacterEncoding()
    {
        return "UTF-8";
    }
    public OrderCancelReplaceRequestEncoder putExpireTime(final byte[] src, final int srcOffset)
    {
        final int length = 17;
        if (srcOffset < 0 || srcOffset > (src.length - length))
        {
            throw new IndexOutOfBoundsException("srcOffset out of range for copy: offset=" + srcOffset);
        }

        CodecUtil.charsPut(buffer, this.offset + 77, src, srcOffset, length);
        return this;
    }
    public OrderCancelReplaceRequestEncoder side(final SideEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 94, value.value());
        return this;
    }

    public static int orderQuantityNullValue()
    {
        return -2147483648;
    }

    public static int orderQuantityMinValue()
    {
        return -2147483647;
    }

    public static int orderQuantityMaxValue()
    {
        return 2147483647;
    }
    public OrderCancelReplaceRequestEncoder orderQuantity(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 95, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int displayQuantityNullValue()
    {
        return -2147483648;
    }

    public static int displayQuantityMinValue()
    {
        return -2147483647;
    }

    public static int displayQuantityMaxValue()
    {
        return 2147483647;
    }
    public OrderCancelReplaceRequestEncoder displayQuantity(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 99, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    public static int minQuantityNullValue()
    {
        return -2147483648;
    }

    public static int minQuantityMinValue()
    {
        return -2147483647;
    }

    public static int minQuantityMaxValue()
    {
        return 2147483647;
    }
    public OrderCancelReplaceRequestEncoder minQuantity(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 103, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }

    private final PriceEncoder limitPrice = new PriceEncoder();

    public PriceEncoder limitPrice()
    {
        limitPrice.wrap(buffer, offset + 107);
        return limitPrice;
    }

    private final PriceEncoder stopPrice = new PriceEncoder();

    public PriceEncoder stopPrice()
    {
        stopPrice.wrap(buffer, offset + 115);
        return stopPrice;
    }
    public OrderCancelReplaceRequestEncoder orderBook(final OrderBookEnum value)
    {
        CodecUtil.uint8Put(buffer, offset + 123, value.value());
        return this;
    }
}
