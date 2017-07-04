package crossing;

import uk.co.real_logic.agrona.DirectBuffer;

/**
 * Created by dharmeshsing on 15/02/15.
 */
public interface LOBManager {

    DirectBuffer processOrder(DirectBuffer message);
    boolean isClientMarketDataRequest();
}
