package receiver;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

public class TradeVODisruptor {
    private Disruptor<TradeVOMessageEvent> disruptor;
    private RingBuffer<TradeVOMessageEvent> ringBuffer;

    public TradeVODisruptor(TradeVOHandler handler, int size){
        init(handler,size);
    }

    public void init(TradeVOHandler handler,int size){
        int bufferSize;
        if(size == 0) {
            bufferSize = findNextPositivePowerOfTwo(100_000);
        }else{
            bufferSize = findNextPositivePowerOfTwo(size);
        }


        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .daemon(true)
                .build();

        disruptor = new Disruptor<>(TradeVOMessageEvent.getEventFactory(), bufferSize, factory, ProducerType.SINGLE,new BlockingWaitStrategy());
        disruptor.handleEventsWith(handler);
        ringBuffer = disruptor.start();
    }

    public void addTradeVO(int securityId,int tradeId,long clientOrderId,int price,int quantity,long executedTime){
        long sequence = ringBuffer.next();
        TradeVOMessageEvent event = ringBuffer.get(sequence);

        event.setSecurityId(securityId);
        event.setTradeId(tradeId);
        event.setClientOrderId(clientOrderId);
        event.setPrice(price);
        event.setQuantity(quantity);
        event.setExecutedTime(executedTime);

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
