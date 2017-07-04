/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum IsMarketOpsRequestEnum
{
    No((short)0),
    Yes((short)1),
    NULL_VAL((short)255);

    private final short value;

    IsMarketOpsRequestEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static IsMarketOpsRequestEnum get(final short value)
    {
        switch (value)
        {
            case 0: return No;
            case 1: return Yes;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
