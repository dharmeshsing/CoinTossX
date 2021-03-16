package sbe.reader;

import sbe.msg.MessageHeaderDecoder;
import sbe.msg.OrderCancelRejectDecoder;
import sbe.msg.OrderMassCancelReportDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

public class OrderMassCancelReportReader {
    private StringBuilder sb;
    private int bufferIndex;
    private OrderMassCancelReportDecoder orderMassCancelReport;
    private MessageHeaderDecoder messageHeader;
    private byte[] clientOrderId;

    public OrderMassCancelReportReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        orderMassCancelReport = new OrderMassCancelReportDecoder();
        messageHeader = new MessageHeaderDecoder();

        clientOrderId = new byte[OrderCancelRejectDecoder.clientOrderIdLength()];
    }


    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        orderMassCancelReport.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("PartitionId=" + orderMassCancelReport.partitionId());
        sb.append("SequenceNumber=" + orderMassCancelReport.sequenceNumber());
        sb.append("ClientOrderId=" + new String(clientOrderId, 0, orderMassCancelReport.getClientOrderId(clientOrderId, 0), OrderMassCancelReportDecoder.clientOrderIdCharacterEncoding()));
        sb.append("Status=" + orderMassCancelReport.status());
        sb.append("TransactTime=" + orderMassCancelReport.transactTime());
        sb.append("RejectCode=" + orderMassCancelReport.rejectCode());
        sb.append("OrderBook=" + orderMassCancelReport.orderBook());

        return sb;
    }
}
