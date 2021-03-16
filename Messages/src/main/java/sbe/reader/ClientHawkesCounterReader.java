package sbe.reader;

import sbe.msg.ClientHawkesCounterDecoder;
import sbe.msg.MessageHeaderDecoder;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

public class ClientHawkesCounterReader {
    private int bufferIndex;
    private ClientHawkesCounterDecoder clientHawkesCounter;
    private MessageHeaderDecoder messageHeader;
    private int clientId;
    private int max;
    private int complete;

    public ClientHawkesCounterReader(){
        bufferIndex = 0;
        clientHawkesCounter = new ClientHawkesCounterDecoder();
        messageHeader = new MessageHeaderDecoder();
    }

    public void read(DirectBuffer buffer) throws UnsupportedEncodingException {
        bufferIndex = 0;
        messageHeader = messageHeader.wrap(buffer, bufferIndex);

        int actingBlockLength = messageHeader.blockLength();
        int actingVersion = messageHeader.version();
        bufferIndex += messageHeader.encodedLength();

        clientHawkesCounter.wrap(buffer, bufferIndex, actingBlockLength, actingVersion);

        clientId = clientHawkesCounter.clientId();
        max = clientHawkesCounter.max();
        complete = clientHawkesCounter.complete();
    }

    public int getClientId() {
        return clientId;
    }

    public int getMax() {
        return max;
    }

    public int getComplete() {
        return complete;
    }
}
