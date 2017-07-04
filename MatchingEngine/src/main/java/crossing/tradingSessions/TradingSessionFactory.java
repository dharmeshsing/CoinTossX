package crossing.tradingSessions;

import com.carrotsearch.hppc.LongObjectHashMap;
import crossing.postProcessor.ExpireOrderPostProcessor;
import crossing.postProcessor.StopOrderPostProcessor;
import crossing.strategy.AuctionStrategy;
import crossing.strategy.FilterAndUncrossStrategy;
import crossing.strategy.PriceTimePriorityStrategy;
import orderBook.OrderBook;
import sbe.msg.marketData.TradingSessionEnum;

/**
 * Created by dharmeshsing on 28/11/15.
 */
public class TradingSessionFactory {

    private static PriceTimePriorityStrategy priceTimePriorityStrategy  = new PriceTimePriorityStrategy();
    private static FilterAndUncrossStrategy filterAndUncrossStrategy  = new FilterAndUncrossStrategy();
    private static AuctionStrategy auctionStrategy = new AuctionStrategy();

    private static StartOfTradingProcessor startOfTradingProcessor;
    private static OpeningAuctionCallProcessor openingAuctionCallProcessor;
    private static ContinuousTradingProcessor continuousTradingProcessor;
    private static FutureClosingAuctionCallProcessor futureClosingAuctionCallProcessor;
    private static VolatilityAuctionCallProcessor volatilityAuctionCallProcessor;
    private static IntraDayAuctionCallProcessor intraDayAuctionCallProcessor;
    private static ClosingPriceCrossProcessor closingPriceCrossProcessor;
    private static ClosingAuctionCallProcessor closingAuctionCallProcessor;
    private static ClosingPricePublicationProcessor closingPricePublicationProcessor;
    private static PostCloseProcessor postCloseProcessor;

    private static StopOrderPostProcessor stopOrderPostProcessor  = new StopOrderPostProcessor();
    private static ExpireOrderPostProcessor expireOrderPostProcessor  = new ExpireOrderPostProcessor();


    public static void initTradingSessionProcessors(LongObjectHashMap<OrderBook> orderBooks){
        startOfTradingProcessor = new StartOfTradingProcessor();
        openingAuctionCallProcessor = new OpeningAuctionCallProcessor(orderBooks,priceTimePriorityStrategy,auctionStrategy,stopOrderPostProcessor,expireOrderPostProcessor);
        continuousTradingProcessor = new ContinuousTradingProcessor(orderBooks,priceTimePriorityStrategy,filterAndUncrossStrategy,expireOrderPostProcessor);
        futureClosingAuctionCallProcessor = new FutureClosingAuctionCallProcessor(orderBooks,priceTimePriorityStrategy,auctionStrategy,stopOrderPostProcessor,expireOrderPostProcessor);
        volatilityAuctionCallProcessor = new VolatilityAuctionCallProcessor(orderBooks,priceTimePriorityStrategy,auctionStrategy,stopOrderPostProcessor,expireOrderPostProcessor);
        intraDayAuctionCallProcessor = new IntraDayAuctionCallProcessor(orderBooks,priceTimePriorityStrategy,auctionStrategy,stopOrderPostProcessor,expireOrderPostProcessor);
        closingPriceCrossProcessor = new ClosingPriceCrossProcessor(orderBooks,priceTimePriorityStrategy,auctionStrategy,expireOrderPostProcessor);
        closingAuctionCallProcessor = new ClosingAuctionCallProcessor(orderBooks,priceTimePriorityStrategy,auctionStrategy,stopOrderPostProcessor,expireOrderPostProcessor);
        closingPricePublicationProcessor = new ClosingPricePublicationProcessor(orderBooks,priceTimePriorityStrategy,auctionStrategy,expireOrderPostProcessor);
        postCloseProcessor = new PostCloseProcessor(orderBooks,priceTimePriorityStrategy,auctionStrategy,expireOrderPostProcessor);

    }

    public static TradingSessionProcessor getTradingSessionProcessor(TradingSessionEnum tradingSessionEnum){
        switch(tradingSessionEnum){
            case StartOfTrading: return startOfTradingProcessor;
            case OpeningAuctionCall: return openingAuctionCallProcessor;
            case ContinuousTrading: return continuousTradingProcessor;
            case FCOAuctionCall: return futureClosingAuctionCallProcessor;
            case VolatilityAuctionCall: return volatilityAuctionCallProcessor;
            case IntraDayAuctionCall: return intraDayAuctionCallProcessor;
            case ClosingAuctionCall: return closingPriceCrossProcessor;
            case ClosingPricePublication: return closingAuctionCallProcessor;
            case ClosingPriceCross: return closingPricePublicationProcessor;
            case PostClose: return postCloseProcessor;
            default: return startOfTradingProcessor;
        }

    }

    public static void reset(){
        priceTimePriorityStrategy  = new PriceTimePriorityStrategy();
        filterAndUncrossStrategy  = new FilterAndUncrossStrategy();
        auctionStrategy = new AuctionStrategy();
        stopOrderPostProcessor  = new StopOrderPostProcessor();
        expireOrderPostProcessor  = new ExpireOrderPostProcessor();
    }
}
