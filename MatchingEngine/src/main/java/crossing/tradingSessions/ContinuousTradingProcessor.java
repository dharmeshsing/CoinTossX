package crossing.tradingSessions;

import com.carrotsearch.hppc.LongObjectHashMap;
import com.carrotsearch.hppc.ObjectArrayList;
import crossing.MatchingContext;
import crossing.MatchingUtil;
import crossing.expireRule.ContinousTradingExpireRule;
import crossing.postProcessor.ExpireOrderPostProcessor;
import crossing.postProcessor.MatchingPostProcessor;
import crossing.postProcessor.StopOrderPostProcessor;
import crossing.preProcessor.*;
import crossing.strategy.FilterAndUncrossStrategy;
import crossing.strategy.PriceTimePriorityStrategy;
import data.ExecutionReportData;
import data.MarketData;
import leafNode.OrderEntry;
import orderBook.OrderBook;
import sbe.msg.ExecutionTypeEnum;
import sbe.msg.OrderStatusEnum;
import validation.ContinuousTradingValidator;

public class ContinuousTradingProcessor implements TradingSessionProcessor {
    private ContinuousTradingValidator validator;
    private PriceTimePriorityStrategy priceTimePriorityStrategy;
    private LongObjectHashMap<OrderBook> orderBooks;
    private ExpireOrderPostProcessor expireOrderPostProcessor;
    private ContinousTradingExpireRule continousTradingExpireRule;

    private ObjectArrayList<MatchingPreProcessor> preProcessors;
    private ObjectArrayList<MatchingPostProcessor> postProcessors;
    private MatchingContext matchingContext = MatchingContext.INSTANCE;
    private ExecutionReportData executionReportData = ExecutionReportData.INSTANCE;
    private FilterAndUncrossStrategy filterAndUncrossStrategy;

    private OrderEntry oe = new OrderEntry();

    public ContinuousTradingProcessor(LongObjectHashMap<OrderBook> orderBooks,
                                      PriceTimePriorityStrategy priceTimePriorityStrategy,
                                      FilterAndUncrossStrategy filterAndUncrossStrategy,
                                      ExpireOrderPostProcessor expireOrderPostProcessor){

        this.validator = new ContinuousTradingValidator();
        this.orderBooks = orderBooks;
        this.priceTimePriorityStrategy = priceTimePriorityStrategy;
        this.continousTradingExpireRule = new ContinousTradingExpireRule();
        this.filterAndUncrossStrategy = filterAndUncrossStrategy;
        this.expireOrderPostProcessor = expireOrderPostProcessor;

        initPreProcessors();
        initPostProcessors();

    }

    private void initPreProcessors(){
        preProcessors = new ObjectArrayList<>(4);
        preProcessors.add(new CancelOrderPreProcessor());
        preProcessors.add(new ReplaceOrderPreProcessor());
        preProcessors.add(new OrderTypePreProcessor());
        preProcessors.add(new AddOrderPreProcessor());
        //preProcessors.add(new HawkesSimulationPreProcessor());
    }

    private void initPostProcessors(){
        postProcessors = new ObjectArrayList<>(2);
        postProcessors.add(new StopOrderPostProcessor());
        postProcessors.add(new ExpireOrderPostProcessor());
    }

    private void processOrder(OrderBook orderBook,OrderEntry orderEntry){
        priceTimePriorityStrategy.process(matchingContext.getAction(), orderBook, orderEntry);

        if(orderBook.isBestBidOfferChanged()){
            filterAndUncrossStrategy.process(priceTimePriorityStrategy,orderBook);
            orderBook.setBestBidOfferChanged(false);
        }
    }

    private void preProcess() {
        for (int i=0; i<preProcessors.size(); i++) {
            preProcessors.get(i).preProcess(matchingContext);

            if (matchingContext.getAction() == MatchingPreProcessor.MATCHING_ACTION.PARK_ORDER || matchingContext.getAction() == MatchingPreProcessor.MATCHING_ACTION.NO_ACTION) {
                break;
            }
        }
    }

    private void postProcess(OrderBook orderBook){
        for (int i=0; i<postProcessors.size(); i++) {
            postProcessors.get(i).postProcess(priceTimePriorityStrategy,orderBook);
        }
    }


    @Override
    public void startSession(OrderBook orderBook) {
        System.out.println("Continuous trading session started");
    }

    @Override
    public void process(OrderBook orderBook, OrderEntry orderEntry) {
        orderEntry.setOrderStatus((byte) OrderStatusEnum.New.value());
        if(orderEntry.getOrderId() == 0){
            orderEntry.setOrderId(MatchingUtil.getNextOrderId());
        }

        ExecutionReportData.INSTANCE.buildOrderView(orderEntry,orderBook.getSecurityId());
        matchingContext.setOrderEntry(orderEntry);
        matchingContext.setAction(MatchingPreProcessor.MATCHING_ACTION.AGGRESS_ORDER);
        executionReportData.setOrderId((int) orderEntry.getOrderId());
        executionReportData.setExecutionType(ExecutionTypeEnum.New);
        MarketData.INSTANCE.setSecurityId(orderBook.getSecurityId());

        orderBook.setLocked(true);
        matchingContext.setOrderBook(orderBook);

        preProcess();
        processOrder(orderBook, orderEntry);
        postProcess(orderBook);
        MatchingUtil.publishBestBidOffer(orderBook, oe);

        orderBook.setLocked(false);
    }

    @Override
    public void endSession(OrderBook orderBook) {
        expireOrderPostProcessor.postProcess(priceTimePriorityStrategy,orderBook,continousTradingExpireRule);
    }

    @Override
    public boolean isOrderValid(OrderEntry orderEntry, int template) {
        return validator.isMessageValidForSession(orderEntry,template);
    }

}
