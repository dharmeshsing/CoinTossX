package sbe.builder.marketData;

import sbe.msg.marketData.MessageHeaderEncoder;
import sbe.msg.marketData.MessageTypeEnum;
import sbe.msg.marketData.OrderExecutedEncoder;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class OrderExecutedBuilder {
    private int bufferIndex;
    private OrderExecutedEncoder orderExecuted;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private MessageTypeEnum messageType;
    private long nanosecond;
    private long orderId;
    private int executedQuantity;
    private int tradeId;

    public static int BUFFER_SIZE = 39;

    public OrderExecutedBuilder(){
        orderExecuted = new OrderExecutedEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));
    }

    public OrderExecutedBuilder messageType(MessageTypeEnum value){
        this.messageType = value;
        return this;
    }

    public OrderExecutedBuilder nanosecond(long value){
        this.nanosecond = value;
        return this;
    }

    public OrderExecutedBuilder orderId(long value){
        this.orderId = value;
        return this;
    }

    public OrderExecutedBuilder executedQuantity(int value){
        this.executedQuantity = value;
        return this;
    }

    public OrderExecutedBuilder tradeId(int value){
        this.tradeId = value;
        return this;
    }


    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(orderExecuted.sbeBlockLength())
                .templateId(orderExecuted.sbeTemplateId())
                .schemaId(orderExecuted.sbeSchemaId())
                .schemaId(orderExecuted.sbeSchemaId())
                .version(orderExecuted.sbeSchemaVersion());

        bufferIndex += messageHeader.encodedLength();
        orderExecuted.wrap(encodeBuffer, bufferIndex);

        orderExecuted.messageType(messageType)
                .nanosecond(nanosecond)
                .orderId(orderId)
                .executedQuantity(executedQuantity)
                .tradeId(tradeId);

        return encodeBuffer;
    }

}
