/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg;

public enum ExecutionTypeEnum
{
    New((byte)48),
    Cancelled((byte)52),
    Amended((byte)53),
    Rejected((byte)56),
    Suspended((byte)57),
    Expired((byte)67),
    Trade((byte)70),
    TradeCorrect((byte)71),
    TradeCancel((byte)72),
    Restated((byte)68),
    Triggered((byte)76),
    NULL_VAL((byte)0);

    private final byte value;

    ExecutionTypeEnum(final byte value)
    {
        this.value = value;
    }

    public byte value()
    {
        return value;
    }

    public static ExecutionTypeEnum get(final byte value)
    {
        switch (value)
        {
            case 48: return New;
            case 52: return Cancelled;
            case 53: return Amended;
            case 56: return Rejected;
            case 57: return Suspended;
            case 67: return Expired;
            case 70: return Trade;
            case 71: return TradeCorrect;
            case 72: return TradeCancel;
            case 68: return Restated;
            case 76: return Triggered;
        }

        if ((byte)0 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
