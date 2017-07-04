package sbe.builder.marketData;

import sbe.msg.marketData.MessageHeaderEncoder;
import sbe.msg.marketData.MessageTypeEnum;
import sbe.msg.marketData.TradeEncoder;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class TradeBuilder {
    private int bufferIndex;
    private TradeEncoder trade;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private MessageTypeEnum messageType;
    private long nanosecond;
    private int executedQuantity;
    private int instrumentId;
    private int tradeId;
    private long price;

    public static int BUFFER_SIZE = 42;

    public TradeBuilder(){
        trade = new TradeEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));
    }

    public TradeBuilder messageType(MessageTypeEnum value){
        this.messageType = value;
        return this;
    }

    public TradeBuilder nanosecond(long value){
        this.nanosecond = value;
        return this;
    }

    public TradeBuilder executedQuantity(int value){
        this.executedQuantity = value;
        return this;
    }

    public TradeBuilder tradeId(int value){
        this.tradeId = value;
        return this;
    }

    public TradeBuilder instrumentId(int value){
        this.instrumentId = value;
        return this;
    }

    public TradeBuilder price(int value){
        this.price = value;
        return this;
    }


    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(trade.sbeBlockLength())
                .templateId(trade.sbeTemplateId())
                .schemaId(trade.sbeSchemaId())
                .schemaId(trade.sbeSchemaId())
                .version(trade.sbeSchemaVersion());

        bufferIndex += messageHeader.encodedLength();
        trade.wrap(encodeBuffer, bufferIndex);

        trade.messageType(messageType)
                .nanosecond(nanosecond)
                .executedQuantity(executedQuantity)
                .instrumentId(instrumentId)
                .tradeId(tradeId);

        trade.price().mantissa(price);


        return encodeBuffer;
    }

}
