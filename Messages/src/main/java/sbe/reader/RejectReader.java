package sbe.reader;

import sbe.msg.MessageHeaderDecoder;
import sbe.msg.RejectDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class RejectReader {
    private StringBuilder sb;
    private int bufferIndex;
    private RejectDecoder reject;
    private MessageHeaderDecoder messageHeader;
    private byte[] rejectReason;
    private byte[] clientOrderId;

    public RejectReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        reject = new RejectDecoder();
        messageHeader = new MessageHeaderDecoder();
        rejectReason = new byte[RejectDecoder.rejectReasonLength()];
        clientOrderId = new byte[RejectDecoder.clientOrderIdLength()];
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        reject.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("RejectCode=" + reject.rejectCode());
        sb.append("MessageType=" + reject.messageType());
        sb.append("RejectReason=" + new String(rejectReason, 0, reject.getRejectReason(rejectReason, 0), RejectDecoder.rejectReasonCharacterEncoding()));
        sb.append("ClientOrderId=" + new String(clientOrderId, 0, reject.getClientOrderId(clientOrderId, 0), RejectDecoder.clientOrderIdCharacterEncoding()));

        return sb;
    }
}
