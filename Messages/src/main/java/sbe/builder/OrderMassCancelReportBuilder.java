package sbe.builder;

import sbe.msg.*;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class OrderMassCancelReportBuilder {
    private int bufferIndex;
    private OrderMassCancelReportEncoder orderMassCancelReport;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int compID;
    private short partitionId;
    private int sequenceNumber;
    private UnsafeBuffer clientOrderId;
    private OrderMassCancelReportStatusEnum status;
    private long transactTime;
    private RejectCode rejectCode;
    private OrderBookEnum orderBook;

    public static int BUFFER_SIZE = 106;

    public OrderMassCancelReportBuilder(){
        orderMassCancelReport = new OrderMassCancelReportEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));

        clientOrderId = new UnsafeBuffer(ByteBuffer.allocateDirect(OrderCancelRejectEncoder.clientOrderIdLength()));
    }

    public OrderMassCancelReportBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public OrderMassCancelReportBuilder partitionId(short value){
        this.partitionId = value;
        return this;
    }

    public OrderMassCancelReportBuilder sequenceNumber(int value){
        this.sequenceNumber = value;
        return this;
    }

    public OrderMassCancelReportBuilder clientOrderId(byte[] value){
        this.clientOrderId.wrap(value);
        return this;
    }

    public OrderMassCancelReportBuilder status(OrderMassCancelReportStatusEnum value){
        this.status = value;
        return this;
    }

    public OrderMassCancelReportBuilder transactTime(long value){
        this.transactTime = value;
        return this;
    }

    public OrderMassCancelReportBuilder rejectCode(RejectCode value){
        this.rejectCode = value;
        return this;
    }

    public OrderMassCancelReportBuilder orderBook(OrderBookEnum value){
        this.orderBook = value;
        return this;
    }

    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(orderMassCancelReport.sbeBlockLength())
                .templateId(orderMassCancelReport.sbeTemplateId())
                .schemaId(orderMassCancelReport.sbeSchemaId())
                .version(orderMassCancelReport.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        orderMassCancelReport.wrap(encodeBuffer, bufferIndex)
                .partitionId(partitionId)
                .sequenceNumber(sequenceNumber)
                .putClientOrderId(clientOrderId.byteArray(),0)
                .status(status)
                .transactTime(transactTime)
                .rejectCode(rejectCode)
                .orderBook(orderBook);

        return encodeBuffer;
    }

}
