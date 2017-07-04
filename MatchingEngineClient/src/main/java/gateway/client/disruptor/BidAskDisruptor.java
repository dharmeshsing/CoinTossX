package gateway.client.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * Created by dharmeshsing on 19/12/16.
 */
public class BidAskDisruptor {
    private Disruptor<BidAskMessageEvent> disruptor;
    private RingBuffer<BidAskMessageEvent> ringBuffer;

    public BidAskDisruptor(BidAskHandler handler, int size){
        init(handler,size);
    }

    public void init(BidAskHandler handler,int size){
        int bufferSize;
        if(size == 0) {
            bufferSize = findNextPositivePowerOfTwo(100_000);
        }else{
            bufferSize = findNextPositivePowerOfTwo(size);
        }

        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .daemon(true)
                .build();

        disruptor = new Disruptor<>(BidAskMessageEvent.getEventFactory(), bufferSize, factory, ProducerType.SINGLE,new BlockingWaitStrategy());
        disruptor.handleEventsWith(handler);
        ringBuffer = disruptor.start();
    }

    public void addBidAsk(long instrumentId,long bid,long bidQuantity,long offer,long offerQuantity){
        long sequence = ringBuffer.next();
        BidAskMessageEvent event = ringBuffer.get(sequence);

        event.setInstrumentId(instrumentId);
        event.setBid(bid);
        event.setBidQuantity(bidQuantity);
        event.setOffer(offer);
        event.setOfferQuantity(offerQuantity);

        ringBuffer.publish(sequence);
    }

    private int findNextPositivePowerOfTwo(int value) {
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    public void stop(){
        disruptor.shutdown();
    }

    public long getCursor(){
        return ringBuffer.getCursor();
    }
}
