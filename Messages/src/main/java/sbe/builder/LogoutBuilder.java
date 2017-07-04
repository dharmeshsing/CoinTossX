package sbe.builder;

import sbe.msg.LogoutEncoder;
import sbe.msg.MessageHeaderEncoder;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class LogoutBuilder {
    private int bufferIndex;
    private LogoutEncoder logout;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int compID;
    private UnsafeBuffer reason;

    public static int BUFFER_SIZE = 106;

    public LogoutBuilder(){
        logout = new LogoutEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));

        reason = new UnsafeBuffer(ByteBuffer.allocateDirect(LogoutEncoder.reasonLength()));
    }

    public LogoutBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public LogoutBuilder reason(byte[] value){
        this.reason.wrap(value);
        return this;
    }

    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(logout.sbeBlockLength())
                .templateId(logout.sbeTemplateId())
                .schemaId(logout.sbeSchemaId())
                .version(logout.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        logout.wrap(encodeBuffer, bufferIndex);

        logout.putReason(reason.byteArray(), 0);

        return encodeBuffer;
    }

}
