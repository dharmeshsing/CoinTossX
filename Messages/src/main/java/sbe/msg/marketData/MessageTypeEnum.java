/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

public enum MessageTypeEnum
{
    AddOrder((byte)65),
    OrderDeleted((byte)68),
    OrderModified((byte)85),
    OrderBookClear((byte)121),
    OrderExecuted((byte)69),
    OrderExecutedPriceSize((byte)67),
    Trade((byte)80),
    AuctionTrade((byte)81),
    OffBookTrade((byte)120),
    TradeBreak((byte)66),
    BestBidAsk((byte)90),
    SymbolStatus((byte)72),
    NULL_VAL((byte)0);

    private final byte value;

    MessageTypeEnum(final byte value)
    {
        this.value = value;
    }

    public byte value()
    {
        return value;
    }

    public static MessageTypeEnum get(final byte value)
    {
        switch (value)
        {
            case 65: return AddOrder;
            case 68: return OrderDeleted;
            case 85: return OrderModified;
            case 121: return OrderBookClear;
            case 69: return OrderExecuted;
            case 67: return OrderExecutedPriceSize;
            case 80: return Trade;
            case 81: return AuctionTrade;
            case 120: return OffBookTrade;
            case 66: return TradeBreak;
            case 90: return BestBidAsk;
            case 72: return SymbolStatus;
        }

        if ((byte)0 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
