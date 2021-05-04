package crossing.strategy;

import bplusTree.BPlusTree;
import com.carrotsearch.hppc.ObjectArrayList;
import com.carrotsearch.hppc.ObjectObjectMap;
import com.carrotsearch.hppc.ObjectObjectScatterMap;
import leafNode.OrderList;
import orderBook.OrderBook;

import java.util.Arrays;

public class AuctionStrategy {

    private ObjectObjectMap<Long, AuctionData> auctionMap;
    private boolean aggressBuySide;

    public AuctionStrategy(){
        this.auctionMap = new ObjectObjectScatterMap<>();
    }

    public void process(PriceTimePriorityStrategy priceTimePriorityStrategy, OrderBook orderBook) {
        init();

        orderBook.setStaticPriceReference(-1);
        orderBook.setDynamicPriceReference(-1);

        long[] prices = getPrices(orderBook);
        setAggregateBuyQuantity(prices,orderBook);
        setAggregateSellQuantity(prices, orderBook);
        Object[] data = auctionMap.values().toArray();
        Arrays.sort(data);
        long targetPrice = getExecutionPrice(data);
        priceTimePriorityStrategy.setTargetPrice(targetPrice);
        setReferencePrice(orderBook,targetPrice);

        if(aggressBuySide){
            priceTimePriorityStrategy.buySideAgressSellSide();
        }else{
            priceTimePriorityStrategy.sellSideAgressBuySide();
        }

        priceTimePriorityStrategy.setTargetPrice(0);
    }

    private void setReferencePrice(OrderBook orderBook,long price){
        if(price != 0){
            orderBook.setStaticPriceReference(price);
            orderBook.setDynamicPriceReference(price);
        }
    }

    private void init() {
        auctionMap.clear();
        aggressBuySide = false;
    }

    private long[] getPrices(OrderBook orderBook){
        long[] prices = orderBook.getPrices();
        Arrays.sort(prices);
        return prices;
    }

    private void setAggregateBuyQuantity(long[] prices,OrderBook orderBook){
        BPlusTree<Long, OrderList> bidTree = orderBook.getBidTree();
        long aggregateBuy = 0L;

        for(int i=prices.length - 1; i>=0; i--){
            if(prices[i] == 0){
                continue;
            }

            OrderList bidList = bidTree.get(prices[i]);

            if(bidList != null){
                aggregateBuy += bidList.total();
            }

            AuctionData data = auctionMap.get(prices[i]);
            if(data == null) {
                data = new AuctionData(prices[i]);
                auctionMap.put(prices[i], data);
            }
            data.setAggregateBuy(aggregateBuy);
        }
    }

    private void setAggregateSellQuantity(long[] prices,OrderBook orderBook){
        BPlusTree<Long, OrderList>  offerTree = orderBook.getOfferTree();
        long aggregateSell = 0L;

        for(int i=0; i<prices.length; i++){
            if(prices[i] == 0){
                continue;
            }
            OrderList offerList = offerTree.get(prices[i]);

            if(offerList != null){
                aggregateSell += offerList.total();
            }

            AuctionData data = auctionMap.get(prices[i]);
            if(data == null) {
                data = new AuctionData(prices[i]);
                auctionMap.put(prices[i], data);
            }
            data.setAggregateSell(aggregateSell);
        }
    }

    private long getExecutionPrice(Object[] data){

        ObjectArrayList<AuctionData> auctionList = new ObjectArrayList<>();
        long maxVolume = 0L;
        long minSurplus = 0L;
        long maxPrice = 0L;
        long minPrice = 0L;

        for(int i=0; i<data.length - 1; i++){
            AuctionData auctionData = (AuctionData)data[i];
            if(maxVolume != 0 && maxVolume != auctionData.getVolume()){
                break;
            }
            maxVolume = auctionData.getVolume();
            if(maxPrice == 0 || maxPrice < auctionData.getPrice()){
                maxPrice = auctionData.getPrice();
            }

            if(minPrice == 0 || minPrice > auctionData.getPrice()){
                minPrice = auctionData.getPrice();
            }

            minSurplus += auctionData.getSurplus();
            auctionList.add(auctionData);
        }

        if(auctionList.size() == 1){
            return auctionList.get(0).getPrice();
        }

        if(minSurplus > 0){
            aggressBuySide = false;
            return maxPrice;
        }else if (minSurplus < 0){
            aggressBuySide = true;
            return minPrice;
        }else{
            //TODO: return reference price;
            return 0;
        }
    }
}
