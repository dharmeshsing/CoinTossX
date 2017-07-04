/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.sbe.codec.java.CodecUtil;

@SuppressWarnings("all")
public class OrderGroupDecoder
{
    public static final int ENCODED_LENGTH = 8;
    private DirectBuffer buffer;
    private int offset;
    public OrderGroupDecoder wrap(final DirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;
        return this;
    }

    public int encodedLength()
    {
        return ENCODED_LENGTH;
    }

    public static int blockLengthNullValue()
    {
        return -2147483648;
    }

    public static int blockLengthMinValue()
    {
        return -2147483647;
    }

    public static int blockLengthMaxValue()
    {
        return 2147483647;
    }

    public int blockLength()
    {
        return CodecUtil.int32Get(buffer, offset + 0, java.nio.ByteOrder.LITTLE_ENDIAN);
    }


    public static int numInGroupNullValue()
    {
        return -2147483648;
    }

    public static int numInGroupMinValue()
    {
        return -2147483647;
    }

    public static int numInGroupMaxValue()
    {
        return 2147483647;
    }

    public int numInGroup()
    {
        return CodecUtil.int32Get(buffer, offset + 4, java.nio.ByteOrder.LITTLE_ENDIAN);
    }

}
