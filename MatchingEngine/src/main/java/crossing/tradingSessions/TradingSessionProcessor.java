package crossing.tradingSessions;

import leafNode.OrderEntry;
import orderBook.OrderBook;

public interface TradingSessionProcessor {

    void startSession(OrderBook orderBook);
    void process(OrderBook orderBook,OrderEntry orderEntry);
    void  endSession(OrderBook orderBook);
    boolean isOrderValid(OrderEntry orderEntry,int template);
}
