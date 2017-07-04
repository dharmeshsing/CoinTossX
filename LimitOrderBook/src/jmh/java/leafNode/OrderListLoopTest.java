package leafNode;

import org.openjdk.jmh.annotations.*;

import java.util.Iterator;
import java.util.Random;

/**
 * Created by dharmeshsing on 6/06/15.
 */
@State(Scope.Thread)
public class OrderListLoopTest {
    private OrderList orderList;
    private OrderEntry orderEntry;
    private Random random = new Random();
    private static final int COUNT = 10;

    @Setup(Level.Trial)
    public void setup(){
        orderList = new OrderListImpl(COUNT);
        orderEntry = new OrderEntry();
        for(int i=0; i<COUNT; i++) {
            orderList.add(OrderEntryFactory.getOrderEntry());
        }
    }

    @TearDown
    public void tearDown(){
       orderList.free();
    }

    @Benchmark
    public int testForLoop() throws Exception {
        int total =0;
        for(int i=0; i<orderList.size(); i++){
            orderList.get(i,orderEntry);
            total += orderEntry.getQuantity();
        }
        return total;
    }

    @Benchmark
    public int testForLoopExtraSizeVariable() throws Exception {
        int total =0;
        int size = orderList.size();
        for(int i=0; i<size; i++){
            orderList.get(i,orderEntry);
            total += orderEntry.getQuantity();
        }
        return total;
    }

    @Benchmark
    public int testIterator() throws Exception {
        int total =0;
        for(Iterator<OrderListCursor> it = orderList.iterator(); it.hasNext();){
            total += it.next().value.getQuantity();
        }
        return total;
    }
}
