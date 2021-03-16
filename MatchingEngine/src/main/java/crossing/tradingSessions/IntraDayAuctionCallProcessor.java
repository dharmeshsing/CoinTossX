package crossing.tradingSessions;

import com.carrotsearch.hppc.LongObjectHashMap;
import com.carrotsearch.hppc.ObjectArrayList;
import common.TimeInForce;
import crossing.MatchingContext;
import crossing.MatchingUtil;
import crossing.expireRule.IntraDayAuctionExpireRule;
import crossing.postProcessor.ExpireOrderPostProcessor;
import crossing.postProcessor.StopOrderPostProcessor;
import crossing.preProcessor.HawkesSimulationPreProcessor;
import crossing.preProcessor.MatchingPreProcessor;
import crossing.strategy.AuctionStrategy;
import crossing.strategy.PriceTimePriorityStrategy;
import data.ExecutionReportData;
import data.MarketData;
import leafNode.OrderEntry;
import orderBook.OrderBook;
import sbe.msg.ExecutionTypeEnum;
import sbe.msg.OrderStatusEnum;
import validation.IntraDayAuctionCallValidator;

import java.util.ArrayList;
import java.util.List;

public class IntraDayAuctionCallProcessor implements TradingSessionProcessor {
    private IntraDayAuctionCallValidator validator;
    private PriceTimePriorityStrategy priceTimePriorityStrategy;
    private AuctionStrategy auctionStrategy;
    private LongObjectHashMap<OrderBook> orderBooks;
    private StopOrderPostProcessor stopOrderPostProcessor;
    private ExpireOrderPostProcessor expireOrderPostProcessor;
    private IntraDayAuctionExpireRule intraDayAuctionExpireRule;
    private List<TimeInForce> timeInForceList;
    private MatchingContext matchingContext = MatchingContext.INSTANCE;
    private ExecutionReportData executionReportData = ExecutionReportData.INSTANCE;
    private OrderEntry oe = new OrderEntry();
    private ObjectArrayList<MatchingPreProcessor> preProcessors;

    public IntraDayAuctionCallProcessor(LongObjectHashMap<OrderBook> orderBooks,
                                        PriceTimePriorityStrategy priceTimePriorityStrategy,
                                        AuctionStrategy auctionStrategy,
                                        StopOrderPostProcessor stopOrderPostProcessor,
                                        ExpireOrderPostProcessor expireOrderPostProcessor){

        this.validator = new IntraDayAuctionCallValidator();
        this.priceTimePriorityStrategy = priceTimePriorityStrategy;
        this.auctionStrategy = auctionStrategy;
        this.orderBooks = orderBooks;
        this.stopOrderPostProcessor = stopOrderPostProcessor;
        this.expireOrderPostProcessor = expireOrderPostProcessor;
        this.intraDayAuctionExpireRule = new IntraDayAuctionExpireRule();

        timeInForceList = new ArrayList<>();
        timeInForceList.add(TimeInForce.GFA);
        timeInForceList.add(TimeInForce.GFX);

        initPreProcessors();
    }

    private void initPreProcessors(){
        preProcessors = new ObjectArrayList<>(1);
        preProcessors.add(new HawkesSimulationPreProcessor());
    }

    private void preProcess() {
        for (int i=0; i<preProcessors.size(); i++) {
            preProcessors.get(i).preProcess(matchingContext);

            if (matchingContext.getAction() == MatchingPreProcessor.MATCHING_ACTION.PARK_ORDER || matchingContext.getAction() == MatchingPreProcessor.MATCHING_ACTION.NO_ACTION) {
                break;
            }
        }
    }


    @Override
    public void startSession(OrderBook orderBook) {
        System.out.println("Intraday auction trading session started");
        MatchingUtil.injectOrders(orderBook, priceTimePriorityStrategy, timeInForceList);
    }

    @Override
    public void process(OrderBook orderBook,OrderEntry orderEntry) {
        orderEntry.setOrderStatus((byte) OrderStatusEnum.New.value());
        if(orderEntry.getOrderId() == 0){
            orderEntry.setOrderId(MatchingUtil.getNextOrderId());
        }


        ExecutionReportData.INSTANCE.buildOrderView(orderEntry,orderBook.getSecurityId());
        matchingContext.setOrderEntry(orderEntry);
        executionReportData.setOrderId((int) orderEntry.getOrderId());
        executionReportData.setExecutionType(ExecutionTypeEnum.New);
        MarketData.INSTANCE.setSecurityId(orderBook.getSecurityId());
        matchingContext.setOrderBook(orderBook);

        preProcess();

        if(matchingContext.getAction() != MatchingPreProcessor.MATCHING_ACTION.NO_ACTION) {
            priceTimePriorityStrategy.process(MatchingPreProcessor.MATCHING_ACTION.ADD_ORDER, orderBook, orderEntry);
        }

        MatchingUtil.publishBestBidOffer(orderBook,oe);
    }

    @Override
    public void endSession(OrderBook orderBook) {
        priceTimePriorityStrategy.init(orderBook);
        auctionStrategy.process(priceTimePriorityStrategy, orderBook);
        stopOrderPostProcessor.postProcess(priceTimePriorityStrategy,orderBook);
        expireOrderPostProcessor.postProcess(priceTimePriorityStrategy,orderBook,intraDayAuctionExpireRule);
        MatchingUtil.parkGFAOrders(orderBook);
        MatchingUtil.publishBestBidOffer(orderBook,oe);
    }

    @Override
    public boolean isOrderValid(OrderEntry orderEntry, int template) {
        return validator.isMessageValidForSession(orderEntry,template);
    }
}
