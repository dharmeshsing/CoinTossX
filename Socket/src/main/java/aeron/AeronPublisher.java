package aeron;

import com.carrotsearch.hppc.ObjectObjectMap;
import com.carrotsearch.hppc.ObjectObjectScatterMap;
import com.carrotsearch.hppc.cursors.ObjectCursor;
import uk.co.real_logic.aeron.Aeron;
import uk.co.real_logic.aeron.Image;
import uk.co.real_logic.aeron.Publication;
import uk.co.real_logic.aeron.Subscription;
import uk.co.real_logic.aeron.driver.Configuration;
import uk.co.real_logic.agrona.CloseHelper;
import uk.co.real_logic.agrona.DirectBuffer;
import uk.co.real_logic.agrona.concurrent.BusySpinIdleStrategy;
import uk.co.real_logic.agrona.concurrent.IdleStrategy;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by dharmeshsing on 7/08/15.
 */
public class AeronPublisher {
    private AtomicBoolean running = new AtomicBoolean(true);
    private Aeron.Context ctx;
    private Aeron aeron;
    private ObjectObjectMap<String,Publication> publications;
    private UnsafeBuffer tmpBuffer = new UnsafeBuffer(ByteBuffer.allocateDirect(106));
    private static final IdleStrategy OFFER_IDLE_STRATEGY = Configuration.agentIdleStrategy("uk.co.real_logic.agrona.concurrent.BackoffIdleStrategy");

    public AeronPublisher(String contextDir){
        this.publications = new ObjectObjectScatterMap<>();
        //SigInt.register(() -> running.set(false));
        initialize(contextDir);
    }

    public void addPublication(String channel,int streamId){
        publications.put(getKey(channel, streamId), aeron.addPublication(channel, streamId));
    }

    private void printError(Throwable throwable){
        System.out.println(throwable.toString());
    }

    public void printAvailableImage(final Image image) {
        final Subscription subscription = image.subscription();
        System.out.println(String.format(
                "Available image on %s streamId=%d sessionId=%d from %s",
                subscription.channel(), subscription.streamId(), image.sessionId(), image.sourceIdentity()));
    }

    public void printUnavailableImage(final Image image) {
        final Subscription subscription = image.subscription();
        System.out.println(String.format(
                "Unavailable image on %s streamId=%d sessionId=%d",
                subscription.channel(), subscription.streamId(), image.sessionId()));
    }

    private void initialize(String contextDir){
        ctx = new Aeron.Context();
        ctx.driverTimeoutMs(1000000);
        ctx.aeronDirectoryName(contextDir);
        ctx.errorHandler(this::printError);
        ctx.availableImageHandler(this::printAvailableImage);
        ctx.unavailableImageHandler(this::printUnavailableImage);

        aeron = Aeron.connect(ctx);

    }

    public long send(DirectBuffer msg,String channel,int streamId) {
        if(running.get() && msg != null){
            Publication pub = publications.get(getKey(channel, streamId));
            return pub.offer(msg);
        }
        return 0;
    }

    public void send(DirectBuffer msg) {
        if(msg != null) {
            long result = -1;
            int failed = 0;
            OFFER_IDLE_STRATEGY.reset();
            for (ObjectCursor<Publication> pub : publications.values()) {
                while(result < 0 && failed < 3) {
                    result = pub.value.offer(msg);

                    if (result < 0L) {
                        failed++;
                        if (result == Publication.BACK_PRESSURED) {
                            System.out.println("Offer failed due to back pressure " + failed);
                        } else if (result == Publication.NOT_CONNECTED) {
                            System.out.println("Offer failed because publisher is not yet connected to subscriber " + failed);
                        } else if (result == Publication.ADMIN_ACTION) {
                            //ignore and retry
                            //System.out.println("Offer failed because of an administration action in the system " + failed);
                        } else if (result == Publication.CLOSED) {
                            System.out.println("Offer failed publication is closed" + failed);
                        }else {
                            System.out.println("Offer failed due to unknown reason " + failed);
                        }

                        if (!pub.value.isConnected()) {
                            System.out.println("No active subscribers detected");
                        }

                        OFFER_IDLE_STRATEGY.idle();
                    }
                }
            }
        }
    }

    public void sendAndNotRetry(DirectBuffer msg) {
        for (ObjectCursor<Publication> pub : publications.values()) {
            pub.value.offer(msg);
        }
    }

    private String getKey(String channel,int streamId){
        return channel + "_" + streamId;
    }

    public void stop() {
        running.set(false);
        CloseHelper.close(aeron);

        for(ObjectCursor<Publication> pub: publications.values()){
            CloseHelper.close(pub.value);
        }
    }
}
