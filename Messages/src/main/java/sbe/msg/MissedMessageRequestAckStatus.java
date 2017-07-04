/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum MissedMessageRequestAckStatus
{
    RequestAccepted((short)0),
    RequestLimitReached((short)1),
    InvalidPartitionID((short)2),
    ServiceUnavailable((short)3),
    NULL_VAL((short)255);

    private final short value;

    MissedMessageRequestAckStatus(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static MissedMessageRequestAckStatus get(final short value)
    {
        switch (value)
        {
            case 0: return RequestAccepted;
            case 1: return RequestLimitReached;
            case 2: return InvalidPartitionID;
            case 3: return ServiceUnavailable;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
