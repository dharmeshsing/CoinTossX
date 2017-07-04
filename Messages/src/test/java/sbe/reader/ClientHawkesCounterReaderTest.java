package sbe.reader;

import org.junit.Test;
import sbe.builder.ClientHawkesCounterBuilder;
import uk.co.real_logic.agrona.DirectBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 25/04/16.
 */
public class ClientHawkesCounterReaderTest {

    @Test
    public void testRead() throws Exception {
        ClientHawkesCounterReader clientHawkesCounterReader = new ClientHawkesCounterReader();
        DirectBuffer buffer = build();

        clientHawkesCounterReader.read(buffer);
        assertEquals(1, clientHawkesCounterReader.getClientId());
        assertEquals(200000, clientHawkesCounterReader.getMax());
        assertEquals(50, clientHawkesCounterReader.getComplete());
    }

    private DirectBuffer build(){
        ClientHawkesCounterBuilder clientHawkesCounterBuilder = new ClientHawkesCounterBuilder();
        clientHawkesCounterBuilder.clientId(1).max(200000).complete(50);

        return clientHawkesCounterBuilder.build();

    }

}