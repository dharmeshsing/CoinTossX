package crossing.tradingSessions;

import com.carrotsearch.hppc.LongObjectHashMap;
import crossing.MatchingUtil;
import crossing.expireRule.OpeningAuctionExpireRule;
import crossing.postProcessor.ExpireOrderPostProcessor;
import crossing.postProcessor.StopOrderPostProcessor;
import crossing.preProcessor.MatchingPreProcessor;
import crossing.strategy.AuctionStrategy;
import crossing.strategy.PriceTimePriorityStrategy;
import leafNode.OrderEntry;
import orderBook.OrderBook;
import validation.OpeningAuctionCallValidator;

public class OpeningAuctionCallProcessor implements TradingSessionProcessor {
    private OpeningAuctionCallValidator validator;
    private PriceTimePriorityStrategy priceTimePriorityStrategy;
    private AuctionStrategy auctionStrategy;
    private LongObjectHashMap<OrderBook> orderBooks;
    private StopOrderPostProcessor stopOrderPostProcessor;
    private ExpireOrderPostProcessor expireOrderPostProcessor;
    private OpeningAuctionExpireRule openingAuctionExpireRule;

    public OpeningAuctionCallProcessor(LongObjectHashMap<OrderBook> orderBooks,
                                       PriceTimePriorityStrategy priceTimePriorityStrategy,
                                       AuctionStrategy auctionStrategy,
                                       StopOrderPostProcessor stopOrderPostProcessor,
                                       ExpireOrderPostProcessor expireOrderPostProcessor){
        this.validator = new OpeningAuctionCallValidator();
        this.priceTimePriorityStrategy = priceTimePriorityStrategy;
        this.auctionStrategy = auctionStrategy;
        this.orderBooks = orderBooks;
        this.stopOrderPostProcessor = stopOrderPostProcessor;
        this.expireOrderPostProcessor = expireOrderPostProcessor;
        this.openingAuctionExpireRule = new OpeningAuctionExpireRule();
    }


    @Override
    public void startSession(OrderBook orderBook) {

    }

    @Override
    public void process(OrderBook orderBook,OrderEntry orderEntry) {
        priceTimePriorityStrategy.process(MatchingPreProcessor.MATCHING_ACTION.ADD_ORDER, orderBook, orderEntry);
    }

    @Override
    public void endSession(OrderBook orderBook) {
        auctionStrategy.process(priceTimePriorityStrategy, orderBook);
        stopOrderPostProcessor.postProcess(priceTimePriorityStrategy,orderBook);
        expireOrderPostProcessor.postProcess(priceTimePriorityStrategy,orderBook,openingAuctionExpireRule);
        MatchingUtil.parkGFAOrders(orderBook);
    }

    @Override
    public boolean isOrderValid(OrderEntry orderEntry, int template) {
        return validator.isMessageValidForSession(orderEntry,template);
    }
}
