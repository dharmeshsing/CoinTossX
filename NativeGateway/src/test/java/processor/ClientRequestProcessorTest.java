package processor;

import aeron.AeronPublisher;
import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import common.MessageGenerator;
import gateway.incoming.Client;
import uk.co.real_logic.agrona.DirectBuffer;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Created by dharmeshsing on 9/08/15.
 */
public class ClientRequestProcessorTest {

    private ClientRequestProcessor requestProcessor;
    private AeronPublisher matchingEnginePub;
    private AeronPublisher clientPublisher;
    private Client client;

    @Before
    public void setup(){
        IntObjectMap clientMap = mock(IntObjectHashMap.class);
        client = new Client();
        client.setLoggedIn(true);
        client.setCompID(1);
        client.setNgOutputURL("udp://localhost:40123");
        client.setNgOutputStreamId(10);
        clientMap.put(1, client);
        when(clientMap.get(anyInt())).thenReturn(client);

        clientPublisher = mock(AeronPublisher.class);
        matchingEnginePub = mock(AeronPublisher.class);
        requestProcessor = new ClientRequestProcessor(clientPublisher,matchingEnginePub,clientMap);
    }

    @Test
    public void testLogonOnRequest() throws Exception {
        client.setLoggedIn(false);
        DirectBuffer db = MessageGenerator.buildLogonRequest();
        requestProcessor.process(db);

        verify(clientPublisher, times(1)).send(any(DirectBuffer.class),anyString(),anyInt());
    }

    @Test
    public void testOrderCancelRequest() throws Exception {
        DirectBuffer db = MessageGenerator.buildOrderCancelRequest();
        requestProcessor.process(db);

        verify(matchingEnginePub, times(1)).send(db);
    }

    @Test
    public void testOrderCancelReplaceRequest() throws Exception {
        DirectBuffer db = MessageGenerator.buildOrderCancelReplaceRequest();
        requestProcessor.process(db);

        verify(matchingEnginePub, times(1)).send(db);
    }
}