/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

public enum PrintableEnum
{
    NonPrintable((byte)78),
    Printable((byte)89),
    NULL_VAL((byte)0);

    private final byte value;

    PrintableEnum(final byte value)
    {
        this.value = value;
    }

    public byte value()
    {
        return value;
    }

    public static PrintableEnum get(final byte value)
    {
        switch (value)
        {
            case 78: return NonPrintable;
            case 89: return Printable;
        }

        if ((byte)0 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
