package gateway.client.disruptor;

import com.lmax.disruptor.EventFactory;

public class BidAskMessageEvent {
    private long bid;
    private long offer;
    private long bidQuantity;
    private long offerQuantity;
    private long instrumentId;

    private final static EventFactory<BidAskMessageEvent> EVENT_FACTORY = () -> new BidAskMessageEvent();

    public static EventFactory<BidAskMessageEvent> getEventFactory() {
        return EVENT_FACTORY;
    }

    public long getBid() {
        return bid;
    }

    public void setBid(long bid) {
        this.bid = bid;
    }

    public long getOffer() {
        return offer;
    }

    public void setOffer(long offer) {
        this.offer = offer;
    }

    public long getBidQuantity() {
        return bidQuantity;
    }

    public void setBidQuantity(long bidQuantity) {
        this.bidQuantity = bidQuantity;
    }

    public long getOfferQuantity() {
        return offerQuantity;
    }

    public void setOfferQuantity(long offerQuantity) {
        this.offerQuantity = offerQuantity;
    }

    public long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(long instrumentId) {
        this.instrumentId = instrumentId;
    }
}
