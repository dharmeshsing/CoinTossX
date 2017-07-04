package crossing.preProcessor;

import crossing.MatchingUtil;
import crossing.preProcessor.MatchingPreProcessor.MATCHING_ACTION;
import crossing.strategy.MatchingLogic;
import leafNode.OrderEntry;
import orderBook.OrderBook;

/**
 * Created by dharmeshsing on 7/07/15.
 */
public class StopOrderPreProcessor {

    public static MATCHING_ACTION preProcess(long lastTradedPrice, OrderBook.SIDE side, OrderEntry orderEntry) {
        if(MatchingUtil.canConverStopOrder(lastTradedPrice,side,orderEntry.getStopPrice())){
            MatchingUtil.convertStopOrderToMarketOrLimitOrder(orderEntry);
            return MATCHING_ACTION.AGGRESS_ORDER;
        }

        return MATCHING_ACTION.PARK_ORDER;
    }
}
