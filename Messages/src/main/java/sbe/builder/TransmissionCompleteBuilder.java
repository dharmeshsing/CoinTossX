package sbe.builder;

import sbe.msg.MessageHeaderEncoder;
import sbe.msg.TransmissionCompleteEncoder;
import sbe.msg.TransmissionCompleteStatus;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

/**
 * Created by dharmeshsing on 12/08/15.
 */
public class TransmissionCompleteBuilder {
    private int bufferIndex;
    private TransmissionCompleteEncoder transmissionComplete;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int compID;
    private TransmissionCompleteStatus status;

    public static int BUFFER_SIZE = 106;

    public TransmissionCompleteBuilder(){
        transmissionComplete = new TransmissionCompleteEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));
    }

    public TransmissionCompleteBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public TransmissionCompleteBuilder status(TransmissionCompleteStatus value){
        this.status = value;
        return this;
    }

    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(transmissionComplete.sbeBlockLength())
                .templateId(transmissionComplete.sbeTemplateId())
                .schemaId(transmissionComplete.sbeSchemaId())
                .version(transmissionComplete.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        transmissionComplete.wrap(encodeBuffer, bufferIndex)
                .status(status);

        return encodeBuffer;
    }

}
