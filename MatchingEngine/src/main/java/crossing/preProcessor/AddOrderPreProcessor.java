package crossing.preProcessor;

import common.OrderType;
import crossing.MatchingContext;
import orderBook.OrderBook;

public class AddOrderPreProcessor implements MatchingPreProcessor  {

    @Override
    public void preProcess(MatchingContext context) {
        MATCHING_ACTION action = preProcess(context.getOrderType(),context.getSide(),context.getOfferTreeSize(),context.getBideTreeSize(),
                context.getBestBid(),context.getBestOffer(),context.getOrderBook().getBestVisibleBid(),
                context.getOrderBook().getBestVisibleOffer(), context.getPrice());

        context.setAction(action);
    }

    public MATCHING_ACTION preProcess(OrderType orderType, OrderBook.SIDE side,long offerTreeSize,long bidTreeSize,
                          long bestBid,long bestOffer,long bestVisibleBid,long bestVisibleOffer,long price) {

        if(orderType.getOrderType() == OrderType.MARKET.getOrderType()) {
            return MATCHING_ACTION.AGGRESS_ORDER;
        }

        if(side == OrderBook.SIDE.BID) {
            if(offerTreeSize == 0){
                return MATCHING_ACTION.ADD_ORDER;
            }

            if(bestBid != 0 && price < bestBid){
                return MATCHING_ACTION.ADD_ORDER;
            }

            if(price == bestVisibleBid){
                return  MATCHING_ACTION.ADD_AND_AGGRESS;
            }

        }else{
            if(bidTreeSize == 0){
                return MATCHING_ACTION.ADD_ORDER;
            }

            if(bestOffer != 0 &&  price > bestOffer){
                return MATCHING_ACTION.ADD_ORDER;
            }

            if(price == bestVisibleOffer){
                return  MATCHING_ACTION.ADD_AND_AGGRESS;
            }
        }

        return MATCHING_ACTION.AGGRESS_ORDER;
    }


}
