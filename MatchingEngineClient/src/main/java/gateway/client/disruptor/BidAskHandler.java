package gateway.client.disruptor;

import com.carrotsearch.hppc.ObjectArrayList;
import com.lmax.disruptor.EventHandler;
import gateway.client.GatewayListener;

public class BidAskHandler implements EventHandler<BidAskMessageEvent> {
    private ObjectArrayList<GatewayListener> listeners;
    private int listenerSize;

    public BidAskHandler(ObjectArrayList<GatewayListener> listeners,int listenerSize){
        this.listeners = listeners;
        this.listenerSize = listenerSize;
    }

    @Override
    public void onEvent(BidAskMessageEvent event, long sequence, boolean endOfBatch) throws Exception {
        for (int i = 0; i < listenerSize; i++) {
            listeners.get(i).updateBidAskPrice(event.getInstrumentId(),event.getBid(),event.getBidQuantity(),
                    event.getOffer(),event.getOfferQuantity());
        }
    }
}
