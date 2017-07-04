package validation;

import common.TimeInForce;
import leafNode.OrderEntry;

/**
 * Created by dharmeshsing on 4/11/15.
 */
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
