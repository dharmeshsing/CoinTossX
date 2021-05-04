package receiver;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import sbe.msg.SideEnum;

public class OrderVODisruptor {
    private Disruptor<OrderVOMessageEvent> disruptor;
    private RingBuffer<OrderVOMessageEvent> ringBuffer;

    public OrderVODisruptor(OrderVOHandler handler,int size){
        init(handler,size);
    }

    public void init(OrderVOHandler handler,int size){
        int bufferSize;
        if(size == 0) {
            bufferSize = findNextPositivePowerOfTwo(100_000);
        }else{
            bufferSize = findNextPositivePowerOfTwo(size);
        }

        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .daemon(true)
                .build();

        disruptor = new Disruptor<>(OrderVOMessageEvent.getEventFactory(), bufferSize, factory, ProducerType.SINGLE,new BlockingWaitStrategy());
        disruptor.handleEventsWith(handler);
        ringBuffer = disruptor.start();
    }

    public void addOrderVO(int securityId, long orderId, String clientOrderId, SideEnum side, long submittedTime, long volume, long price){
        long sequence = ringBuffer.next();
        OrderVOMessageEvent event = ringBuffer.get(sequence);

        event.setSecurityId(securityId);
        event.setOrderId(orderId);
        event.setClientOrderId(clientOrderId);
        event.setSide(side);
        event.setSubmittedTime(submittedTime);
        event.setVolume(volume);
        event.setPrice(price);

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
