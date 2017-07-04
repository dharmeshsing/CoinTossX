package processor;

import aeron.AeronPublisher;
import aeron.AeronSubscriber;
import com.carrotsearch.hppc.IntObjectMap;
import gateway.incoming.Client;
import sbe.msg.MessageHeaderDecoder;
import uk.co.real_logic.aeron.logbuffer.FragmentHandler;
import uk.co.real_logic.aeron.logbuffer.Header;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

/**
 * Created by dharmeshsing on 10/08/15.
 */
public class MEProcessor implements Runnable, FragmentHandler{
    private String mediaDriverConextDir;
    private String url;
    private int streamId;
    private AeronPublisher clientPublisher;
    private IntObjectMap<Client> clients;
    private AeronSubscriber matchingEngineSubscriber;
    private UnsafeBuffer temp = new UnsafeBuffer(ByteBuffer.allocate(106));

    private MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();

    public MEProcessor(String mediaDriverConextDir,String url,int streamId, AeronPublisher clientPublisher,IntObjectMap<Client> clients){
        this.mediaDriverConextDir = mediaDriverConextDir;
        this.url = url;
        this.streamId = streamId;
        this.clientPublisher = clientPublisher;
        this.clients = clients;
    }

    public void stop(){
        matchingEngineSubscriber.stop();
    }

    @Override
    public void run() {
        matchingEngineSubscriber = new AeronSubscriber(mediaDriverConextDir,this);
        matchingEngineSubscriber.addSubscriber(url,streamId);
        matchingEngineSubscriber.start();
    }

    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
        temp.wrap(buffer,offset,length);
        messageHeaderDecoder.wrap(temp, 0);
        Client client = clients.get(messageHeaderDecoder.compID());
        if(client != null) {
            clientPublisher.send(temp, client.getNgOutputURL(), client.getNgOutputStreamId());
        }
    }
}
