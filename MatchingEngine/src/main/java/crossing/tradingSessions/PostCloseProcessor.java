package crossing.tradingSessions;

import com.carrotsearch.hppc.LongObjectHashMap;
import crossing.expireRule.ClosingPricePublicationExpireRule;
import crossing.postProcessor.ExpireOrderPostProcessor;
import crossing.strategy.AuctionStrategy;
import crossing.strategy.PriceTimePriorityStrategy;
import leafNode.OrderEntry;
import orderBook.OrderBook;
import validation.ClosingPricePublicationValidator;

public class PostCloseProcessor implements TradingSessionProcessor {
    private ClosingPricePublicationValidator validator;
    private PriceTimePriorityStrategy priceTimePriorityStrategy;
    private AuctionStrategy auctionStrategy;
    private LongObjectHashMap<OrderBook> orderBooks;
    private ExpireOrderPostProcessor expireOrderPostProcessor;
    private ClosingPricePublicationExpireRule closingPricePublicationExpireRule;

    public PostCloseProcessor(LongObjectHashMap<OrderBook> orderBooks,
                              PriceTimePriorityStrategy priceTimePriorityStrategy,
                              AuctionStrategy auctionStrategy,
                              ExpireOrderPostProcessor expireOrderPostProcessor){

        this.validator = new ClosingPricePublicationValidator();
        this.priceTimePriorityStrategy = priceTimePriorityStrategy;
        this.auctionStrategy = auctionStrategy;
        this.orderBooks = orderBooks;
        this.expireOrderPostProcessor = expireOrderPostProcessor;
        this.closingPricePublicationExpireRule = new ClosingPricePublicationExpireRule();

    }


    @Override
    public void startSession(OrderBook orderBook) {

    }

    @Override
    public void process(OrderBook orderBook,OrderEntry orderEntry) {
        //No executions during this session
    }

    @Override
    public void endSession(OrderBook orderBook) {
        auctionStrategy.process(priceTimePriorityStrategy, orderBook);
        expireOrderPostProcessor.postProcess(priceTimePriorityStrategy, orderBook, closingPricePublicationExpireRule);
    }

    @Override
    public boolean isOrderValid(OrderEntry orderEntry, int template) {
        return validator.isMessageValidForSession(orderEntry,template);
    }
}
