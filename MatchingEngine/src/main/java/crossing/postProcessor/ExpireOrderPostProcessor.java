package crossing.postProcessor;

import bplusTree.BPlusTree;
import crossing.expireRule.ExpireRule;
import crossing.expireRule.MarketOrderExpireRule;
import crossing.strategy.PriceTimePriorityStrategy;
import leafNode.OrderList;
import leafNode.OrderListCursor;
import orderBook.OrderBook;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by dharmeshsing on 8/07/15.
 */
public class ExpireOrderPostProcessor implements MatchingPostProcessor{

    private MarketOrderExpireRule marketOrderExpireRule = new MarketOrderExpireRule();

    public void postProcess(PriceTimePriorityStrategy priceTimePriorityStrategy,OrderBook orderBook) {
        postProcess(orderBook, marketOrderExpireRule);
    }

    public void postProcess(PriceTimePriorityStrategy priceTimePriorityStrategy,OrderBook orderBook,ExpireRule expireRule) {
        postProcess(orderBook, expireRule);
    }

    public void postProcess(OrderBook orderBook,ExpireRule expireRule) {
        execute(orderBook, OrderBook.SIDE.BID,expireRule);
        execute(orderBook,OrderBook.SIDE.OFFER,expireRule);
    }

    private void execute(OrderBook orderBook,OrderBook.SIDE side,ExpireRule expireRule){
        BPlusTree.BPlusTreeIterator iterator  = orderBook.getPriceIterator(side);

        while (iterator.hasNext()) {
            Map.Entry<Long, OrderList> entry = iterator.next();
            OrderList orderList = entry.getValue();
            Iterator<OrderListCursor> orderListIterator = orderList.iterator();
            while (orderListIterator.hasNext()) {
                if(expireRule.isOrderExpired(orderListIterator.next().value)) {
                    orderListIterator.remove();
                }
            }

            if(orderList.total() == 0){
                orderBook.removePrice(entry.getKey(), side);
            }
        }
    }

}
