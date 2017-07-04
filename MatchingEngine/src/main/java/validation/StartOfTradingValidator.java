package validation;

import leafNode.OrderEntry;

/**
 * Created by dharmeshsing on 4/11/15.
 */
public class StartOfTradingValidator implements SessionValidator {

    @Override
    /**
     * Traders will not be able to submit, cancel or amend orders during this session.
     */
    public boolean isMessageValidForSession(OrderEntry orderEntry, int templateId) {
        return false;
    }
}
