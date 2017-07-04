package leafNode;

import org.openjdk.jmh.annotations.*;

import java.util.Random;

/**
 * Created by dharmeshsing on 6/06/15.
 */
@State(Scope.Thread)
public class OrderListPerfTest {
    private OrderList orderList = new OrderListImpl();
    private OrderEntry orderEntry;
    private Random random = new Random();

    @Setup(Level.Invocation)
    public void setup(){
        orderEntry = createOrderEntry();
    }

    @Benchmark
    public OrderList testPutEntry1() throws Exception {
        orderList.add(orderEntry);
        return orderList;
    }

    private OrderEntry createOrderEntry(){
        //TODO:use object pool
        OrderEntry orderEntry = OrderEntryFactory.getOrderEntry();

        byte side = (byte)(random.nextInt(2) + 1);
        orderEntry.setSide(side);
        orderEntry.setOrderId(1);
        orderEntry.setType((byte)2);

        orderEntry.setSubmittedTime(System.nanoTime());

        orderEntry.setQuantity(1000);
        orderEntry.setMinExecutionSize(0);
        orderEntry.setPrice(100);
        orderEntry.setExecuteVolume(0);

        return orderEntry;
    }
}
