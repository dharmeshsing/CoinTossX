package processor;

import aeron.AeronPublisher;
import com.carrotsearch.hppc.IntObjectMap;
import gateway.incoming.Client;
import gateway.incoming.NativeGateway;
import sbe.msg.*;
import sbe.reader.AdminReader;
import uk.co.real_logic.agrona.DirectBuffer;

import java.io.UnsupportedEncodingException;

public class ClientRequestProcessor {
    private MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    private LogonDecoder logonDecoder = new LogonDecoder();
    private AeronPublisher clientPublisher;
    private AeronPublisher matchingEnginePublisher;
    private IntObjectMap<Client> clients;

    private LogonProcessor logonProcessor;
    private AdminReader adminReader;

    public ClientRequestProcessor(AeronPublisher clientPublisher,AeronPublisher matchingEnginePublisher,IntObjectMap<Client> clients){
        this.clientPublisher = clientPublisher;
        this.matchingEnginePublisher = matchingEnginePublisher;
        this.clients = clients;
        this.logonProcessor = new LogonProcessor();
        adminReader = new AdminReader();
    }

    public void process(DirectBuffer buffer) throws UnsupportedEncodingException {
        messageHeaderDecoder.wrap(buffer, 0);

        Client client = clients.get(messageHeaderDecoder.compID());
        boolean isLoggedIn = client.isLoggedIn();
        boolean sendToMatchingEngine = true;

        DirectBuffer response = null;
        if(messageHeaderDecoder.templateId() == 1) {
            response = logonProcessor.process(logonDecoder, messageHeaderDecoder, buffer, client, isLoggedIn);
            sendToMatchingEngine = false;
        }else if(!isLoggedIn){
            response = logonProcessor.buildResponse(RejectCode.NotLoggedIntoRealTimeChannel,client.getCompID());
            sendToMatchingEngine = false;
        }

        if(sendToMatchingEngine){
            if(messageHeaderDecoder.templateId() == AdminDecoder.TEMPLATE_ID){
                adminReader.read(buffer);
                processAdminMessage(adminReader.getAdminTypeEnum());
            }
            publishToMatchingEngine(buffer);
        }else{
            sendResponseToClient(response,client);
        }
    }

    private void sendResponseToClient(DirectBuffer buffer,Client client){
        clientPublisher.send(buffer, client.getNgOutputURL(), client.getNgOutputStreamId());
    }

    private void publishToMatchingEngine(DirectBuffer buffer){
        matchingEnginePublisher.send(buffer);
    }

    private void processAdminMessage(AdminTypeEnum adminTypeEnum){
        switch(adminTypeEnum){
            case ShutDown:NativeGateway.setRunning(false);break;
            default: return;
        }
    }

}
