package orderBook;

import bplusTree.BPlusTree;
import com.carrotsearch.hppc.LongArrayList;
import com.carrotsearch.hppc.ObjectArrayList;
import leafNode.OrderEntry;
import leafNode.OrderList;
import leafNode.OrderListImpl;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by dharmeshsing on 15/03/08.
 */
public class OrderBook {
    private long securityId;
    private long bestVisibleBid;
    private long bestVisibleOffer;
    private long bestBidQuantity;
    private long bestOfferQuantity;
    private long staticPriceReference;
    private long dynamicPriceReference;
    private long lasTradedPrice;
    private boolean locked;
    private boolean bestBidOfferChanged;
    private long totalQuantity;
    private long totalQuantityTimesPrice;
    private boolean circuitBreakerBreached;

    private Stock stock;

    private BPlusTree<Long, OrderList> bidTree;
    private BPlusTree<Long, OrderList> offerTree;
    private BPlusTree.BPlusTreeIterator bidTreeIterator;
    private BPlusTree.BPlusTreeIterator offerTreeIterator;


    private BPlusTree<Long, OrderList> parkedBidTree;
    private BPlusTree<Long, OrderList> parkedOfferTree;
    private BPlusTree.BPlusTreeIterator parkedBidTreeIterator;
    private BPlusTree.BPlusTreeIterator parkedOfferTreeIterator;

    private LongArrayList prices;
    private ObjectArrayList<Trade> trades;

    public long getSecurityId() {
        return securityId;
    }

    public void setSecurityId(long securityId) {
        this.securityId = securityId;
    }

    public BPlusTree<Long, OrderList> getParkedBidTree() {
        return parkedBidTree;
    }

    public BPlusTree<Long, OrderList> getParkedOfferTree() {
        return parkedOfferTree;
    }

    public long getStaticPriceReference() {
        return staticPriceReference;
    }

    public void setStaticPriceReference(long staticPriceReference) {
        this.staticPriceReference = staticPriceReference;
    }

    public long getDynamicPriceReference() {
        return dynamicPriceReference;
    }

    public void setDynamicPriceReference(long dynamicPriceReference) {
        this.dynamicPriceReference = dynamicPriceReference;
    }

    public boolean isCircuitBreakerBreached() {
        return circuitBreakerBreached;
    }

    public void setCircuitBreakerBreached(boolean circuitBreakerBreached) {
        this.circuitBreakerBreached = circuitBreakerBreached;
    }

    public enum SIDE {BID,OFFER};

    public OrderBook(long securityId) {
        Comparator<Long> priceComp = Comparator.comparingLong(e -> e.longValue());
        bidTree = new BPlusTree(100, priceComp.reversed());
        offerTree = new BPlusTree(100, priceComp);
        this.bidTreeIterator = (BPlusTree.BPlusTreeIterator) bidTree.iterator();
        this.offerTreeIterator = (BPlusTree.BPlusTreeIterator) offerTree.iterator();

        parkedBidTree = new BPlusTree(100, priceComp.reversed());
        parkedOfferTree = new BPlusTree(100, priceComp);
        parkedBidTreeIterator = (BPlusTree.BPlusTreeIterator) getParkedBidTree().iterator();
        parkedOfferTreeIterator = (BPlusTree.BPlusTreeIterator) getParkedOfferTree().iterator();

        this.setSecurityId(securityId);
        this.setBestVisibleBid(-1L);
        this.setBestVisibleOffer(-1L);

        this.prices = new LongArrayList();
        this.setTrades(new ObjectArrayList<>(0));
        staticPriceReference = -1;
        dynamicPriceReference = -1;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isLocked() {
        return this.locked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderBook orderBook = (OrderBook) o;

        if (getSecurityId() != orderBook.getSecurityId()) return false;

        if (!equalsTree(bidTreeIterator, orderBook.getBidTreeIterator())) {
            return false;
        }

        if (!equalsTree(offerTreeIterator, orderBook.getOfferTreeIterator())) {
            return false;
        }

        if (!equalsTree(parkedBidTreeIterator, orderBook.getParkedBidTreeIterator())) {
            return false;
        }

        if (!equalsTree(parkedOfferTreeIterator, orderBook.getParkedOfferTreeIterator())) {
            return false;
        }

        return true;
    }

    private boolean equalsTree(BPlusTree.BPlusTreeIterator b1, BPlusTree.BPlusTreeIterator b2) {
        boolean result = true;
        b1.reset();
        b2.reset();

        while (b1.hasNext() && b2.hasNext()) {
            Map.Entry<Long, OrderList> value1 = b1.next();
            Map.Entry<Long, OrderList> value2 = b2.next();

            if (!value1.getKey().equals(value2.getKey())) {
                result = false;
                break;
            }

            value1.getValue().trimToSize();
            value2.getValue().trimToSize();

            if (!value1.getValue().equals(value2.getValue())) {
                result = false;
                break;
            }
        }

        return result;
    }

    public BPlusTree.BPlusTreeIterator getBidTreeIterator() {
        return bidTreeIterator;
    }

    public BPlusTree.BPlusTreeIterator getOfferTreeIterator() {
        return offerTreeIterator;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }


    public long getBestVisibleBid() {
        return bestVisibleBid;
    }

    public void setBestVisibleBid(long bestVisibleBid) {
        this.bestVisibleBid = bestVisibleBid;
    }

    public long getBestVisibleOffer() {
        return bestVisibleOffer;
    }

    public void setBestVisibleOffer(long bestVisibleOffer) {
        this.bestVisibleOffer = bestVisibleOffer;
    }


    public long getBestBid() {
        if (getBidTree().getFirstKey() != null) {
            return getBidTree().getFirstKey();
        } else {
            return 0L;
        }
    }

    public long getBestOffer() {
        if (getOfferTree().getFirstKey() != null) {
            return getOfferTree().getFirstKey();
        } else {
            return 0L;
        }
    }

    public BPlusTree<Long, OrderList> getBidTree() {
        return bidTree;
    }

    public BPlusTree<Long, OrderList> getOfferTree() {
        return offerTree;
    }

    public void addPrice(long price) {
        prices.add(price);
    }

    public void removePrice(long price) {
        prices.removeFirst(price);
    }

    public long[] getPrices() {
        return this.prices.buffer;
    }

    public LongArrayList getPriceList() {
        return this.prices;
    }

    public Stock getStock() {
        return this.stock;
    }

    public void free(BPlusTree.BPlusTreeIterator b1,SIDE side,boolean isParked) {
        b1.reset();

        while (b1.hasNext()) {
            Map.Entry<Long, OrderList> priceList = b1.next();
            if(isParked){
                removeParkedPrice(priceList.getKey(), side);
            }else {
                removePrice(priceList.getKey(), side);
            }
        }
    }

    public void freeAll(){
        free(bidTreeIterator, SIDE.BID,false);
        free(offerTreeIterator, SIDE.OFFER,false);

        free(parkedBidTreeIterator, SIDE.BID,true);
        free(parkedOfferTreeIterator, SIDE.OFFER,true);

        prices.clear();
        trades.clear();
    }

    @Override
    public String toString() {
        return "OrderBook{" +
                "bidTree=" + printTree((BPlusTree.BPlusTreeIterator)bidTree.iterator()) +
                ", offerTree=" + printTree((BPlusTree.BPlusTreeIterator)offerTree.iterator()) +
                ", parkedBidTree=" + printTree((BPlusTree.BPlusTreeIterator) getParkedBidTree().iterator()) +
                ", parkedOfferTree=" + printTree((BPlusTree.BPlusTreeIterator) getParkedOfferTree().iterator()) +
                '}';
    }

    private String printTree(BPlusTree.BPlusTreeIterator b1){
        String result = "";
        b1.reset();

        while (b1.hasNext()) {
            Map.Entry<Long, OrderList> priceList = b1.next();
            if (priceList != null) {
               result += priceList.getValue().toString();
            }
        }

        return result;
    }

    public long getLasTradedPrice() {
        return lasTradedPrice;
    }

    public void setLasTradedPrice(long lasTradedPrice) {
        this.lasTradedPrice = lasTradedPrice;
    }

    public BPlusTree.BPlusTreeIterator getPriceIterator(SIDE side){
        if(side == SIDE.BID){
            bidTreeIterator.reset();
            return bidTreeIterator;
        } else{
            offerTreeIterator.reset();
            return offerTreeIterator;
        }
    }

    public BPlusTree.BPlusTreeIterator getContraSidePriceIterator(SIDE side){
        if(side == SIDE.BID){
            return getPriceIterator(SIDE.OFFER);
        } else{
            return getPriceIterator(SIDE.BID);
        }
    }

    public boolean isValidProcessingPrice(long aggPrice, long existingPrice, SIDE aggSide){
        if(aggSide == SIDE.BID && aggPrice >= existingPrice || aggSide == SIDE.OFFER && aggPrice <= existingPrice) {
            return true;
        }
        return false;
    }

    public long getBestVisibleBidOffer(SIDE side) {
        if (side == SIDE.BID) {
            return getBestVisibleBid();
        } else {
            return getBestVisibleOffer();
        }
    }

    public void getBestBidQuantity(OrderEntry orderEntry){
        if(getBestBid() != 0L) {
            bidTree.get(getBestBid()).get(0, orderEntry);
        }
    }

    public void getBestOfferQuantity(OrderEntry orderEntry){
        if(getBestOffer() != 0L) {
            offerTree.get(getBestOffer()).get(0, orderEntry);
        }
    }

    public void addParkedOrder(OrderEntry orderEntry,SIDE side) {
        if (side == SIDE.BID) {
            addParkedOrder(getParkedBidTree(), orderEntry);
        } else {
            addParkedOrder(getParkedOfferTree(), orderEntry);
        }
    }

    private void addParkedOrder(BPlusTree<Long, OrderList> btree, OrderEntry orderEntry){
        OrderList orderList = btree.get(orderEntry.getPrice());

        if(orderList == null){
            orderList = new OrderListImpl();
            orderList.add(orderEntry);
            btree.put(orderEntry.getPrice(), orderList);
        }else{
            orderList.add(orderEntry);
        }
    }

    public void removePrice(long price,SIDE side){
        if(side == SIDE.BID){
            removeBidTreePrice(price);
        }else{
            removeOfferTreePrice(price);
        }
    }

    public void removeParkedPrice(long price,SIDE side){
        if(side == SIDE.BID){
            removeParkedBidTreePrice(price);
        }else{
            removeParkedOfferTreePrice(price);
        }
    }

    private void removeOfferTreePrice(long price){
        if(price == getBestOffer()){
            setBestBidOfferChanged(true);
        }

        offerTree.get(price).free();
        offerTree.remove(price);
        offerTreeIterator.remove();

        if(bidTree.get(price) == null) {
            removePrice(price);
        }
    }

    private void removeBidTreePrice(long price){
        if(price == getBestBid()){
            setBestBidOfferChanged(true);
        }

        bidTree.get(price).free();
        bidTree.remove(price);
        bidTreeIterator.remove();

        if(offerTree.get(price) == null){
            removePrice(price);
        }
    }

    private void removeParkedBidTreePrice(long price){
        getParkedBidTree().get(price).free();
        getParkedBidTree().remove(price);
        parkedBidTreeIterator.remove();
    }

    private void removeParkedOfferTreePrice(long price){
        getParkedOfferTree().get(price).free();
        getParkedOfferTree().remove(price);
        parkedOfferTreeIterator.remove();
    }

    public boolean isBestBidOfferChanged() {
        return bestBidOfferChanged;
    }

    public void setBestBidOfferChanged(boolean bestBidOfferChanged) {
        this.bestBidOfferChanged = bestBidOfferChanged;
    }

    public ObjectArrayList<Trade> getTrades() {
        return trades;
    }

    public void setTrades(ObjectArrayList<Trade> trades) {
        this.trades = trades;
    }

    public BPlusTree.BPlusTreeIterator getParkedBidTreeIterator() {
        return parkedBidTreeIterator;
    }

    public BPlusTree.BPlusTreeIterator getParkedOfferTreeIterator() {
        return parkedOfferTreeIterator;
    }

    public static SIDE getSide(int side){
        return side== 1 ? OrderBook.SIDE.BID : OrderBook.SIDE.OFFER;
    }
}
