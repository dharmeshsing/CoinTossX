/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum SystemStatusEnum
{
    RecoveryServiceResumed((short)1),
    RecoveryServiceUnavailable((short)2),
    NULL_VAL((short)255);

    private final short value;

    SystemStatusEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static SystemStatusEnum get(final short value)
    {
        switch (value)
        {
            case 1: return RecoveryServiceResumed;
            case 2: return RecoveryServiceUnavailable;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
