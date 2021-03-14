package crossing;

import bplusTree.BPlusTree;
import common.OrderType;
import common.TimeInForce;
import config.Configuration;
import crossing.preProcessor.MatchingPreProcessor;
import crossing.strategy.PriceTimePriorityStrategy;
import data.MarketData;
import leafNode.OrderEntry;
import leafNode.OrderList;
import leafNode.OrderListCursor;
import orderBook.OrderBook;
import org.joda.time.Instant;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dharmeshsing on 8/07/15.
 */
public class MatchingUtil {

    private static AtomicInteger orderIds = new AtomicInteger();
    private static OrderEntry orderEntry = new OrderEntry();
    private static boolean enableCircuitBreaker = false;

    public static void convertStopOrderToMarketOrLimitOrder(OrderEntry orderEntry){
        if (orderEntry.getType() == OrderType.STOP.getOrderType()) {
            orderEntry.setType(OrderType.MARKET.getOrderType());
        } else if (orderEntry.getType() == OrderType.STOP_LIMIT.getOrderType()) {
            orderEntry.setType(OrderType.LIMIT.getOrderType());
        }
    }

    public static boolean canConverStopOrder(long lastTradedPrice,OrderBook.SIDE side,long stopPrice){
        if(lastTradedPrice != 0){
            if(side == OrderBook.SIDE.BID && lastTradedPrice >= stopPrice) { return true; }
            else if (side == OrderBook.SIDE.OFFER && lastTradedPrice <= stopPrice) { return true; }
        }
        return false;

    }

    public static boolean isValidProcessingPrice(long aggPrice, long existingPrice, OrderBook.SIDE aggSide){
        if((aggSide == OrderBook.SIDE.BID && aggPrice >= existingPrice) || (aggSide == OrderBook.SIDE.OFFER && aggPrice <= existingPrice)) {
            return true;
        }
        return false;
    }

    public static int getExecutionQuantity(int existingOrderQuantity,int aggOrderQuantity,int existingOrderMES,int aggOrderMES){
        int maxMESQuantity = Math.max(existingOrderMES,aggOrderMES);
        int minQuantity = Math.min(existingOrderQuantity, aggOrderQuantity);

        if(minQuantity >= maxMESQuantity){
            return minQuantity;
        }

        return -1;
    }

   /* public static int getExecutionQuantity(int existingOrderQuantity,int aggOrderQuantity,int existingOrderMES,int aggOrderMES){
        int minQuantity = Math.min(existingOrderQuantity, aggOrderQuantity);

        if(existingOrderMES == 0 || minQuantity >= existingOrderMES){
            return minQuantity;
        }

        return -1;
    }*/


    public static boolean isParkedOrder(OrderEntry orderEntry){
        return orderEntry.getType() == OrderType.STOP.getOrderType() ||
                orderEntry.getType() == OrderType.STOP_LIMIT.getOrderType();

    }

    public static boolean isPastExpiryDateTime(long expiryDateTime){
        return expiryDateTime <= Instant.now().getMillis();
    }

    public static void parkGFAOrders(OrderBook orderBook){
        parkGFAOrders(orderBook, orderBook.getBidTreeIterator(), OrderBook.SIDE.BID);
        parkGFAOrders(orderBook, orderBook.getOfferTreeIterator(), OrderBook.SIDE.OFFER);
    }

    private static void parkGFAOrders(OrderBook orderBook, BPlusTree.BPlusTreeIterator iterator,OrderBook.SIDE side) {
        iterator.reset();

        while (iterator.hasNext()) {
            Map.Entry<Long, OrderList> entry = iterator.next();
            OrderList orderList = entry.getValue();
            Iterator<OrderListCursor> orderListIterator = orderList.iterator();
            while (orderListIterator.hasNext()) {
                OrderEntry orderEntry = orderListIterator.next().value;
                if (orderEntry.getTimeInForce() == TimeInForce.GFA.getValue()) {
                    orderBook.addParkedOrder(orderEntry,side);
                    orderListIterator.remove();
                }
            }
        }
    }

    public static void injectOrders(OrderBook orderBook,PriceTimePriorityStrategy priceTimePriorityStrategy,List<TimeInForce> timeInForceList){
        if(orderBook != null) {
            injectOrders(orderBook, orderBook.getParkedBidTreeIterator(), priceTimePriorityStrategy, timeInForceList);
            injectOrders(orderBook, orderBook.getParkedOfferTreeIterator(), priceTimePriorityStrategy, timeInForceList);
        }
    }

    private static void injectOrders(OrderBook orderBook, BPlusTree.BPlusTreeIterator iterator,PriceTimePriorityStrategy priceTimePriorityStrategy,List<TimeInForce> timeInForceList) {
        iterator.reset();

        while (iterator.hasNext()) {
            Map.Entry<Long, OrderList> entry = iterator.next();
            OrderList orderList = entry.getValue();
            Iterator<OrderListCursor> orderListIterator = orderList.iterator();
            while (orderListIterator.hasNext()) {
                OrderEntry orderEntry = orderListIterator.next().value;
                if (timeInForceList.contains(orderEntry.getTimeInForce())) {
                    priceTimePriorityStrategy.process(MatchingPreProcessor.MATCHING_ACTION.ADD_ORDER,orderBook,orderEntry);
                    orderListIterator.remove();
                }
            }
        }
    }

    public static Integer getNextOrderId(){
        return orderIds.incrementAndGet();
    }

    public static boolean doesPriceBreachCircuitBreaker(OrderBook orderBook, long price){
        if(enableCircuitBreaker) {
            long calculatedStaticTolerance = 0;
            long calculatedDynamicTolerance = 0;

            if (orderBook.getStaticPriceReference() > 0) {
                calculatedStaticTolerance = Math.abs(((price - orderBook.getStaticPriceReference()) / orderBook.getStaticPriceReference()) * 100);
            }

            if (orderBook.getDynamicPriceReference() > 0) {
                calculatedDynamicTolerance = Math.abs(((price - orderBook.getDynamicPriceReference()) / orderBook.getDynamicPriceReference()) * 100);
            }

            if (calculatedStaticTolerance > Configuration.CIRCUIT_BREAKER_TOLERANCE_PERCENTAGE ||
                    calculatedDynamicTolerance > Configuration.CIRCUIT_BREAKER_TOLERANCE_PERCENTAGE) {
                return true;
            }
        }

        return false;

    }

    public static void publishBestBidOffer(OrderBook orderBook,OrderEntry oe){

        long bestBid = orderBook.getBestBid();
        long bestBidQuantity = 0L;
        if(bestBid != 0L) {
            bestBidQuantity = orderBook.getBidTree().get(bestBid).get(0, oe).getQuantity();
        }

        long bestOffer = orderBook.getBestOffer();
        long bestOfferQuantity = 0L;
        if(bestOffer != 0L) {
            bestOfferQuantity = orderBook.getOfferTree().get(bestOffer).get(0, oe).getQuantity();
        }

        MarketData.INSTANCE.addBestBidOffer(bestBid, bestBidQuantity,
                bestOffer, bestOfferQuantity);

    }

    public static void publishBestBidOffer(OrderBook orderBook){
        publishBestBidOffer(orderBook,orderEntry);
    }

    public static boolean isEnableCircuitBreaker() {
        return enableCircuitBreaker;
    }

    public static void setEnableCircuitBreaker(boolean enableCircuitBreaker) {
        MatchingUtil.enableCircuitBreaker = enableCircuitBreaker;
    }
}
