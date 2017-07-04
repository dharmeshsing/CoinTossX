package crossing;

import com.carrotsearch.hppc.LongObjectHashMap;
import leafNode.OrderEntry;
import orderBook.OrderBook;

/**
 * Created by dharmeshsing on 27/08/15.
 */
public class OrderValidation {


    public static boolean isValid(OrderEntry orderEntry,LongObjectHashMap<OrderBook> orderBooks,int securityId){
        boolean result = isInstrumentKnown(orderBooks,securityId);
        return result;
    }

    public static boolean isInstrumentKnown(LongObjectHashMap<OrderBook> orderBooks,int securityId){
        if(orderBooks.get(securityId) == null){
            return false;
        }
        return true;
    }
}
