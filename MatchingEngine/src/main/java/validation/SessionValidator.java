package validation;

import leafNode.OrderEntry;

/**
 * Created by dharmeshsing on 4/11/15.
 */
public interface SessionValidator {
    boolean isMessageValidForSession(OrderEntry orderEntry, int templateId);
}
