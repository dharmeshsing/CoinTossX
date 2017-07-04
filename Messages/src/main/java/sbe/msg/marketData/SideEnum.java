/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

public enum SideEnum
{
    Buy((byte)66),
    Sell((byte)83),
    NULL_VAL((byte)0);

    private final byte value;

    SideEnum(final byte value)
    {
        this.value = value;
    }

    public byte value()
    {
        return value;
    }

    public static SideEnum get(final byte value)
    {
        switch (value)
        {
            case 66: return Buy;
            case 83: return Sell;
        }

        if ((byte)0 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
