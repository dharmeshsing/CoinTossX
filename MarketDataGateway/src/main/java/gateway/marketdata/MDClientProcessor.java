package gateway.marketdata;

import aeron.AeronPublisher;
import aeron.AeronSubscriber;
import com.carrotsearch.hppc.IntObjectMap;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import sbe.msg.MessageHeaderDecoder;
import uk.co.real_logic.aeron.logbuffer.FragmentHandler;
import uk.co.real_logic.aeron.logbuffer.Header;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

public class MDClientProcessor implements Runnable,FragmentHandler{
    private String mediaDriverConextDir;
    private IntObjectMap<Client> clients;
    private AeronSubscriber marketDataClientSubscriber;
    private AeronPublisher matchingEnginePublisher;
    private UnsafeBuffer temp = new UnsafeBuffer(ByteBuffer.allocate(106));

    private MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();

    public MDClientProcessor(String mediaDriverConextDir, AeronPublisher matchingEnginePublisher, IntObjectMap<Client> clients){
        this.mediaDriverConextDir = mediaDriverConextDir;
        this.matchingEnginePublisher = matchingEnginePublisher;
        this.clients = clients;
    }

    public void stop(){
        marketDataClientSubscriber.stop();
    }

    @Override
    public void run() {
        initClientSubscriber();
        marketDataClientSubscriber.start();
    }

    private void initClientSubscriber(){
        marketDataClientSubscriber = new AeronSubscriber(mediaDriverConextDir,this);

        for(ObjectCursor<Client> client : clients.values()){
            marketDataClientSubscriber.addSubscriber(client.value.getMdgInputURL(), client.value.getMdgInputStreamId());
        }
    }

    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
        temp.wrap(buffer.byteBuffer(),offset,length);
        messageHeaderDecoder.wrap(temp, 0);
        matchingEnginePublisher.send(temp);
    }
}
