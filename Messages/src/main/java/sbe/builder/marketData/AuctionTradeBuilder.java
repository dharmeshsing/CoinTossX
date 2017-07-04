package sbe.builder.marketData;

import sbe.msg.marketData.AuctionTradeEncoder;
import sbe.msg.marketData.MessageHeaderEncoder;
import sbe.msg.marketData.MessageTypeEnum;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class AuctionTradeBuilder {
    private int bufferIndex;
    private AuctionTradeEncoder auctionTrade;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private MessageTypeEnum messageType;
    private long nanosecond;
    private int executedQuantity;
    private int instrumentId;
    private int tradeId;
    private long price;

    public static int BUFFER_SIZE = 42;

    public AuctionTradeBuilder(){
        auctionTrade = new AuctionTradeEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));
    }

    public AuctionTradeBuilder messageType(MessageTypeEnum value){
        this.messageType = value;
        return this;
    }

    public AuctionTradeBuilder nanosecond(long value){
        this.nanosecond = value;
        return this;
    }

    public AuctionTradeBuilder executedQuantity(int value){
        this.executedQuantity = value;
        return this;
    }

    public AuctionTradeBuilder tradeId(int value){
        this.tradeId = value;
        return this;
    }

    public AuctionTradeBuilder instrumentId(int value){
        this.instrumentId = value;
        return this;
    }

    public AuctionTradeBuilder price(int value){
        this.price = value;
        return this;
    }


    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(auctionTrade.sbeBlockLength())
                .templateId(auctionTrade.sbeTemplateId())
                .schemaId(auctionTrade.sbeSchemaId())
                .schemaId(auctionTrade.sbeSchemaId())
                .version(auctionTrade.sbeSchemaVersion());

        bufferIndex += messageHeader.encodedLength();
        auctionTrade.wrap(encodeBuffer, bufferIndex);

        auctionTrade.messageType(messageType)
                .nanosecond(nanosecond)
                .executedQuantity(executedQuantity)
                .instrumentId(instrumentId)
                .tradeId(tradeId);

        auctionTrade.price().mantissa(price);


        return encodeBuffer;
    }

}
