package crossing.strategy;

import com.carrotsearch.hppc.ObjectArrayList;
import crossing.OrderData;
import crossing.OrderLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by dharmeshsing on 7/07/15.
 */
public class PriceTimePriorityStrategyTest {

    PriceTimePriorityStrategy priceTimePriorityStrategy;

    @Before
    public void setup(){
        priceTimePriorityStrategy = new PriceTimePriorityStrategy();
    }

    @Test
    public void testAddOrder() throws Exception {

    }

    public static Object[] provideFilterData() {
        OrderLoader orderLoader = new OrderLoader();
        try {
            ObjectArrayList<OrderData> orderDataList = orderLoader.getFilterAndUncrossOrders();
            //TODO:Remove this. Only used for testing
            orderDataList.trimToSize();
            Object[] arr = orderDataList.buffer;
            org.apache.commons.lang3.ArrayUtils.reverse(arr);
            return arr;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}