package receiver;

import com.lmax.disruptor.EventFactory;
import sbe.msg.SideEnum;

public class OrderVOMessageEvent {
    private int securityId;
    private long orderId;
    private long submittedTime;
    private long price;
    private long volume;
    private SideEnum side;

    private final static EventFactory<OrderVOMessageEvent> EVENT_FACTORY = () -> new OrderVOMessageEvent();

    public static EventFactory<OrderVOMessageEvent> getEventFactory() {
        return EVENT_FACTORY;
    }

    public int getSecurityId() {
        return securityId;
    }

    public void setSecurityId(int securityId) {
        this.securityId = securityId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getSubmittedTime() {
        return submittedTime;
    }

    public void setSubmittedTime(long submittedTime) {
        this.submittedTime = submittedTime;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public SideEnum getSide() {
        return side;
    }

    public void setSide(SideEnum side) {
        this.side = side;
    }
}
