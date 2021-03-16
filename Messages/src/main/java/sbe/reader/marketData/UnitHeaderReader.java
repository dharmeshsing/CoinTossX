package sbe.reader.marketData;

import sbe.msg.marketData.MessageHeaderDecoder;
import sbe.msg.marketData.UnitHeaderDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

public class UnitHeaderReader {
    private StringBuilder sb;
    private int bufferIndex;
    private UnitHeaderDecoder unitHeader;
    private MessageHeaderDecoder messageHeader;

    public UnitHeaderReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        unitHeader = new UnitHeaderDecoder();
        messageHeader = new MessageHeaderDecoder();
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        unitHeader.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("MessageCount=" + unitHeader.messageCount());
        sb.append("MarketDataGroup=" + unitHeader.marketDataGroup());
        sb.append("SequenceNumber=" + unitHeader.sequenceNumber());

        return sb;
    }
}
