package validation;

import common.OrderType;
import common.TimeInForce;
import leafNode.OrderEntry;
import sbe.msg.NewOrderEncoder;

public class PauseSessionValidator implements SessionValidator {

    @Override
    public boolean isMessageValidForSession(OrderEntry orderEntry, int templateId) {

        //New Hidden Orders not allowed
        if(templateId == NewOrderEncoder.TEMPLATE_ID &&
                orderEntry.getType() == OrderType.HIDDEN_LIMIT.getOrderType()){
            return false;
        }

        //Limit orders and Market orders with IOC or FOK time qualifiers not allowed
        if((orderEntry.getType() == OrderType.LIMIT.getOrderType() || orderEntry.getType() == OrderType.MARKET.getOrderType()) &&
                orderEntry.getTimeInForce() == TimeInForce.IOC.getValue() || orderEntry.getTimeInForce() == TimeInForce.FOK.getValue()){
            return false;
        }

        //Orders with OPG time qualifier are rejected during this session
        if(orderEntry.getTimeInForce() == TimeInForce.OPG.getValue()){
            return false;
        }



        return true;
    }
}
