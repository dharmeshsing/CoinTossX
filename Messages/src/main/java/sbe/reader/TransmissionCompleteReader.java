package sbe.reader;

import sbe.msg.MessageHeaderDecoder;
import sbe.msg.TransmissionCompleteDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class TransmissionCompleteReader {
    private StringBuilder sb;
    private int bufferIndex;
    private TransmissionCompleteDecoder transmissionComplete;
    private MessageHeaderDecoder messageHeader;

    public TransmissionCompleteReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        transmissionComplete = new TransmissionCompleteDecoder();
        messageHeader = new MessageHeaderDecoder();
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        transmissionComplete.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("Status=" + transmissionComplete.status());

        return sb;
    }
}
