package validation;

import leafNode.OrderEntry;

public interface SessionValidator {
    boolean isMessageValidForSession(OrderEntry orderEntry, int templateId);
}
