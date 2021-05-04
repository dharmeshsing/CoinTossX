package crossing.strategy;

import orderBook.OrderBook;
import crossing.preProcessor.MatchingPreProcessor.MATCHING_ACTION;
import leafNode.OrderEntry;

public interface MatchingLogic {
    void process(MATCHING_ACTION action, OrderBook orderBook, OrderEntry orderEntry);
}
