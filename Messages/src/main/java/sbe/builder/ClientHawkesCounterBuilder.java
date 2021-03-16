package sbe.builder;

import sbe.msg.ClientHawkesCounterEncoder;
import sbe.msg.MessageHeaderEncoder;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class ClientHawkesCounterBuilder {
    private int bufferIndex;
    private ClientHawkesCounterEncoder clientHawkesCounter;
    private MessageHeaderEncoder messageHeader;
    private UnsafeBuffer encodeBuffer;

    private int clientId;
    private int max;
    private int complete;

    public static int BUFFER_SIZE = 106;

    public ClientHawkesCounterBuilder(){
        clientHawkesCounter = new ClientHawkesCounterEncoder();
        messageHeader = new MessageHeaderEncoder();
        encodeBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(BUFFER_SIZE));
    }

    public ClientHawkesCounterBuilder clientId(int value){
        this.clientId = value;
        return this;
    }

    public ClientHawkesCounterBuilder max(int max){
        this.max = max;
        return this;
    }

    public ClientHawkesCounterBuilder complete(int complete){
        this.complete = complete;
        return this;
    }


    public DirectBuffer build(){
        bufferIndex = 0;
        messageHeader.wrap(encodeBuffer, bufferIndex)
                .blockLength(clientHawkesCounter.sbeBlockLength())
                .templateId(clientHawkesCounter.sbeTemplateId())
                .schemaId(clientHawkesCounter.sbeSchemaId())
                .version(clientHawkesCounter.sbeSchemaVersion())
                .compID(clientId);

        bufferIndex += messageHeader.encodedLength();
        clientHawkesCounter.wrap(encodeBuffer, bufferIndex);

        clientHawkesCounter.clientId(clientId);
        clientHawkesCounter.max(max);
        clientHawkesCounter.complete(complete);

        return encodeBuffer;
    }

}
