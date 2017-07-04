package validation;

import common.TimeInForce;
import crossing.MatchingUtil;
import leafNode.OrderEntry;
import sbe.msg.NewOrderEncoder;

/**
 * Created by dharmeshsing on 4/11/15.
 */
public class ClosingPriceCrossValidator implements SessionValidator {

    @Override
    public boolean isMessageValidForSession(OrderEntry orderEntry, int templateId) {
        //New Orders with GFA, GFX, OPG and ATC are rejected during this session
        //New Stop or Stop Limit order not allowed
        if(templateId == NewOrderEncoder.TEMPLATE_ID){

            if(orderEntry.getTimeInForce() == TimeInForce.GFA.getValue() ||
               orderEntry.getTimeInForce() == TimeInForce.GFX.getValue() ||
               orderEntry.getTimeInForce() == TimeInForce.OPG.getValue() ||
               orderEntry.getTimeInForce() == TimeInForce.ATC.getValue() ||
               orderEntry.getTimeInForce() == TimeInForce.OPG.getValue()) {
                return false;
            }

            if(MatchingUtil.isParkedOrder(orderEntry)){
                return false;
            }

        }


        return true;
    }
}
