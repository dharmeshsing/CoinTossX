package sbe.reader;

import sbe.msg.LogonResponseDecoder;
import sbe.msg.MessageHeaderDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class LogonResponseReader {
    private StringBuilder sb;
    private int bufferIndex;
    private LogonResponseDecoder logonResponse;
    private MessageHeaderDecoder messageHeader;

    public LogonResponseReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        messageHeader = new MessageHeaderDecoder();
        logonResponse = new LogonResponseDecoder();
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        logonResponse.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("RejectCode=" + logonResponse.rejectCode().value());
        sb.append("PasswordExpiry=" + logonResponse.passwordExpiry());

        return sb;
    }
}
