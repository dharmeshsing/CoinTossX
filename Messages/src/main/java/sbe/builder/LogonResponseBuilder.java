package sbe.builder;

import sbe.msg.LogonResponseEncoder;
import sbe.msg.MessageHeaderEncoder;
import sbe.msg.RejectCode;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class LogonResponseBuilder {
    private int bufferIndex;
    private LogonResponseEncoder logonResponse;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int compID;
    private RejectCode rejectCode;
    private int passwordExpiry;

    public static int BUFFER_SIZE = 106;

    public LogonResponseBuilder(){
        logonResponse = new LogonResponseEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));
    }

    public LogonResponseBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public LogonResponseBuilder rejectCode(RejectCode value){
        this.rejectCode = value;
        return this;
    }

    public LogonResponseBuilder passwordExpiry(int value){
        this.passwordExpiry = value;
        return this;
    }


    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(logonResponse.sbeBlockLength())
                .templateId(logonResponse.sbeTemplateId())
                .schemaId(logonResponse.sbeSchemaId())
                .schemaId(logonResponse.sbeSchemaId())
                .version(logonResponse.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        logonResponse.wrap(encodeBuffer, bufferIndex);

        logonResponse.rejectCode(rejectCode)
                     .passwordExpiry(passwordExpiry);

        return encodeBuffer;
    }

}
