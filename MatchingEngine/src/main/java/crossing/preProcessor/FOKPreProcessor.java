package crossing.preProcessor;

import crossing.preProcessor.MatchingPreProcessor.MATCHING_ACTION;
import crossing.strategy.MatchingLogic;
import crossing.strategy.TimeInForceStrategy;
import orderBook.OrderBook;

/**
 * Created by dharmeshsing on 7/07/15.
 */
public class FOKPreProcessor {

    public static MATCHING_ACTION preProcess(OrderBook orderBook,int aggOrderQuantity,long aggOrderPrice,int aggOrderMES,OrderBook.SIDE side) {
        if (TimeInForceStrategy.canFOKOrderBeFilled(orderBook.getContraSidePriceIterator(side), aggOrderQuantity, aggOrderPrice, aggOrderMES, side)) {
            return MATCHING_ACTION.AGGRESS_ORDER;
        } else {
            return MATCHING_ACTION.NO_ACTION;
        }
    }
}
