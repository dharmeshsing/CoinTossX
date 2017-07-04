package sbe.builder;

import sbe.msg.MessageHeaderEncoder;
import sbe.msg.OrderBookEnum;
import sbe.msg.OrderCancelRejectEncoder;
import sbe.msg.RejectCode;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class OrderCancelRejectBuilder {
    private int bufferIndex;
    private OrderCancelRejectEncoder orderCancelReject;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int compID;
    private short partitionId;
    private int sequenceNumber;
    private UnsafeBuffer clientOrderId;
    private int orderId;
    private long transactTime;
    private RejectCode rejectCode;
    private OrderBookEnum orderBook;

    public static int BUFFER_SIZE = 106;

    public OrderCancelRejectBuilder(){
        orderCancelReject = new OrderCancelRejectEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));

        clientOrderId = new UnsafeBuffer(ByteBuffer.allocateDirect(OrderCancelRejectEncoder.clientOrderIdLength()));
    }

    public OrderCancelRejectBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public OrderCancelRejectBuilder partitionId(short value){
        this.partitionId = value;
        return this;
    }

    public OrderCancelRejectBuilder sequenceNumber(int value){
        this.sequenceNumber = value;
        return this;
    }

    public OrderCancelRejectBuilder clientOrderId(byte[] value){
        this.clientOrderId.wrap(value);
        return this;
    }

    public OrderCancelRejectBuilder orderId(int value){
        this.orderId = value;
        return this;
    }

    public OrderCancelRejectBuilder transactTime(long value){
        this.transactTime = value;
        return this;
    }

    public OrderCancelRejectBuilder rejectCode(RejectCode value){
        this.rejectCode = value;
        return this;
    }

    public OrderCancelRejectBuilder orderBook(OrderBookEnum value){
        this.orderBook = value;
        return this;
    }

    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(orderCancelReject.sbeBlockLength())
                .templateId(orderCancelReject.sbeTemplateId())
                .schemaId(orderCancelReject.sbeSchemaId())
                .version(orderCancelReject.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        orderCancelReject.wrap(encodeBuffer, bufferIndex)
                .partitionId(partitionId)
                .sequenceNumber(sequenceNumber)
                .putClientOrderId(clientOrderId.byteArray(),0)
                .orderId(orderId)
                .transactTime(transactTime)
                .rejectCode(rejectCode)
                .orderBook(orderBook);

        return encodeBuffer;
    }

}
