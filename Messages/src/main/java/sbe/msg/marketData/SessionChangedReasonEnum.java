/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

public enum SessionChangedReasonEnum
{
    ScheduledTransition((short)0),
    ExtendedByMarketOps((short)1),
    ShortenedByMarketOps((short)2),
    MarketOrderImbalance((short)3),
    PriceOutsideRange((short)4),
    CircuitBreakerTripped((short)5),
    Unavailable((short)9),
    NULL_VAL((short)255);

    private final short value;

    SessionChangedReasonEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static SessionChangedReasonEnum get(final short value)
    {
        switch (value)
        {
            case 0: return ScheduledTransition;
            case 1: return ExtendedByMarketOps;
            case 2: return ShortenedByMarketOps;
            case 3: return MarketOrderImbalance;
            case 4: return PriceOutsideRange;
            case 5: return CircuitBreakerTripped;
            case 9: return Unavailable;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
