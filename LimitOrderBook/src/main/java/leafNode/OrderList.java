package leafNode;

public interface OrderList extends Iterable<OrderListCursor>  {
    int size();
    int capacity();
    long total();
    boolean isEmpty();
    void add(OrderEntry orderEntry);
    void remove(int index);
    void remove(int index,OrderEntry oe);
    void trimToSize();
    OrderEntry get(int index,OrderEntry orderEntry);
    int getTotalExecuteVolume();
    void updateTotal(int quantityToRemove);
    void free();

}
