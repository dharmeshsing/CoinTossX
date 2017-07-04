/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum CapacityEnum
{
    Principal((short)2),
    Agency((short)3),
    NULL_VAL((short)255);

    private final short value;

    CapacityEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static CapacityEnum get(final short value)
    {
        switch (value)
        {
            case 2: return Principal;
            case 3: return Agency;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
