package sbe.reader;

import sbe.msg.LogoutDecoder;
import sbe.msg.MessageHeaderDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

public class LogoutReader {
    private StringBuilder sb;
    private int bufferIndex;
    private LogoutDecoder logout;
    private MessageHeaderDecoder messageHeader;
    private byte[] reason;

    public LogoutReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        logout = new LogoutDecoder();
        messageHeader = new MessageHeaderDecoder();
        reason = new byte[LogoutDecoder.reasonLength()];
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        logout.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("Reason=" + new String(reason, 0, logout.getReason(reason, 0), LogoutDecoder.reasonCharacterEncoding()));

        return sb;
    }
}
