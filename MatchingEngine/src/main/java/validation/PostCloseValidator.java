package validation;

import leafNode.OrderEntry;
import sbe.msg.NewOrderEncoder;
import sbe.msg.OrderCancelReplaceRequestEncoder;

public class PostCloseValidator implements SessionValidator {

    @Override
    public boolean isMessageValidForSession(OrderEntry orderEntry, int templateId) {

        //New or Amended Orders not allowed
        if(templateId == NewOrderEncoder.TEMPLATE_ID || templateId == OrderCancelReplaceRequestEncoder.TEMPLATE_ID){
            return false;
        }

        return true;
    }
}
