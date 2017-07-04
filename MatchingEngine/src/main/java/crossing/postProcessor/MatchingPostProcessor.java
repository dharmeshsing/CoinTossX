package crossing.postProcessor;

import orderBook.OrderBook;
import crossing.strategy.PriceTimePriorityStrategy;

/**
 * Created by dharmeshsing on 8/07/15.
 */
public interface MatchingPostProcessor {
    void postProcess(PriceTimePriorityStrategy priceTimePriorityStrategy,OrderBook orderBook);
}
