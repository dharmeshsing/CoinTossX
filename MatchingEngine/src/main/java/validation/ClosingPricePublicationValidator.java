package validation;

import common.TimeInForce;
import leafNode.OrderEntry;

/**
 * Created by dharmeshsing on 4/11/15.
 */
public class ClosingPricePublicationValidator implements SessionValidator {

    @Override
    public boolean isMessageValidForSession(OrderEntry orderEntry, int templateId) {

        //New or Amended Orders not allowed
        //if(templateId == NewOrderEncoder.TEMPLATE_ID || templateId == OrderCancelReplaceRequestEncoder.TEMPLATE_ID){
         //   return false;
        //}

        //Orders with TIFs ATC, OPG, GFA, GFX, IOC and FOK are rejected during this session
        if(orderEntry.getTimeInForce() == TimeInForce.ATC.getValue() ||
           orderEntry.getTimeInForce() == TimeInForce.OPG.getValue() ||
           orderEntry.getTimeInForce() == TimeInForce.GFA.getValue() ||
           orderEntry.getTimeInForce() == TimeInForce.GFX.getValue() ||
           orderEntry.getTimeInForce() == TimeInForce.IOC.getValue() ||
           orderEntry.getTimeInForce() == TimeInForce.FOK.getValue()){
            return false;
        }


        return true;
    }
}
