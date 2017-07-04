/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum OrderMassCancelReportStatusEnum
{
    Rejected((short)0),
    Accepted((short)7),
    NULL_VAL((short)255);

    private final short value;

    OrderMassCancelReportStatusEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static OrderMassCancelReportStatusEnum get(final short value)
    {
        switch (value)
        {
            case 0: return Rejected;
            case 7: return Accepted;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
