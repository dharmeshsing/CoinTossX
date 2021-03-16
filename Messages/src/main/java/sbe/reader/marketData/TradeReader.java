package sbe.reader.marketData;

import sbe.msg.marketData.TradeDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

public class TradeReader {
    private StringBuilder sb;
    private int bufferIndex;
    private TradeDecoder trade;
    private sbe.msg.marketData.MessageHeaderDecoder messageHeader;

    public TradeReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        trade = new TradeDecoder();
        messageHeader = new sbe.msg.marketData.MessageHeaderDecoder();
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        trade.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("MessageType=" + trade.messageType());
        sb.append("Nanosecond=" + trade.nanosecond());
        sb.append("ExecutedQuantity=" + trade.executedQuantity());
        sb.append("InstrumentId=" + trade.instrumentId());
        sb.append("TradeId=" + trade.tradeId());
        sb.append("Price=" + trade.price().mantissa());

        return sb;
    }
}
