/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum TransmissionCompleteStatus
{
    AllMessageTransmitted((short)0),
    MessageLimitReached((short)1),
    ServiceUnavailable((short)3),
    NULL_VAL((short)255);

    private final short value;

    TransmissionCompleteStatus(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static TransmissionCompleteStatus get(final short value)
    {
        switch (value)
        {
            case 0: return AllMessageTransmitted;
            case 1: return MessageLimitReached;
            case 3: return ServiceUnavailable;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
