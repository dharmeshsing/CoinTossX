/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum OrdTypeEnum
{
    Market((short)1),
    Limit((short)2),
    Stop((short)3),
    StopLimit((short)4),
    NULL_VAL((short)255);

    private final short value;

    OrdTypeEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static OrdTypeEnum get(final short value)
    {
        switch (value)
        {
            case 1: return Market;
            case 2: return Limit;
            case 3: return Stop;
            case 4: return StopLimit;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
