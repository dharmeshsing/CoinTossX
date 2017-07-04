/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum ContainerEnum
{
    None((short)0),
    Main((short)1),
    MarketOrder((short)3),
    ParkedOrder((short)5),
    StopOrder((short)6),
    NULL_VAL((short)255);

    private final short value;

    ContainerEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static ContainerEnum get(final short value)
    {
        switch (value)
        {
            case 0: return None;
            case 1: return Main;
            case 3: return MarketOrder;
            case 5: return ParkedOrder;
            case 6: return StopOrder;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
