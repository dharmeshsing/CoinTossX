package crossing;

import com.carrotsearch.hppc.LongObjectHashMap;
import common.OrderType;
import crossing.preProcessor.MatchingPreProcessor;
import leafNode.OrderEntry;
import orderBook.OrderBook;
import sbe.msg.marketData.TradingSessionEnum;

/**
 * Created by dharmeshsing on 25/08/15.
 */
public enum MatchingContext {
    INSTANCE;

    private OrderBook orderBook;
    private OrderEntry orderEntry;
    private MatchingPreProcessor.MATCHING_ACTION action;
    private int templateId;
    private OrderBook.SIDE side;
    private long price;
    private long bideTreeSize;
    private long offerTreeSize;
    private long bestBid;
    private long bestOffer;
    private OrderType orderType;
    private LongObjectHashMap<TradingSessionEnum> orderBookTradingSession = new LongObjectHashMap<>();

    public OrderBook getOrderBook() {
        return orderBook;
    }

    public void setOrderBook(OrderBook orderBook) {
        this.orderBook = orderBook;

        bideTreeSize = orderBook.getBidTree().size();
        offerTreeSize = orderBook.getOfferTree().size();
        bestBid = orderBook.getBestBid();
        bestOffer = orderBook.getBestOffer();
    }

    public OrderEntry getOrderEntry() {
        return orderEntry;
    }

    public void setOrderEntry(OrderEntry orderEntry) {
        this.orderEntry = orderEntry;

        side = orderEntry.getSide() == 1 ? OrderBook.SIDE.BID : OrderBook.SIDE.OFFER;
        price = orderEntry.getPrice();
        orderType = OrderType.getOrderType(orderEntry.getType());
    }

    public void setAction(MatchingPreProcessor.MATCHING_ACTION action) {
         this.action = action;
    }

    public MatchingPreProcessor.MATCHING_ACTION getAction() {
        return action;
    }

    public OrderBook.SIDE getSide() {
        return side;
    }

    public long getPrice() {
        return price;
    }

    public long getBideTreeSize() {
        return bideTreeSize;
    }

    public long getOfferTreeSize() {
        return offerTreeSize;
    }

    public long getBestBid() {
        return bestBid;
    }

    public long getBestOffer() {
        return bestOffer;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public void setOrderBookTradingSession(long securityId, TradingSessionEnum tradingSession){
        orderBookTradingSession.put(securityId,tradingSession);
    }

    public TradingSessionEnum getOrderBookTradingSession(long securityId){
        return orderBookTradingSession.get(securityId);
    }
}
