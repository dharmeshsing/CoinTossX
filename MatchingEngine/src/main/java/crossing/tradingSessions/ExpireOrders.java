package crossing.tradingSessions;

import bplusTree.BPlusTree;
import common.TimeInForce;
import leafNode.OrderList;
import leafNode.OrderListCursor;
import orderBook.OrderBook;

import java.util.Iterator;
import java.util.Map;

public class ExpireOrders {

    public static void expire(OrderBook orderBook, TimeInForce timeInForce) {
        execute(orderBook, OrderBook.SIDE.BID,timeInForce);
        execute(orderBook,OrderBook.SIDE.OFFER,timeInForce);
    }

    private static void execute(OrderBook orderBook,OrderBook.SIDE side,TimeInForce timeInForce){
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
}
