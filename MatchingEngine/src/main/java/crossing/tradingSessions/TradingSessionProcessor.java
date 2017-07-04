package crossing.tradingSessions;

import leafNode.OrderEntry;
import orderBook.OrderBook;

/**
 * Created by dharmeshsing on 26/08/15.
 */
public interface TradingSessionProcessor {

    void startSession(OrderBook orderBook);
    void process(OrderBook orderBook,OrderEntry orderEntry);
    void  endSession(OrderBook orderBook);
    boolean isOrderValid(OrderEntry orderEntry,int template);
}
