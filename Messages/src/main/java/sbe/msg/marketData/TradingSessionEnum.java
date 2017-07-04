/* Generated SBE (Simple Binary Encoding) message codec */
package sbe.msg.marketData;

public enum TradingSessionEnum
{
    StartOfTrading((short)0),
    OpeningAuctionCall((short)1),
    ContinuousTrading((short)2),
    FCOAuctionCall((short)3),
    VolatilityAuctionCall((short)4),
    IntraDayAuctionCall((short)5),
    ClosingAuctionCall((short)6),
    ClosingPricePublication((short)7),
    ClosingPriceCross((short)8),
    PostClose((short)9),
    TradeReporting((short)10),
    NULL_VAL((short)255);

    private final short value;

    TradingSessionEnum(final short value)
    {
        this.value = value;
    }

    public short value()
    {
        return value;
    }

    public static TradingSessionEnum get(final short value)
    {
        switch (value)
        {
            case 0: return StartOfTrading;
            case 1: return OpeningAuctionCall;
            case 2: return ContinuousTrading;
            case 3: return FCOAuctionCall;
            case 4: return VolatilityAuctionCall;
            case 5: return IntraDayAuctionCall;
            case 6: return ClosingAuctionCall;
            case 7: return ClosingPricePublication;
            case 8: return ClosingPriceCross;
            case 9: return PostClose;
            case 10: return TradeReporting;
        }

        if ((short)255 == value)
        {
            return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
