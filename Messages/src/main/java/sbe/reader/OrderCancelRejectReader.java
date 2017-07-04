package sbe.reader;

import sbe.msg.MessageHeaderDecoder;
import sbe.msg.OrderCancelRejectDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class OrderCancelRejectReader {
    private StringBuilder sb;
    private int bufferIndex;
    private OrderCancelRejectDecoder orderCancelReject;
    private MessageHeaderDecoder messageHeader;
    private byte[] clientOrderId;

    public OrderCancelRejectReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        orderCancelReject = new OrderCancelRejectDecoder();
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

        orderCancelReject.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("PartitionId=" + orderCancelReject.partitionId());
        sb.append("SequenceNumber=" + orderCancelReject.sequenceNumber());
        sb.append("ClientOrderId=" + new String(clientOrderId, 0, orderCancelReject.getClientOrderId(clientOrderId, 0), OrderCancelRejectDecoder.clientOrderIdCharacterEncoding()));
        sb.append("OrderId=" + orderCancelReject.orderId());
        sb.append("TransactTime=" + orderCancelReject.transactTime());
        sb.append("RejectCode=" + orderCancelReject.rejectCode());
        sb.append("OrderBook=" + orderCancelReject.orderBook());

        return sb;
    }
}
