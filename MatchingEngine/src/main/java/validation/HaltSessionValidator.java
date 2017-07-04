package validation;

import leafNode.OrderEntry;
import sbe.msg.NewOrderEncoder;
import sbe.msg.OrderCancelReplaceRequestEncoder;

/**
 * Created by dharmeshsing on 4/11/15.
 */
public class HaltSessionValidator implements SessionValidator {

    @Override
    public boolean isMessageValidForSession(OrderEntry orderEntry, int templateId) {

        //New or Amended Orders not allowed
        if(templateId == NewOrderEncoder.TEMPLATE_ID || templateId == OrderCancelReplaceRequestEncoder.TEMPLATE_ID){
            return false;
        }

        return true;
    }
}
