/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum SideEnum
{
    Buy((short)1),
    Sell((short)2),
    NULL_VAL((short)255);

    private final short value;

    SideEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static SideEnum get(final short value)
    {
        switch (value)
        {
            case 1: return Buy;
            case 2: return Sell;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
