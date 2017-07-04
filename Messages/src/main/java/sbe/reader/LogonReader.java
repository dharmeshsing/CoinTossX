package sbe.reader;

import sbe.msg.LogonDecoder;
import sbe.msg.MessageHeaderDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class LogonReader {
    private StringBuilder sb;
    private int bufferIndex;
    private LogonDecoder logon;
    private MessageHeaderDecoder messageHeader;
    private byte[] password;
    private byte[] newPassword;

    public LogonReader(){
        sb = new StringBuilder();
        bufferIndex = 0;
        logon = new LogonDecoder();
        messageHeader = new MessageHeaderDecoder();
        password = new byte[LogonDecoder.passwordLength()];
        newPassword = new byte[LogonDecoder.newPasswordLength()];
    }

    public StringBuilder read(DirectBuffer buffer) throws UnsupportedEncodingException {
        sb.delete(0, sb.capacity());
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        logon.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        sb.append("CompID=" + logon.compID());
        sb.append("Password=" + new String(password, 0, logon.getPassword(password, 0), LogonDecoder.passwordCharacterEncoding()));
        sb.append("NewPassword=" + new String(newPassword, 0, logon.getNewPassword(newPassword, 0), LogonDecoder.newPasswordCharacterEncoding()));

        return sb;
    }
}
