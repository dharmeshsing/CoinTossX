package crossing.postProcessor;

import orderBook.OrderBook;
import crossing.strategy.PriceTimePriorityStrategy;

public interface MatchingPostProcessor {
    void postProcess(PriceTimePriorityStrategy priceTimePriorityStrategy,OrderBook orderBook);
}
