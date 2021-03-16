package validation;

import common.TimeInForce;
import leafNode.OrderEntry;

public class ContinuousTradingValidator implements SessionValidator {

    @Override
    public boolean isMessageValidForSession(OrderEntry orderEntry, int templateId) {

        //Orders with OPG time qualifier are rejected during this session
        if(orderEntry.getTimeInForce() == TimeInForce.OPG.getValue()){
            return false;
        }

        return true;
    }
}
