package sbe.builder;

import sbe.msg.MessageHeaderEncoder;
import sbe.msg.SystemStatusEncoder;
import sbe.msg.SystemStatusEnum;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class SystemStatusBuilder {
    private int bufferIndex;
    private SystemStatusEncoder systemStatus;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int compID;
    private SystemStatusEnum status;

    public static int BUFFER_SIZE = 106;

    public SystemStatusBuilder(){
        systemStatus = new SystemStatusEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));
    }

    public SystemStatusBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public SystemStatusBuilder status(SystemStatusEnum value){
        this.status = value;
        return this;
    }

    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(systemStatus.sbeBlockLength())
                .templateId(systemStatus.sbeTemplateId())
                .schemaId(systemStatus.sbeSchemaId())
                .version(systemStatus.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        systemStatus.wrap(encodeBuffer, bufferIndex)
                .status(status);

        return encodeBuffer;
    }

}
