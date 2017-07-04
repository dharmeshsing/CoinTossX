package sbe.builder;

import sbe.msg.LogonEncoder;
import sbe.msg.MessageHeaderEncoder;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class LogonBuilder {
    private int bufferIndex;
    private LogonEncoder logon;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int compID;
    private UnsafeBuffer password;
    private UnsafeBuffer newPassword;

    public static int BUFFER_SIZE = 106;

    public LogonBuilder(){
        logon = new LogonEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));

        password = new UnsafeBuffer(ByteBuffer.allocateDirect(LogonEncoder.passwordLength()));
        newPassword = new UnsafeBuffer(ByteBuffer.allocateDirect(LogonEncoder.newPasswordLength()));
    }

    public LogonBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public LogonBuilder password(byte[] value){
        this.password.wrap(value);
        return this;
    }

    public LogonBuilder newPassword(byte[] value){
        this.newPassword.wrap(value);
        return this;
    }

    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(logon.sbeBlockLength())
                .templateId(logon.sbeTemplateId())
                .schemaId(logon.sbeSchemaId())
                .version(logon.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        logon.wrap(encodeBuffer, bufferIndex);

        logon.compID(compID);
        logon.putPassword(password.byteArray(), 0);
        logon.putNewPassword(newPassword.byteArray(), 0);

        return encodeBuffer;
    }

}
