package sbe.builder;

import sbe.msg.MessageHeaderEncoder;
import sbe.msg.marketData.TradingSessionEncoder;
import sbe.msg.marketData.TradingSessionEnum;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class TradingSessionBuilder {
    private int bufferIndex;
    private TradingSessionEncoder tradingSession;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private TradingSessionEnum tradingSessionEnum;
    private int compID;
    private int securityId;

    public static int BUFFER_SIZE = 256;

    public TradingSessionBuilder(){
        tradingSession = new TradingSessionEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));
    }

    public TradingSessionBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public TradingSessionBuilder tradingSessionEnum(TradingSessionEnum value){
        this.tradingSessionEnum = value;
        return this;
    }

    public TradingSessionBuilder securityId(int value){
        this.securityId = value;
        return this;
    }

    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(tradingSession.sbeBlockLength())
                .templateId(tradingSession.sbeTemplateId())
                .schemaId(tradingSession.sbeSchemaId())
                .version(tradingSession.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        tradingSession.wrap(encodeBuffer, bufferIndex)
                .tradingSession(tradingSessionEnum)
                .securityId(securityId);

        return encodeBuffer;
    }
}
