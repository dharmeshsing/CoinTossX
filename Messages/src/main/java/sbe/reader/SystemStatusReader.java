package sbe.reader;

import sbe.msg.MessageHeaderDecoder;
import sbe.msg.SystemStatusDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class SystemStatusReader {
    private StringBuilder sb;
    private int bufferIndex;
    private SystemStatusDecoder systemStatus;
    private MessageHeaderDecoder messageHeader;

    public SystemStatusReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        systemStatus = new SystemStatusDecoder();
        messageHeader = new MessageHeaderDecoder();
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        systemStatus.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("Status=" + systemStatus.status());

        return sb;
    }
}
