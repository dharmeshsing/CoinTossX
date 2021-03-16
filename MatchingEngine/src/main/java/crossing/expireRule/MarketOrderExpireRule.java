package crossing.expireRule;

import common.OrderType;
import leafNode.OrderEntry;

public class MarketOrderExpireRule implements ExpireRule {

    @Override
    public boolean isOrderExpired(OrderEntry orderEntry) {
        return orderEntry.getType() == OrderType.MARKET.getOrderType();
    }
}
