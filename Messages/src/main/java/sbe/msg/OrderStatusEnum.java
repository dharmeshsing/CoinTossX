/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum OrderStatusEnum
{
    New((short)0),
    PartiallyFilled((short)1),
    Filled((short)2),
    Cancelled((short)4),
    Expired((short)6),
    Rejected((short)8),
    Suspended((short)9),
    NULL_VAL((short)255);

    private final short value;

    OrderStatusEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static OrderStatusEnum get(final short value)
    {
        switch (value)
        {
            case 0: return New;
            case 1: return PartiallyFilled;
            case 2: return Filled;
            case 4: return Cancelled;
            case 6: return Expired;
            case 8: return Rejected;
            case 9: return Suspended;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
