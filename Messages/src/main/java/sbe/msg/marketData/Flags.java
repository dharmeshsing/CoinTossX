/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

public enum Flags
{
    Priority((short)0),
    B((short)1),
    C((short)2),
    MarketOrder((short)4),
    NULL_VAL((short)255);

    private final short value;

    Flags(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static Flags get(final short value)
    {
        switch (value)
        {
            case 0: return Priority;
            case 1: return B;
            case 2: return C;
            case 4: return MarketOrder;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
