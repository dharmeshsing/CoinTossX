package bplusTree;

import leafNode.OrderEntry;
import leafNode.OrderList;
import leafNode.OrderListImpl;
import leafNode.TestOrderEntryFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmeshsing on 24/07/15.
 */
public class BPlusTreeTest {

    @Test
    public void testIterator(){
        BPlusTree<Integer,String> tree = new BPlusTree(10);
        tree.put(1,"One");
        tree.put(2,"Two");
        tree.put(3,"Three");
        tree.put(4,"Four");
        tree.put(5, "Five");

        BPlusTree.BPlusTreeIterator iterator = (BPlusTree.BPlusTreeIterator)tree.iterator();

        while(iterator.hasNext()){
            Map.Entry<Integer, String> entry = iterator.next();
            System.out.println(entry.getKey());
            if(entry.getKey() == 3){
                tree.remove(3);
                iterator.remove();
            }
        }
    }

    @Test
    public void testAddMillionOrders() throws Exception{
        BPlusTree<Long, OrderList> tree = new BPlusTree(10);

        OrderEntry orderEntry = TestOrderEntryFactory.getOrderEntryInstance();

        for(int i=0; i<1_000_000; i++){
            OrderList orderList = tree.get(0L);
            if(orderList == null){
                orderList = new OrderListImpl();
                tree.put(0L,orderList);
            }
            TestOrderEntryFactory.createOrderEntry(orderEntry, System.nanoTime());
            orderList.add(orderEntry);
        }

        assertEquals(1000000000, tree.get(0L).total());

    }

    @Test
    public void testAddMillionItems() throws Exception{
        BPlusTree<Long, List<Long>> tree = new BPlusTree(10);

        for(int i=0; i<1_000_000; i++){
            List<Long> values = tree.get(0L);
            if(values == null){
                values = new ArrayList<>();
                tree.put(0L,values);
            }
            values.add(new Long(i));
        }
    }

}