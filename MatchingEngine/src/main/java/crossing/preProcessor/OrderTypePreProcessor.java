package crossing.preProcessor;

import crossing.MatchingContext;

/**
 * Created by dharmeshsing on 25/08/15.
 */
public class OrderTypePreProcessor implements MatchingPreProcessor {

    @Override
    public void preProcess(MatchingContext context) {
        MATCHING_ACTION action = MATCHING_ACTION.AGGRESS_ORDER;

        switch(context.getOrderType()){
            case MARKET: action = MarketOrderPreProcessor.preProcess(context.getSide(),
                                  context.getOrderEntry(),context.getBideTreeSize(),
                                  context.getOfferTreeSize(), context.getBestBid(),context.getBestOffer());
                                  break;

            case HIDDEN_LIMIT: action = HiddenOrderPreProcessor.preProcess(context.getOrderBook(),
                                        context.getOrderEntry().getMinExecutionSize(),
                                        context.getPrice(),context.getSide());
                                        break;

            case STOP:
            case STOP_LIMIT: action = StopOrderPreProcessor.preProcess(context.getOrderBook().getLasTradedPrice(),
                                      context.getSide(),context.getOrderEntry());
        }

        context.setAction(action);
    }
}
