package sbe.builder.marketData;

import sbe.msg.marketData.MessageHeaderEncoder;
import sbe.msg.marketData.MessageTypeEnum;
import sbe.msg.marketData.OrderDeletedEncoder;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class OrderDeletedBuilder {
    private int bufferIndex;
    private OrderDeletedEncoder orderDeleted;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private MessageTypeEnum messageType;
    private long nanosecond;
    private long orderId;

    public static int BUFFER_SIZE = 39;

    public OrderDeletedBuilder(){
        orderDeleted = new OrderDeletedEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));
    }

    public OrderDeletedBuilder messageType(MessageTypeEnum value){
        this.messageType = value;
        return this;
    }

    public OrderDeletedBuilder nanosecond(long value){
        this.nanosecond = value;
        return this;
    }

    public OrderDeletedBuilder orderId(long value){
        this.orderId = value;
        return this;
    }

    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(orderDeleted.sbeBlockLength())
                .templateId(orderDeleted.sbeTemplateId())
                .schemaId(orderDeleted.sbeSchemaId())
                .schemaId(orderDeleted.sbeSchemaId())
                .version(orderDeleted.sbeSchemaVersion());

        bufferIndex += messageHeader.encodedLength();
        orderDeleted.wrap(encodeBuffer, bufferIndex);

        orderDeleted.messageType(messageType)
                .nanosecond(nanosecond)
                .orderId(orderId);

        return encodeBuffer;
    }

}
