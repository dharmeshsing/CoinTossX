package sbe.builder.marketData;

import sbe.msg.marketData.Flags;
import sbe.msg.marketData.MessageHeaderEncoder;
import sbe.msg.marketData.MessageTypeEnum;
import sbe.msg.marketData.OrderModifiedEncoder;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class OrderModifiedBuilder {
    private int bufferIndex;
    private OrderModifiedEncoder orderModified;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private MessageTypeEnum messageType;
    private long nanosecond;
    private long orderId;

    private int newQuantity;
    private long newPrice;
    private Flags flags;

    public static int BUFFER_SIZE = 39;

    public OrderModifiedBuilder(){
        orderModified = new OrderModifiedEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));
    }

    public OrderModifiedBuilder messageType(MessageTypeEnum value){
        this.messageType = value;
        return this;
    }

    public OrderModifiedBuilder nanosecond(long value){
        this.nanosecond = value;
        return this;
    }

    public OrderModifiedBuilder orderId(long value){
        this.orderId = value;
        return this;
    }

    public OrderModifiedBuilder newQuantity(int value){
        this.newQuantity = value;
        return this;
    }

    public OrderModifiedBuilder newPrice(long value){
        this.newPrice = value;
        return this;
    }

    public OrderModifiedBuilder flags(Flags value){
        this.flags = value;
        return this;
    }

    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(orderModified.sbeBlockLength())
                .templateId(orderModified.sbeTemplateId())
                .schemaId(orderModified.sbeSchemaId())
                .schemaId(orderModified.sbeSchemaId())
                .version(orderModified.sbeSchemaVersion());

        bufferIndex += messageHeader.encodedLength();
        orderModified.wrap(encodeBuffer, bufferIndex);

        orderModified.messageType(messageType)
                .nanosecond(nanosecond)
                .orderId(orderId)
                .newQuantity(newQuantity);

        orderModified.newPrice().mantissa(newPrice);
        orderModified.flags(flags);

        return encodeBuffer;
    }

}
