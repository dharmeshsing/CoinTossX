package sbe.reader.marketData;

import sbe.msg.marketData.AuctionTradeDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class AuctionTradeReader {
    private StringBuilder sb;
    private int bufferIndex;
    private AuctionTradeDecoder auctionTrade;
    private sbe.msg.marketData.MessageHeaderDecoder messageHeader;

    public AuctionTradeReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        auctionTrade = new AuctionTradeDecoder();
        messageHeader = new sbe.msg.marketData.MessageHeaderDecoder();
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        auctionTrade.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("MessageType=" + auctionTrade.messageType());
        sb.append("Nanosecond=" + auctionTrade.nanosecond());
        sb.append("ExecutedQuantity=" + auctionTrade.executedQuantity());
        sb.append("InstrumentId=" + auctionTrade.instrumentId());
        sb.append("TradeId=" + auctionTrade.tradeId());
        sb.append("Price=" + auctionTrade.price().mantissa());

        return sb;
    }
}
