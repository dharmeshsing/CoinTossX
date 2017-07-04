/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum OrderBookEnum
{
    Regular((short)1),
    NULL_VAL((short)255);

    private final short value;

    OrderBookEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static OrderBookEnum get(final short value)
    {
        switch (value)
        {
            case 1: return Regular;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
