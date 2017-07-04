/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum AdminTypeEnum
{
    SimulationComplete((short)0),
    WarmUpComplete((short)1),
    LOB((short)2),
    ShutDown((short)3),
    VWAP((short)4),
    StartLOB((short)5),
    EndLOB((short)6),
    BestBidOfferRequest((short)7),
    StartMessage((short)8),
    EndMessage((short)9),
    NULL_VAL((short)255);

    private final short value;

    AdminTypeEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static AdminTypeEnum get(final short value)
    {
        switch (value)
        {
            case 0: return SimulationComplete;
            case 1: return WarmUpComplete;
            case 2: return LOB;
            case 3: return ShutDown;
            case 4: return VWAP;
            case 5: return StartLOB;
            case 6: return EndLOB;
            case 7: return BestBidOfferRequest;
            case 8: return StartMessage;
            case 9: return EndMessage;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
