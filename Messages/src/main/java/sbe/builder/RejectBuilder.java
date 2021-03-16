package sbe.builder;

import sbe.msg.MessageHeaderEncoder;
import sbe.msg.RejectEncoder;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class RejectBuilder {
    private int bufferIndex;
    private RejectEncoder reject;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int compID;
    private int rejectCode;
    private char messageType;
    private UnsafeBuffer rejectReason;
    private UnsafeBuffer clientOrderId;

    public static int BUFFER_SIZE = 106;

    public RejectBuilder(){
        reject = new RejectEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));

        rejectReason = new UnsafeBuffer(ByteBuffer.allocateDirect(RejectEncoder.rejectReasonLength()));
        clientOrderId = new UnsafeBuffer(ByteBuffer.allocateDirect(RejectEncoder.clientOrderIdLength()));
    }

    public RejectBuilder compID(int value){
        this.compID = value;
        return this;
    }

    public RejectBuilder rejectCode(int value){
        this.rejectCode = value;
        return this;
    }

    public RejectBuilder messageType(char value){
        this.messageType = value;
        return this;
    }

    public RejectBuilder rejectReason(byte[] value){
        this.rejectReason.wrap(value);
        return this;
    }

    public RejectBuilder clientOrderId(byte[] value){
        this.clientOrderId.wrap(value);
        return this;
    }


    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(reject.sbeBlockLength())
                .templateId(reject.sbeTemplateId())
                .schemaId(reject.sbeSchemaId())
                .version(reject.sbeSchemaVersion())
                .compID(compID);

        bufferIndex += messageHeader.encodedLength();
        reject.wrap(encodeBuffer, bufferIndex)
                .rejectCode(rejectCode)
                .messageType((byte)messageType)
                .putRejectReason(rejectReason.byteArray(),0)
                .putClientOrderId(clientOrderId.byteArray(),0);

        return encodeBuffer;
    }

}
