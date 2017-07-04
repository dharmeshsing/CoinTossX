/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum OrderSubTypeEnum
{
    Order((short)0),
    NULL_VAL((short)255);

    private final short value;

    OrderSubTypeEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static OrderSubTypeEnum get(final short value)
    {
        switch (value)
        {
            case 0: return Order;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
