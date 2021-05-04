package crossing.tradingSessions;

import leafNode.OrderEntry;
import orderBook.OrderBook;
import validation.StartOfTradingValidator;

public class StartOfTradingProcessor implements TradingSessionProcessor {
    private StartOfTradingValidator validator;

    public StartOfTradingProcessor(){
        validator = new StartOfTradingValidator();
    }

    @Override
    public void startSession(OrderBook orderBook) {
        //Nothing to do
    }

    @Override
    public void process(OrderBook orderBook,OrderEntry orderEntry) {
        //Orders can only be viewed. No executions will take place
    }

    @Override
    public void endSession(OrderBook orderBook) {
        //Nothing to do
    }

    @Override
    public boolean isOrderValid(OrderEntry orderEntry, int template) {
        return validator.isMessageValidForSession(orderEntry,template);
    }
}
