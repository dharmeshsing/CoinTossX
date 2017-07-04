package sbe.reader;

import sbe.msg.MessageHeaderDecoder;
import sbe.msg.MissedMessageRequestDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class MissedMessageRequestReader {
    private StringBuilder sb;
    private int bufferIndex;
    private MissedMessageRequestDecoder missedMessage;
    private MessageHeaderDecoder messageHeader;

    public MissedMessageRequestReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        missedMessage = new MissedMessageRequestDecoder();
        messageHeader = new MessageHeaderDecoder();
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        missedMessage.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("PartitionId=" + missedMessage.partitionId());
        sb.append("SequenceNumber=" + missedMessage.sequenceNumber());

        return sb;
    }
}
