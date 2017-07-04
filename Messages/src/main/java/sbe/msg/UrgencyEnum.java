/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum UrgencyEnum
{
    Regular((byte)48),
    HighPriorty((byte)49),
    LowPriorty((byte)50),
    NULL_VAL((byte)0);

    private final byte value;

    UrgencyEnum(final byte value)
    {
        this.value = value;
    }

    public byte value()
    {
        return value;
    }

    public static UrgencyEnum get(final byte value)
    {
        switch (value)
        {
            case 48: return Regular;
            case 49: return HighPriorty;
            case 50: return LowPriorty;
        }

        if ((byte)0 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
