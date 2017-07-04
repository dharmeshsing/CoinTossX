package sbe.reader;

import sbe.msg.BusinessRejectDecoder;
import sbe.msg.MessageHeaderDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class BusinessRejectReader {
    private StringBuilder sb;
    private int bufferIndex;
    private BusinessRejectDecoder businessReject;
    private MessageHeaderDecoder messageHeader;
    private byte[] clientOrderId;

    public BusinessRejectReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        messageHeader = new MessageHeaderDecoder();
        businessReject = new BusinessRejectDecoder();
        clientOrderId = new byte[BusinessRejectDecoder.clientOrderIdLength()];
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        businessReject.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("PartitionId=" + businessReject.partitionId());
        sb.append("SequenceNumber=" + businessReject.sequenceNumber());
        sb.append("RejectCode=" + businessReject.rejectCode());
        sb.append("ClientOrderId=" + new String(clientOrderId, 0, businessReject.getClientOrderId(clientOrderId, 0), BusinessRejectDecoder.clientOrderIdCharacterEncoding()));
        sb.append("OrderId=" + businessReject.orderId());
        sb.append("TransactTime=" + businessReject.transactTime());

        return sb;
    }
}
