package processor;

import common.MessageGenerator;
import gateway.incoming.Client;
import uk.co.real_logic.agrona.DirectBuffer;
import org.openjdk.jmh.annotations.*;
import sbe.msg.LogonDecoder;
import sbe.msg.MessageHeaderDecoder;

/**
 * Created by dharmeshsing on 4/09/15.
 */
@State(Scope.Thread)
public class LogonProcessorPerfTest {

    private LogonProcessor logonProcessor;
    private LogonDecoder logonDecoder;
    private MessageHeaderDecoder messageHeaderDecoder;
    private DirectBuffer logonRequest;
    private Client client;

    @Setup(Level.Trial)
    public void setup(){
        logonProcessor = new LogonProcessor();
        logonRequest = MessageGenerator.buildLogonRequest();
        logonDecoder = new LogonDecoder();
        messageHeaderDecoder = new MessageHeaderDecoder();
        messageHeaderDecoder.wrap(logonRequest,0);

        client = new Client();
        client.setLoggedIn(false);
        client.setCompID(1);
        client.setNgOutputURL("udp://localhost:40123");
        client.setNgOutputStreamId(10);
    }


    @Benchmark
    public DirectBuffer testValidate() throws Exception {
        return logonProcessor.process(logonDecoder, messageHeaderDecoder, logonRequest, client, false);
    }
}
