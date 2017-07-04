package crossing.strategy;

import bplusTree.BPlusTree;
import common.TimeInForce;
import config.Configuration;
import crossing.MatchingUtil;
import leafNode.OrderEntry;
import leafNode.OrderList;
import leafNode.OrderListCursor;
import orderBook.OrderBook;
import org.joda.time.Duration;
import org.joda.time.Instant;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by dharmeshsing on 29/07/15.
 */
public class TimeInForceStrategy {

    public static boolean removeOrder(int quantity,byte timeInForce){
        if(quantity >=0 && timeInForce == TimeInForce.IOC.getValue()){
            return true;
        }

        return false;
    }

    public static boolean canFOKOrderBeFilled(BPlusTree.BPlusTreeIterator iterator,int aggOrderQuantity,long aggOrderPrice,int aggOrderMES,OrderBook.SIDE side){
        int remainingQuantity = aggOrderQuantity;
        boolean result = false;

        while (!result && iterator.hasNext()) {
            Map.Entry<Long, OrderList> entry = iterator.next();
            if(!MatchingUtil.isValidProcessingPrice(aggOrderPrice, entry.getKey(), side)) {
                break;
            }

            Iterator<OrderListCursor> orderListIterator = entry.getValue().iterator();
            while(orderListIterator.hasNext()){
                OrderEntry currentOrder = orderListIterator.next().value;
                int quantity = MatchingUtil.getExecutionQuantity(currentOrder.getQuantity(), aggOrderQuantity,currentOrder.getMinExecutionSize(),aggOrderMES);
                if (quantity != -1) {
                    remainingQuantity -= quantity;
                }

                if(remainingQuantity <= 0){
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public static void expireOrders(OrderBook orderBook,TimeInForce timeInForce) {
        execute(orderBook,timeInForce,OrderBook.SIDE.BID);
        execute(orderBook,timeInForce,OrderBook.SIDE.OFFER);
    }

    private static void execute(OrderBook orderBook,TimeInForce timeInForce,OrderBook.SIDE side){
        BPlusTree.BPlusTreeIterator iterator  = orderBook.getPriceIterator(side);

        while (iterator.hasNext()) {
            Map.Entry<Long, OrderList> entry = iterator.next();
            OrderList orderList = entry.getValue();
            Iterator<OrderListCursor> orderListIterator = orderList.iterator();
            while (orderListIterator.hasNext()) {
                if (orderListIterator.next().value.getTimeInForce() == timeInForce.getValue()) {
                    orderListIterator.remove();
                }
            }

            if(orderList.total() == 0){
                orderBook.removePrice(entry.getKey(), side);
            }
        }
    }

    public static boolean isPastMaxDuration(long submittedTime){
        long duration = Duration.millis(Instant.now().getMillis() - submittedTime).getStandardDays();
        if(duration >= Configuration.MAX_ORDER_DURATION_DAYS){
            return true;
        }

        return false;
    }

    public static boolean isPastExpiryDateOrMaxDuration(long expiryDate, long submittedTime){
        boolean result = Duration.millis(expiryDate).getStandardDays() == Duration.millis(Instant.now().getMillis()).getStandardDays();
        if(!result){
            return isPastMaxDuration(submittedTime);
        }

        return true;
    }
}
