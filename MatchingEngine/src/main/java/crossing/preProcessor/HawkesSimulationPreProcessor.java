package crossing.preProcessor;

import bplusTree.BPlusTree;
import common.OrderType;
import crossing.MatchingContext;
import leafNode.OrderList;
import orderBook.OrderBook;

import java.util.Map;

public class HawkesSimulationPreProcessor implements MatchingPreProcessor  {

    private static int ORDER_BOOK_DEPTH = 10;

    public static int getOrderBookDepth() {
        return ORDER_BOOK_DEPTH;
    }

    public static void setOrderBookDepth(int orderBookDepth) {
        ORDER_BOOK_DEPTH = orderBookDepth;
    }

    @Override
    public void preProcess(MatchingContext context) {
        MATCHING_ACTION action = preProcess(context.getOrderBook(),context.getSide(),context.getOrderEntry().getPrice(),context.getOrderType());
        if(action != null) {
            context.setAction(action);
        }
    }

    public MATCHING_ACTION preProcess(OrderBook orderBook, OrderBook.SIDE side,long price, OrderType orderType) {
        BPlusTree.BPlusTreeIterator iterator = orderBook.getPriceIterator(side);
        boolean priceDoesNotExist = true;

        while(iterator.hasNext()){
            Map.Entry<Long, OrderList> entry = iterator.next();
            if(entry.getKey() == price){
                priceDoesNotExist = false;
                break;
            }
        }

        if(priceDoesNotExist && orderType.equals(OrderType.LIMIT)){
            if(side == OrderBook.SIDE.BID) {
                if(orderBook.getBidTree().size() >= ORDER_BOOK_DEPTH){
                    return MATCHING_ACTION.NO_ACTION;
                }
            }else{
                if(orderBook.getOfferTree().size() >= ORDER_BOOK_DEPTH){
                    return MATCHING_ACTION.NO_ACTION;
                }
            }
        }
        return null;
    }


}
