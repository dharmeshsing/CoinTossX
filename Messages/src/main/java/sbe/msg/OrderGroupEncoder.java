/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

import uk.co.real_logic.sbe.codec.java.*;
import uk.co.real_logic.agrona.MutableDirectBuffer;

@SuppressWarnings("all")
public class OrderGroupEncoder
{
    public static final int ENCODED_LENGTH = 8;
    private MutableDirectBuffer buffer;
    private int offset;
    public OrderGroupEncoder wrap(final MutableDirectBuffer buffer, final int offset)
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
    public OrderGroupEncoder blockLength(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 0, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
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
    public OrderGroupEncoder numInGroup(final int value)
    {
        CodecUtil.int32Put(buffer, offset + 4, value, java.nio.ByteOrder.LITTLE_ENDIAN);
        return this;
    }
}
