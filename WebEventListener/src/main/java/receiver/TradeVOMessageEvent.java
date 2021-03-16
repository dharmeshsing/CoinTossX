package receiver;

import com.lmax.disruptor.EventFactory;

public class TradeVOMessageEvent {
    private int securityId;
    private int tradeId;
    private int price;
    private int quantity;

    private final static EventFactory<TradeVOMessageEvent> EVENT_FACTORY = () -> new TradeVOMessageEvent();

    public static EventFactory<TradeVOMessageEvent> getEventFactory() {
        return EVENT_FACTORY;
    }

    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSecurityId() {
        return securityId;
    }

    public void setSecurityId(int securityId) {
        this.securityId = securityId;
    }
}
