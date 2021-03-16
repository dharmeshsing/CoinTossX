package crossing;

import uk.co.real_logic.agrona.DirectBuffer;

public interface LOBManager {

    DirectBuffer processOrder(DirectBuffer message);
    boolean isClientMarketDataRequest();
}
