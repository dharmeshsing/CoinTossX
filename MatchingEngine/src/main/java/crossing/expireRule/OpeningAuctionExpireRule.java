package crossing.expireRule;

import common.OrderType;
import common.TimeInForce;
import crossing.MatchingUtil;
import leafNode.OrderEntry;

/**
 * Created by dharmeshsing on 21/11/15.
 */
public class OpeningAuctionExpireRule implements ExpireRule {
    @Override
    public boolean isOrderExpired(OrderEntry orderEntry) {
        if(orderEntry.getType() == OrderType.MARKET.getOrderType() ||
           orderEntry.getTimeInForce() == TimeInForce.OPG.getValue() ||
           (orderEntry.getTimeInForce() == TimeInForce.GTT.getValue() && MatchingUtil.isPastExpiryDateTime(orderEntry.getExpireTime()))){
            return true;
        }

        return false;
    }
}
