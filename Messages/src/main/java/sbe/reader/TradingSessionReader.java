package sbe.reader;

import sbe.msg.MessageHeaderDecoder;
import sbe.msg.marketData.TradingSessionDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class TradingSessionReader {
    private StringBuilder sb;
    private int bufferIndex;
    private TradingSessionDecoder tradingSession;
    private MessageHeaderDecoder messageHeader;

    public TradingSessionReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        messageHeader = new MessageHeaderDecoder();
        tradingSession = new TradingSessionDecoder();
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        tradingSession.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("TradingSession=" + tradingSession.tradingSession());
        sb.append("securityId=" + tradingSession.securityId());

        return sb;
    }
}
