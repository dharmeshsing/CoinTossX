package crossing.strategy;

import bplusTree.BPlusTree;
import common.OrderType;
import config.Configuration;
import crossing.MatchingUtil;
import crossing.preProcessor.MatchingPreProcessor.MATCHING_ACTION;
import data.ExecutionReportData;
import data.MarketData;
import leafNode.OrderEntry;
import leafNode.OrderList;
import leafNode.OrderListCursor;
import leafNode.OrderListImpl;
import orderBook.OrderBook;
import orderBook.Trade;
import sbe.msg.ExecutionTypeEnum;
import sbe.msg.OrderStatusEnum;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class PriceTimePriorityStrategy implements MatchingLogic {
    private long targetPrice;

    private AtomicLong tradeId;
    private OrderBook orderBook;
    private BPlusTree<Long, OrderList> bidTree;
    private BPlusTree<Long, OrderList> offerTree;
    private BPlusTree.BPlusTreeIterator bidTreeIterator;
    private BPlusTree.BPlusTreeIterator offerTreeIterator;

    public PriceTimePriorityStrategy(){
        this.tradeId = new AtomicLong();
    }

    @Override
    public void process(MATCHING_ACTION action,OrderBook orderBook, OrderEntry orderEntry) {
        init(orderBook);

        switch(action){
            case AGGRESS_ORDER: {
                agressOrder(orderEntry);
                addOrder(orderEntry);
                break;
            }
            case NO_ACTION: break;
            case ADD_ORDER: {
                addOrder(orderEntry);
                break;
            }
            case ADD_AND_AGGRESS: {
                addOrder(orderEntry);
                if(getOrderSide(orderEntry.getSide()) == OrderBook.SIDE.BID){
                    sellSideAgressBuySide();
                }else{
                    buySideAgressSellSide();
                }
                break;
            }
            case PARK_ORDER: {
                parkOrder(orderEntry);
                break;
            }
        }

        if(orderEntry.getQuantity() == 0){
            orderEntry.setOrderStatus((byte)OrderStatusEnum.Filled.value());
        }
    }

    private void parkOrder(OrderEntry orderEntry){
        OrderBook.SIDE side = OrderBook.getSide(orderEntry.getSide());
        orderBook.addParkedOrder(orderEntry, side);
    }

    public void init(OrderBook orderBook){
        this.orderBook = orderBook;
        this.bidTree = orderBook.getBidTree();
        this.offerTree = orderBook.getOfferTree();
        this.bidTreeIterator = orderBook.getPriceIterator(OrderBook.SIDE.BID);
        this.offerTreeIterator = orderBook.getPriceIterator(OrderBook.SIDE.OFFER);
    }

    private OrderBook.SIDE getOrderSide(int side){
        return side == 1 ? OrderBook.SIDE.BID : OrderBook.SIDE.OFFER;
    }

    private OrderBook.SIDE getContraSide(int side){
        return getOrderSide(side) == OrderBook.SIDE.BID ? OrderBook.SIDE.OFFER : OrderBook.SIDE.BID;
    }

    private void addOrder(OrderEntry orderEntry){
        if(orderEntry.getQuantity() > 0 && !TimeInForceStrategy.removeOrder(orderEntry.getQuantity(),orderEntry.getTimeInForce())) {
            if (getOrderSide(orderEntry.getSide()) == OrderBook.SIDE.BID) {
                addBuyOrder(orderEntry);
            } else {
                addSellOrder(orderEntry);
            }

            MarketData.INSTANCE.buildAddOrder(orderEntry, false);
        }
    }

    private void addBuyOrder(OrderEntry orderEntry){
        long price = orderEntry.getPrice();
        updatePrices(price);
        updateBestBidOffer(price, true);
        updateBestVisibleBid(orderEntry.getType(), price);
        addOrderEntry(bidTree, orderEntry);
    }

    private void addSellOrder(OrderEntry orderEntry){
        long price = orderEntry.getPrice();
        updatePrices(price);
        updateBestBidOffer(price, false);
        updateBestVisibleOffer(orderEntry.getType(), price);
        addOrderEntry(offerTree, orderEntry);
    }

    private void updatePrices(long price){
        if (!orderBook.getPriceList().contains(price)) {
            orderBook.addPrice(price);
        }
    }

    private void addOrderEntry(BPlusTree<Long, OrderList> btree, OrderEntry orderEntry){
        OrderList orderList = btree.get(orderEntry.getPrice());

        if(orderList == null){
            orderList = new OrderListImpl();
            orderList.add(orderEntry);
            btree.put(orderEntry.getPrice(), orderList);
        }else{
            orderList.add(orderEntry);
        }
    }

    private void updateBestBidOffer(long price,boolean isBuySide){
        if(isBuySide && orderBook.getBestBid() == 0){
            orderBook.setBestBidOfferChanged(true);
        } else if (!isBuySide && orderBook.getBestOffer() == 0){
            orderBook.setBestBidOfferChanged(true);
        } else if(isBuySide && price >= orderBook.getBestBid()){
            orderBook.setBestBidOfferChanged(true);
        } else if(!isBuySide && price <= orderBook.getBestOffer()){
            orderBook.setBestBidOfferChanged(true);
        }
    }

    private void updateBestVisibleBid(long orderType, long price){
        if(orderType != OrderType.HIDDEN_LIMIT.getOrderType()){
            long bestVisibleBid = orderBook.getBestVisibleBid();
            if(bestVisibleBid == -1 || bestVisibleBid < price){
                orderBook.setBestVisibleBid(price);
            }
        }
    }

    private void updateBestVisibleOffer(long orderType, long price){
        if(orderType != OrderType.HIDDEN_LIMIT.getOrderType()){
            long bestVisibleOffer = orderBook.getBestVisibleOffer();
            if(bestVisibleOffer == -1 || bestVisibleOffer > price){
                orderBook.setBestVisibleOffer(price);
            }
        }
    }

    public void setTargetPrice(long targetPrice){
        this.targetPrice = targetPrice;
    }

    public void sellSideAgressBuySide(){
        aggressOrders(offerTreeIterator, orderBook.getBestBid(), OrderBook.SIDE.OFFER);
    }

    public void buySideAgressSellSide(){
        aggressOrders(bidTreeIterator, orderBook.getBestOffer(), OrderBook.SIDE.BID);
    }

    private void aggressOrders(BPlusTree.BPlusTreeIterator iterator,long bestPrice, OrderBook.SIDE side){
        iterator.reset();

        while (iterator.hasNext()) {
            Map.Entry<Long, OrderList> entry = iterator.next();

            if(!orderBook.isValidProcessingPrice(entry.getKey(), bestPrice, side)) {
                break;
            }

            OrderList orderList = entry.getValue();
            Iterator<OrderListCursor> orderListIterator = orderList.iterator();
            while (orderListIterator.hasNext()) {
                OrderEntry aggOrder = orderListIterator.next().value;
                if (aggOrder.getExecuteVolume() > 0) {
                    agressOrder(aggOrder);

                    if (shouldRemoveExistingOrder(aggOrder)) {
                        orderListIterator.remove();
                    }
                }
            }

            if (orderList.total() == 0) {
                orderBook.removePrice(entry.getKey(), side);
            }
        }
    }

    public void agressOrder(OrderEntry aggOrder){
        OrderBook.SIDE orderSide = getOrderSide(aggOrder.getSide());
        OrderBook.SIDE contraSide = getContraSide(aggOrder.getSide());
        BPlusTree.BPlusTreeIterator iterator = orderBook.getContraSidePriceIterator(orderSide);

        while (iterator.hasNext()) {
            Map.Entry<Long, OrderList> entry = iterator.next();

            if(aggOrder.getType() == OrderType.MARKET.getOrderType()) {
                aggOrder.setPrice(entry.getKey());
            }

            if(aggOrder.getQuantity() == 0){
                break;
            }

            if(!MatchingUtil.isValidProcessingPrice(aggOrder.getPrice(), entry.getKey(), orderSide)) {
                break;
            }

            long price = getExecutionPrice(entry.getKey(),contraSide);

            if(MatchingUtil.doesPriceBreachCircuitBreaker(orderBook, price)){
                orderBook.setCircuitBreakerBreached(true);
                break;
            }

            processOrdersInList(price, entry.getValue(), aggOrder);

            if (entry.getValue().total() == 0) {
                orderBook.removePrice(entry.getKey(), contraSide);
            }
        }
    }

    private void processOrdersInList(long price, OrderList orderList,OrderEntry aggOrder){
        Iterator<OrderListCursor> iterator = orderList.iterator();
        while (iterator.hasNext()) {
            OrderEntry currentOrder = iterator.next().value;
            int quantity = MatchingUtil.getExecutionQuantity(currentOrder.getQuantity(), aggOrder.getQuantity(), currentOrder.getMinExecutionSize(),aggOrder.getMinExecutionSize());
            long orderId = currentOrder.getOrderId();
            if(quantity > 0) {
                addTrade(price, quantity, orderId);
                aggOrder.removeQuantity(quantity);
                currentOrder.removeQuantity(quantity);

                if (shouldRemoveExistingOrder(currentOrder)) {
                    iterator.remove();
                }
            }
        }
    }

    private long getExecutionPrice(long price, OrderBook.SIDE side){
        if(targetPrice != 0){
            return targetPrice;
        } else if(isPassivePriceImprovement(price,side)){
            return getPriceImprovementPrice(price);
        } else{
            return price;
        }
    }

    private boolean isPassivePriceImprovement(long executionPrice, OrderBook.SIDE side){
        return (side == OrderBook.SIDE.BID && orderBook.getBestVisibleBid() > 0 && executionPrice > orderBook.getBestVisibleBid()) ||
               (side == OrderBook.SIDE.OFFER && orderBook.getBestVisibleOffer() > 0 && executionPrice < orderBook.getBestVisibleOffer());
    }

    private long getPriceImprovementPrice(long price){
        if(price < orderBook.getBestVisibleBid()){
            return orderBook.getBestVisibleBid() + Configuration.PASSIVE_PRICE_IMPROVEMENT;
        }

        if(price > orderBook.getBestVisibleOffer()){
            return orderBook.getBestVisibleOffer() - Configuration.PASSIVE_PRICE_IMPROVEMENT;
        }

        return price;
    }

    public boolean shouldRemoveExistingOrder(OrderEntry orderEntry){
        int quantity = orderEntry.getQuantity();
        if(quantity == 0){
            return true;
        }

        if(orderEntry.getType() == OrderType.HIDDEN_LIMIT.getOrderType()){
            if(quantity < orderBook.getStock().getMRS() || quantity < orderEntry.getMinExecutionSize()){
                return true;
            }
        }

        return false;
    }

    //TODO: Send the trades out instead of storing it
    private void addTrade(long price, long quantity, long orderId){
        Trade trade = new Trade();
        trade.setId(orderId);
        trade.setPrice(price);
        trade.setQuantity(quantity);
        orderBook.setLasTradedPrice(price);
        orderBook.getTrades().add(trade);
        ExecutionReportData.INSTANCE.setExecutionType(ExecutionTypeEnum.Trade);
        ExecutionReportData.INSTANCE.addFillGroup(price,(int)quantity);
        MarketData.INSTANCE.addTrade(trade.getId(),price,quantity);
        setReferencePrice(price);

    }

    private void setReferencePrice(long price){
        if(orderBook.getStaticPriceReference() == -1){
            orderBook.setStaticPriceReference(price);
        }
        orderBook.setDynamicPriceReference(price);
    }
}
