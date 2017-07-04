/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum BusinessRejectEnum
{
    UnknownInstrument(90000),
    MatchingPartitionSuspended(9998),
    SystemSuspended(9999),
    NULL_VAL(-2147483648);

    private final int value;

    BusinessRejectEnum(final int value)
    {
        this.value = value;
    }

    public int value()
    {
        return value;
    }

    public static BusinessRejectEnum get(final int value)
    {
        switch (value)
        {
            case 90000: return UnknownInstrument;
            case 9998: return MatchingPartitionSuspended;
            case 9999: return SystemSuspended;
        }

        if (-2147483648 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
