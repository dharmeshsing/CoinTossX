package crossing.preProcessor;

import bplusTree.BPlusTree;
import common.OrderType;
import crossing.MatchingUtil;
import crossing.preProcessor.MatchingPreProcessor.MATCHING_ACTION;
import leafNode.OrderEntry;
import leafNode.OrderList;
import leafNode.OrderListCursor;
import orderBook.OrderBook;

import java.util.Iterator;
import java.util.Map;

public class HiddenOrderPreProcessor {

    public static MATCHING_ACTION preProcess(OrderBook orderBook,long aggOrderMES,long aggOrderPrice,OrderBook.SIDE side) {
        if (canHOBeFilled(orderBook.getContraSidePriceIterator(side),aggOrderMES,aggOrderPrice,side)) {
            return MATCHING_ACTION.AGGRESS_ORDER;
        } else {
            return MATCHING_ACTION.ADD_ORDER;
        }
    }

    public static boolean canHOBeFilled(BPlusTree.BPlusTreeIterator iterator,long aggOrderMES,long aggOrderPrice,OrderBook.SIDE side){
        boolean result = false;

        while (!result && iterator.hasNext()) {
            Map.Entry<Long, OrderList> entry = iterator.next();
            if(!MatchingUtil.isValidProcessingPrice(aggOrderPrice, entry.getKey(), side)) {
                break;
            }

            Iterator<OrderListCursor> orderListIterator = entry.getValue().iterator();
            while(orderListIterator.hasNext()){
                OrderEntry currentOrder = orderListIterator.next().value;
                if(currentOrder.getType() == OrderType.HIDDEN_LIMIT.getOrderType()){
                    if (currentOrder.getMinExecutionSize() <= aggOrderMES) {
                        aggOrderMES -= currentOrder.getQuantity();
                    }
                }else {
                    if (currentOrder.getQuantity() <= aggOrderMES) {
                        aggOrderMES -= currentOrder.getQuantity();
                    }
                }

                if(aggOrderMES <= 0){
                    result = true;
                    break;
                }
            }
        }

        return result;
    }
}
