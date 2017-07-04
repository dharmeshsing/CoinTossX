/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum TimeInForceEnum
{
    Day((short)0),
    GTC((short)1),
    IOC((short)3),
    FOK((short)4),
    OPG((short)5),
    GTD((short)6),
    GTT((short)8),
    GFA((short)9),
    GFX((short)51),
    ATC((short)10),
    CPX((short)12),
    NULL_VAL((short)255);

    private final short value;

    TimeInForceEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static TimeInForceEnum get(final short value)
    {
        switch (value)
        {
            case 0: return Day;
            case 1: return GTC;
            case 3: return IOC;
            case 4: return FOK;
            case 5: return OPG;
            case 6: return GTD;
            case 8: return GTT;
            case 9: return GFA;
            case 51: return GFX;
            case 10: return ATC;
            case 12: return CPX;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
