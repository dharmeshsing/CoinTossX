package crossing.postProcessor;

import bplusTree.BPlusTree;
import com.carrotsearch.hppc.ObjectArrayList;
import common.OrderType;
import crossing.MatchingUtil;
import crossing.preProcessor.MatchingPreProcessor.MATCHING_ACTION;
import crossing.strategy.PriceTimePriorityStrategy;
import leafNode.OrderEntry;
import leafNode.OrderList;
import leafNode.OrderListCursor;
import orderBook.OrderBook;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dharmeshsing on 8/07/15.
 */
public class StopOrderPostProcessor implements MatchingPostProcessor {

    private PriceTimePriorityStrategy priceTimePriorityStrategy;
    private ObjectArrayList<OrderEntry> sortedOrders = new ObjectArrayList<>();
    private StopOrderComparator stopOrderComparator = new StopOrderComparator();

    //TODO: Fix logic for election order of stop orders.
    public void postProcess(PriceTimePriorityStrategy priceTimePriorityStrategy, OrderBook orderBook) {
        this.priceTimePriorityStrategy = priceTimePriorityStrategy;
        execute(orderBook,orderBook.getParkedBidTreeIterator(), OrderBook.SIDE.BID);
        execute(orderBook,orderBook.getParkedOfferTreeIterator(),OrderBook.SIDE.OFFER);
    }

    private void execute(OrderBook orderBook, BPlusTree.BPlusTreeIterator iterator,OrderBook.SIDE side) {
        sortParkedOrdersByTime(iterator);

        for(int i=0; i<sortedOrders.buffer.length; i++){
            iterator.reset();
            while(iterator.hasNext()){
                Map.Entry<Long, OrderList> entry = iterator.next();
                if(entry.getKey() != ((OrderEntry)sortedOrders.buffer[i]).getPrice()){
                    continue;
                }

                OrderList orderList = entry.getValue();
                Iterator<OrderListCursor> orderListIterator = orderList.iterator();
                while (orderListIterator.hasNext()) {
                    OrderEntry parkedOrder = orderListIterator.next().value;
                    if(parkedOrder.getOrderId() == ((OrderEntry)sortedOrders.buffer[i]).getOrderId());
                    if (isStopOrder(parkedOrder)) {
                        boolean isStoppedOrderProcessed = processStopOrder(orderBook,parkedOrder,side);
                        if(isStoppedOrderProcessed) {
                            orderListIterator.remove();
                        }
                    }
                }

                if(orderList.total() == 0){
                    orderBook.removeParkedPrice(entry.getKey(),side);
                }

            }
        }
    }

    private void sortParkedOrdersByTime(BPlusTree.BPlusTreeIterator iterator){
        iterator.reset();
        sortedOrders.clear();

        while (iterator.hasNext()) {
            Map.Entry<Long, OrderList> entry = iterator.next();
            OrderList orderList = entry.getValue();
            Iterator<OrderListCursor> orderListIterator = orderList.iterator();
            while (orderListIterator.hasNext()) {
                OrderEntry parkedOrder = orderListIterator.next().value;
                if (isStopOrder(parkedOrder)) {
                    sortedOrders.add(parkedOrder);
                }
            }
        }

        sortedOrders.trimToSize();
        Object[] orderEntrys = sortedOrders.buffer;
        Arrays.sort(orderEntrys, stopOrderComparator);
    }

    private boolean isStopOrder(OrderEntry parkedOrder){
        return parkedOrder.getType() == OrderType.STOP.getOrderType() ||
                parkedOrder.getType() == OrderType.STOP_LIMIT.getOrderType();

    }

    private boolean processStopOrder(OrderBook orderBook,OrderEntry stopOrder,OrderBook.SIDE side) {
        if(MatchingUtil.canConverStopOrder(orderBook.getLasTradedPrice(),side,stopOrder.getStopPrice())) {
            MatchingUtil.convertStopOrderToMarketOrLimitOrder(stopOrder);
            priceTimePriorityStrategy.process(MATCHING_ACTION.AGGRESS_ORDER,orderBook,stopOrder);
            return true;
        }

        return false;
    }

    class StopOrderComparator implements Comparator<Object>{

        @Override
        public int compare(Object o1, Object o2) {
            return Long.compare(((OrderEntry) o2).getSubmittedTime(), ((OrderEntry) o1).getSubmittedTime());
        }
    }
}
