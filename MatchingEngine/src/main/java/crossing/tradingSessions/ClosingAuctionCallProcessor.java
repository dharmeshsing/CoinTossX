package crossing.tradingSessions;

import com.carrotsearch.hppc.LongObjectHashMap;
import common.TimeInForce;
import crossing.MatchingUtil;
import crossing.expireRule.ClosingAuctionExpireRule;
import crossing.postProcessor.ExpireOrderPostProcessor;
import crossing.postProcessor.StopOrderPostProcessor;
import crossing.preProcessor.MatchingPreProcessor;
import crossing.strategy.AuctionStrategy;
import crossing.strategy.PriceTimePriorityStrategy;
import leafNode.OrderEntry;
import orderBook.OrderBook;
import validation.ClosingAuctionCallValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dharmeshsing on 14/11/15.
 */
public class ClosingAuctionCallProcessor implements TradingSessionProcessor {
    private ClosingAuctionCallValidator validator;
    private PriceTimePriorityStrategy priceTimePriorityStrategy;
    private AuctionStrategy auctionStrategy;
    private LongObjectHashMap<OrderBook> orderBooks;
    private StopOrderPostProcessor stopOrderPostProcessor;
    private ExpireOrderPostProcessor expireOrderPostProcessor;
    private ClosingAuctionExpireRule closingAuctionExpireRule;
    private List<TimeInForce> timeInForceList;

    public ClosingAuctionCallProcessor(LongObjectHashMap<OrderBook> orderBooks,
                                       PriceTimePriorityStrategy priceTimePriorityStrategy,
                                       AuctionStrategy auctionStrategy,
                                       StopOrderPostProcessor stopOrderPostProcessor,
                                       ExpireOrderPostProcessor expireOrderPostProcessor){

        this.validator = new ClosingAuctionCallValidator();
        this.priceTimePriorityStrategy = priceTimePriorityStrategy;
        this.auctionStrategy = auctionStrategy;
        this.orderBooks = orderBooks;
        this.stopOrderPostProcessor = stopOrderPostProcessor;
        this.expireOrderPostProcessor = expireOrderPostProcessor;
        this.closingAuctionExpireRule = new ClosingAuctionExpireRule();

        timeInForceList = new ArrayList<>();
        timeInForceList.add(TimeInForce.GFA);
        timeInForceList.add(TimeInForce.ATC);
    }


    @Override
    public void startSession(OrderBook orderBook) {
        MatchingUtil.injectOrders(orderBook, priceTimePriorityStrategy, timeInForceList);
    }

    @Override
    public void process(OrderBook orderBook,OrderEntry orderEntry) {
        priceTimePriorityStrategy.process(MatchingPreProcessor.MATCHING_ACTION.ADD_ORDER, orderBook, orderEntry);
    }

    @Override
    public void endSession(OrderBook orderBook) {
        auctionStrategy.process(priceTimePriorityStrategy, orderBook);
        stopOrderPostProcessor.postProcess(priceTimePriorityStrategy,orderBook);
        expireOrderPostProcessor.postProcess(priceTimePriorityStrategy,orderBook,closingAuctionExpireRule);
        MatchingUtil.parkGFAOrders(orderBook);
    }

    @Override
    public boolean isOrderValid(OrderEntry orderEntry, int template) {
        return validator.isMessageValidForSession(orderEntry,template);
    }
}
