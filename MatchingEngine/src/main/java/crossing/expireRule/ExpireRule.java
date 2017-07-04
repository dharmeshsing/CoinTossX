package crossing.expireRule;

import leafNode.OrderEntry;

/**
 * Created by dharmeshsing on 16/11/15.
 */
public interface ExpireRule {
    boolean isOrderExpired(OrderEntry orderEntry);
}
