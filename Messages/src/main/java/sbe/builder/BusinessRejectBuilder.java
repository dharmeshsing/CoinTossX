package sbe.builder;

import sbe.msg.BusinessRejectEncoder;
import sbe.msg.BusinessRejectEnum;
import sbe.msg.MessageHeaderEncoder;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class BusinessRejectBuilder {
    private int bufferIndex;
    private BusinessRejectEncoder businessReject;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int compID;
    private short partitionId;
    private int sequenceNumber;
    private BusinessRejectEnum businessRejectEnum;
    private UnsafeBuffer clientOrderId;
    private int orderId;
    private long transactTime;

    public static int BUFFER_SIZE = 256;

    public BusinessRejectBuilder(){
        businessReject = new BusinessRejectEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));

        clientOrderId = new UnsafeBuffer(ByteBuffer.allocateDirect(BusinessRejectEncoder.clientOrderIdLength()));
    }

    public BusinessRejectBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public BusinessRejectBuilder clientOrderId(byte[] value){
        this.clientOrderId.wrap(value);
        return this;
    }

    public BusinessRejectBuilder orderId(int value){
        this.orderId = value;
        return this;
    }

    public BusinessRejectBuilder sequenceNumber(int value){
        this.sequenceNumber = value;
        return this;
    }

    public BusinessRejectBuilder partitionId(short value){
        this.partitionId = value;
        return this;
    }

    public BusinessRejectBuilder businessRejectEnum(BusinessRejectEnum value){
        this.businessRejectEnum = value;
        return this;
    }

    public BusinessRejectBuilder transactTime(long value){
        this.transactTime = value;
        return this;
    }


    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(businessReject.sbeBlockLength())
                .templateId(businessReject.sbeTemplateId())
                .schemaId(businessReject.sbeSchemaId())
                .version(businessReject.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        businessReject.wrap(encodeBuffer, bufferIndex)
                .partitionId(partitionId)
                .sequenceNumber(sequenceNumber)
                .rejectCode(businessRejectEnum)
                .putClientOrderId(clientOrderId.byteArray(),0)
                .orderId(orderId)
                .transactTime(transactTime);

        return encodeBuffer;
    }
}
