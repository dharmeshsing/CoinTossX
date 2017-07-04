package crossing.strategy;

import bplusTree.BPlusTree;
import common.OrderType;
import leafNode.OrderEntry;
import leafNode.OrderList;
import leafNode.OrderListCursor;
import orderBook.OrderBook;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by dharmeshsing on 8/07/15.
 */
public class FilterAndUncrossStrategy {

    private long maxVolume;
    private long maxPrice;
    private long minPrice;
    private long bidExecuteVolume;
    private long offerExecuteVolume;
    private OrderBook orderBook;
    private PriceTimePriorityStrategy priceTimePriorityStrategy;


    public void process(PriceTimePriorityStrategy priceTimePriorityStrategy,OrderBook orderBook) {
        init(priceTimePriorityStrategy, orderBook);

        BPlusTree.BPlusTreeIterator bidTreeIterator = orderBook.getPriceIterator(OrderBook.SIDE.BID);
        BPlusTree.BPlusTreeIterator offerTreeIterator = orderBook.getPriceIterator(OrderBook.SIDE.OFFER);
        long bestBid = orderBook.getBestBid();
        long bestOffer = orderBook.getBestOffer();

        if(bestBid == 0 || bestOffer == 0){
            return;
        }

        long bidExecuteVolume = getExecuteVolumeSize(bidTreeIterator,true,bestOffer);
        long offerExecuteVolume = getExecuteVolumeSize(offerTreeIterator,false,bestBid);

        boolean bidUpdate = true;
        boolean offerUpdate = true;
        if(bidExecuteVolume > 0 && offerExecuteVolume > 0) {
            if (bidExecuteVolume > offerExecuteVolume) {
                while(offerUpdate) {
                    bidUpdate = setBidExecuteVolume(bidTreeIterator, offerTreeIterator, bestOffer);
                    offerUpdate = setOfferExecuteVolume(bidTreeIterator, offerTreeIterator, bestBid);
                }
            } else {
                while(bidUpdate) {
                    offerUpdate = setOfferExecuteVolume(bidTreeIterator, offerTreeIterator, bestBid);
                    bidUpdate = setBidExecuteVolume(bidTreeIterator, offerTreeIterator, bestOffer);
                }
            }

            if (bidUpdate || offerUpdate) {
                runUncross();
            }

            resetOrderBook(bidTreeIterator,offerTreeIterator);
        }
    }

    private void init(PriceTimePriorityStrategy priceTimePriorityStrategy,OrderBook orderBook){
        this.orderBook = orderBook;
        this.maxPrice = 0;
        this.minPrice = 0;
        this.maxVolume = 0;
        this.bidExecuteVolume = 0;
        this.offerExecuteVolume = 0;
        this.priceTimePriorityStrategy = priceTimePriorityStrategy;
    }

    public void resetOrderBook(BPlusTree.BPlusTreeIterator bidTreeIterator,BPlusTree.BPlusTreeIterator offerTreeIterator){
        resetExecutionVolume(bidTreeIterator);
        resetExecutionVolume(offerTreeIterator);
    }

    private void resetExecutionVolume(BPlusTree.BPlusTreeIterator iterator){
        iterator.reset();
        while (iterator.hasNext()) {
            Map.Entry<Long, OrderList> entry = iterator.next();

            Iterator<OrderListCursor> orderListIterator = entry.getValue().iterator();
            while(orderListIterator.hasNext()){
                OrderEntry currentOrder = orderListIterator.next().value;
                currentOrder.setExecuteVolume(currentOrder.getQuantity());
            }
        }
    }

    private void runUncross(){
        long targetPrice = getTargetPrice(orderBook);
        if(targetPrice > 0) {
            priceTimePriorityStrategy.setTargetPrice(targetPrice);

            if (getOfferExecuteVolume() < getBidExecuteVolume()) {
                priceTimePriorityStrategy.sellSideAgressBuySide();
            } else {
                priceTimePriorityStrategy.buySideAgressSellSide();
            }

            priceTimePriorityStrategy.setTargetPrice(0);
        }

    }

    public long getTargetPrice(OrderBook orderBook){
        long visibleBestBid = orderBook.getBestVisibleOffer();
        long visibleBestOffer = orderBook.getBestVisibleBid();

        long target = 0;
        //TODO: Fix the rest of the logic
        if(visibleBestBid != 0 && visibleBestOffer != 0){
            target =  visibleBestOffer - ((visibleBestOffer - visibleBestBid) / 2);
        }else if(visibleBestBid != 0 && visibleBestOffer == 0){
            target = visibleBestBid;
        }else if(visibleBestBid == 0 && visibleBestOffer != 0){
            target = visibleBestOffer;
        }else if(visibleBestBid == 0 && visibleBestOffer == 0){
            target = -1;
        }

        if(target >= maxPrice) {
            target = maxPrice;
        }else if(target <= minPrice){
            target = minPrice;
        }


        return target;
    }

    private boolean setExecuteVolume(BPlusTree.BPlusTreeIterator iterator1,BPlusTree.BPlusTreeIterator iterator2,boolean isBuySide,long stopPrice){
        int volumeAhead = 0;
        boolean result = false;
        iterator1.reset();
        iterator2.reset();

        while (iterator1.hasNext()) {
            Map.Entry<Long, OrderList> entry1 = iterator1.next();

            if((isBuySide && entry1.getKey() < stopPrice) ||
                    (!isBuySide && entry1.getKey() > stopPrice)) {
                break;
            }

            int volumeAvailable = getExecuteVolumeSize(iterator2, !isBuySide, entry1.getKey());

            boolean isHiddenOrder = false;
            OrderList orderList = entry1.getValue();
            Iterator<OrderListCursor> orderListIterator = orderList.iterator();
            while(orderListIterator.hasNext()){
                OrderEntry currentOrder = orderListIterator.next().value;

                if (currentOrder.getType() == OrderType.HIDDEN_LIMIT.getOrderType()) {
                    isHiddenOrder = true;
                    int volume = volumeAvailable - volumeAhead;
                    if (volume > 0 && volume >= currentOrder.getMinExecutionSize()) {
                        int executeVolume = Math.min(volume, currentOrder.getQuantity());
                        if(currentOrder.getQuantity() - executeVolume != 0){
                            result = true;
                            currentOrder.setExecuteVolume(executeVolume);
                        }
                    } else {
                        currentOrder.setExecuteVolume(0);
                    }
                }
            }
            volumeAhead += orderList.getTotalExecuteVolume();

            long currentVolume = 0L;
            if(isHiddenOrder && orderList.getTotalExecuteVolume() == 0){
                currentVolume = volumeAvailable;
            }else if(isHiddenOrder) {
                currentVolume = volumeAhead;
            }

            if(currentVolume > maxVolume) {
                maxVolume = currentVolume;
                long currentPrice = entry1.getKey();

                if (currentPrice > maxPrice) {
                    maxPrice = currentPrice;
                }

                if(minPrice == 0 || currentPrice < minPrice) {
                    minPrice = currentPrice;
                }
            }


        }

        if(isBuySide){
            setBidExecuteVolume(volumeAhead);
        }else{
            setOfferExecuteVolume(volumeAhead);
        }

        return result;
    }

    private boolean setBidExecuteVolume(BPlusTree.BPlusTreeIterator bidIterator,BPlusTree.BPlusTreeIterator offerIterator,long bestOffer){
        return setExecuteVolume(bidIterator,offerIterator,true,bestOffer);
    }

    private boolean setOfferExecuteVolume(BPlusTree.BPlusTreeIterator bidIterator,BPlusTree.BPlusTreeIterator offerIterator,long bestBid){
        return setExecuteVolume(offerIterator,bidIterator,false,bestBid);
    }

    private int getExecuteVolumeSize(BPlusTree.BPlusTreeIterator iterator,boolean isBuySide,long price){
        int executeVolumeSize = 0;

        iterator.reset();
        while (iterator.hasNext()) {
            Map.Entry<Long, OrderList> entry = iterator.next();
            if((isBuySide && entry.getKey() < price) || (!isBuySide && entry.getKey() > price)) {
                break;
            }

            executeVolumeSize += entry.getValue().getTotalExecuteVolume();
        }

        return executeVolumeSize;
    }

    public long getBidExecuteVolume() {
        return bidExecuteVolume;
    }

    public void setBidExecuteVolume(long bidExecuteVolume) {
        this.bidExecuteVolume = bidExecuteVolume;
    }

    public long getOfferExecuteVolume() {
        return offerExecuteVolume;
    }

    public void setOfferExecuteVolume(long offerExecuteVolume) {
        this.offerExecuteVolume = offerExecuteVolume;
    }
}
