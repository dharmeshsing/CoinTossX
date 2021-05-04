package crossing.preProcessor;

import common.TimeInForce;
import crossing.MatchingContext;
import leafNode.OrderEntry;
import orderBook.OrderBook;

public class TimeInForcePreProcessor implements MatchingPreProcessor {
    @Override
    public void preProcess(MatchingContext context) {
        TimeInForce tif = TimeInForce.getTimeInForce(context.getOrderEntry().getTimeInForce());
        switch(tif){
            case GFA:
        }
    }

    private void parkGFAOrder(OrderEntry orderEntry, OrderBook orderBook){

    }
}
