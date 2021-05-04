package sbe.builder;

import sbe.msg.MessageHeaderEncoder;
import sbe.msg.MissedMessageRequestEncoder;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class MissedMessageRequestBuilder {
    private int bufferIndex;
    private MissedMessageRequestEncoder missedMessageRequest;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int compID;
    private short partitionId;
    private int sequenceNumber;

    public static int BUFFER_SIZE = 106;

    public MissedMessageRequestBuilder(){
        missedMessageRequest = new MissedMessageRequestEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));
    }

    public MissedMessageRequestBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public MissedMessageRequestBuilder partitionId(short value){
        this.partitionId = value;
        return this;
    }

    public MissedMessageRequestBuilder sequenceNumber(int value){
        this.sequenceNumber = value;
        return this;
    }

    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(missedMessageRequest.sbeBlockLength())
                .templateId(missedMessageRequest.sbeTemplateId())
                .schemaId(missedMessageRequest.sbeSchemaId())
                .version(missedMessageRequest.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        missedMessageRequest.wrap(encodeBuffer, bufferIndex)
                .partitionId(partitionId)
                .sequenceNumber(sequenceNumber);

        return encodeBuffer;
    }

}
