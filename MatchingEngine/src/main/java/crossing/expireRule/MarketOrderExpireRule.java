package crossing.expireRule;

import common.OrderType;
import leafNode.OrderEntry;

/**
 * Created by dharmeshsing on 21/11/15.
 */
public class MarketOrderExpireRule implements ExpireRule {

    @Override
    public boolean isOrderExpired(OrderEntry orderEntry) {
        return orderEntry.getType() == OrderType.MARKET.getOrderType();
    }
}
