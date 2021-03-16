package sbe.reader.marketData;

import sbe.msg.MessageHeaderDecoder;
import sbe.msg.marketData.SessionChangedReasonEnum;
import sbe.msg.marketData.SymbolStatusDecoder;
import sbe.msg.marketData.TradingSessionEnum;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

public class SymbolStatusReader {
    private StringBuilder sb;
    private int bufferIndex;
    private SymbolStatusDecoder symbolStatus;
    private MessageHeaderDecoder messageHeader;

    private long securityId;
    private SessionChangedReasonEnum sessionChangedReason;
    private TradingSessionEnum tradingSessionEnum;

    public SymbolStatusReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        messageHeader = new MessageHeaderDecoder();
        symbolStatus = new SymbolStatusDecoder();
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        symbolStatus.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        securityId = symbolStatus.instrumentId();
        tradingSessionEnum = symbolStatus.tradingSession();
        sessionChangedReason = symbolStatus.sessionChangedReason();

//        sb.append("MessageTypeEnum=" + symbolStatus.messageType());
//        sb.append("Nanosecond=" + symbolStatus.nanosecond());
//        sb.append("TradingStatus=" + symbolStatus.tradingStatus());
//        sb.append("HaltReason=" + symbolStatus.haltReason());
//        sb.append("SessionChangedReason=" + symbolStatus.sessionChangedReason());

        return sb;
    }

    public long getSecurityId() {
        return securityId;
    }

    public SessionChangedReasonEnum getSessionChangedReason() {
        return sessionChangedReason;
    }

    public TradingSessionEnum getTradingSession() {
        return tradingSessionEnum;
    }
}
