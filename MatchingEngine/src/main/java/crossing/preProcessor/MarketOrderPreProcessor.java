package crossing.preProcessor;

import leafNode.OrderEntry;
import orderBook.OrderBook;

/**
 * Created by dharmeshsing on 7/07/15.
 */
public class MarketOrderPreProcessor {


    public static MatchingPreProcessor.MATCHING_ACTION preProcess(OrderBook.SIDE side, OrderEntry orderEntry,long bidTreeSize,long offerTreeSize,long bestBidPrice,long bestOfferPrice) {
        setMarketOrderPrice(side,orderEntry,bidTreeSize,offerTreeSize,bestBidPrice,bestOfferPrice);

        if(orderEntry.getPrice() == 0){
            return MatchingPreProcessor.MATCHING_ACTION.NO_ACTION;
        }else{
            return MatchingPreProcessor.MATCHING_ACTION.AGGRESS_ORDER;
        }
    }

    private static void setMarketOrderPrice(OrderBook.SIDE side,OrderEntry orderEntry,long bidTreeSize,long offerTreeSize,long bestBidPrice,long bestOfferPrice){
        if(side == OrderBook.SIDE.BID){
            if(offerTreeSize > 0) {
                orderEntry.setPrice(bestOfferPrice);
            }
        }else{
            if(bidTreeSize > 0) {
                orderEntry.setPrice(bestBidPrice);
            }
        }
    }
}
