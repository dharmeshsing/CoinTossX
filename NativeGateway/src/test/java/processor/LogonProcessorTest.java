package processor;

import common.MessageGenerator;
import gateway.incoming.Client;
import uk.co.real_logic.agrona.DirectBuffer;
import org.junit.Before;
import org.junit.Test;
import sbe.builder.LogonBuilder;
import sbe.msg.LogonDecoder;
import sbe.msg.MessageHeaderDecoder;
import sbe.reader.LogonResponseReader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

/**
 * Created by dharmeshsing on 2/09/15.
 */
public class LogonProcessorTest {

    private LogonProcessor logonProcessor;

    @Before
    public void setup(){
        logonProcessor = new LogonProcessor();
    }

    @Test
    public void testProcessClientAlreadyLoggedIn() throws Exception {
        Client client = new Client();
        client.setLoggedIn(true);
        client.setCompID(1);
        DirectBuffer response = logonProcessor.process(any(LogonDecoder.class),
                               any(MessageHeaderDecoder.class),
                               any(DirectBuffer.class),
                               client,
                               true);

        LogonResponseReader logonResponseReader = new LogonResponseReader();
        StringBuilder sb = logonResponseReader.read(response);
        assertEquals("RejectCode=9903PasswordExpiry=-1",sb.toString());
    }

    @Test
    public void testClientLogin() throws Exception {
        DirectBuffer logonRequest = MessageGenerator.buildLogonRequest();
        LogonDecoder logonDecoder = new LogonDecoder();
        MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
        messageHeaderDecoder.wrap(logonRequest,0);

        Client client = new Client();
        client.setLoggedIn(false);
        client.setCompID(1);
        client.setNgOutputURL("udp://localhost:40123");
        client.setNgOutputStreamId(10);

        DirectBuffer response = logonProcessor.process(logonDecoder,messageHeaderDecoder,logonRequest,client,false);

        LogonResponseReader logonResponseReader = new LogonResponseReader();
        StringBuilder sb = logonResponseReader.read(response);
        assertEquals("RejectCode=0PasswordExpiry=-1",sb.toString());
    }

    @Test
    public void testClientUserNameIncorrectLogin() throws Exception {
        LogonBuilder logonBuilder = new LogonBuilder();
        DirectBuffer logonRequest = logonBuilder.compID(0)
                .password("password12".getBytes())
                .newPassword("1234567890".getBytes())
                .build();

        LogonDecoder logonDecoder = new LogonDecoder();
        MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
        messageHeaderDecoder.wrap(logonRequest,0);

        Client client = new Client();
        client.setLoggedIn(false);
        client.setCompID(1);
        client.setNgOutputURL("udp://localhost:40123");
        client.setNgOutputStreamId(10);

        DirectBuffer response = logonProcessor.process(logonDecoder,messageHeaderDecoder,logonRequest,client,false);

        LogonResponseReader logonResponseReader = new LogonResponseReader();
        StringBuilder sb = logonResponseReader.read(response);
        assertEquals("RejectCode=1PasswordExpiry=-1",sb.toString());
    }

}