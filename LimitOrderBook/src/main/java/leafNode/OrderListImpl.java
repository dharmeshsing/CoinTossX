package leafNode;

import common.OrderType;
import sun.misc.Unsafe;
import unsafe.UnsafeUtil;

import java.util.Iterator;

/**
 * Created by dharmeshsing on 15/03/09.
 */
public class OrderListImpl implements OrderList{
    private static final Unsafe unsafe = UnsafeUtil.getUnsafe();
    private static int DEFAULT_CAPACITY = 1000;
    private int length;
    private int capacity;
    private long address;
    private long total;
    private OrderListIterator orderListIterator;

    private OrderEntry orderEntry;
    private static final long OBJECT_SIZE = OrderEntry.getObjectSize();

    public OrderListImpl(){
        this(DEFAULT_CAPACITY);
    }

    public OrderListImpl(int initialCapacity){
        this.capacity =  findNextPositivePowerOfTwo(initialCapacity);
        orderEntry = new OrderEntry();
        address = unsafe.allocateMemory(OBJECT_SIZE * this.capacity);
        initArray();
        orderListIterator = new OrderListIterator();
    }

    public int size() {
        return length;
    }

    public int capacity(){
        return capacity;
    }

    public long total(){
        return total;
    }

    public boolean isEmpty() {
        return total == 0;
    }

    public void add(OrderEntry orderEntry) {
        if(length == capacity){
            increaseCapacity();
        }

        int index = binarySearch(0,length,orderEntry);
        index = -(++index);

        if (index < length) {
            shiftElementsRight(index);
        }
        set(index, orderEntry);
        length++;
        total += orderEntry.getQuantity();
    }

    private void shiftElementsRight(int index){
        long start = address + (index * OBJECT_SIZE);
        long end = address + ((index + 1) * OBJECT_SIZE);
        long len = (length - index) * OBJECT_SIZE;
        unsafe.copyMemory(start, end, len);
    }

    private void shiftElementsLeft(int index){
        long start = address + ((index + 1) * OBJECT_SIZE);
        long end = address + (index * OBJECT_SIZE);
        long len = (length - index) * OBJECT_SIZE;
        unsafe.copyMemory(start, end, len);
    }

    public void remove(int index){
        OrderEntry oe = get(index);
        remove(index,oe);
    }

    public void remove(int index,OrderEntry oe){
        total -= oe.getQuantity();

        oe.clear();
        if(index < length - 1){
            shiftElementsLeft(index);
        }

        length--;
    }

    private int findNextPositivePowerOfTwo(int value) {
        return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
    }

    private void initArray(){
        for(int i=0; i<capacity; i++) {
            get(i).init();
        }
    }

    private int timePriorityCompare(OrderEntry oe1, OrderEntry oe2) {
        if(oe1.isEmpty()){
            return 0;
        }

        if(oe1.getType() != OrderType.HIDDEN_LIMIT.getOrderType() &&
                oe2.getType() != OrderType.HIDDEN_LIMIT.getOrderType()){
            return Long.compare(oe1.getSubmittedTime(),oe2.getSubmittedTime());
        }

        if(oe2.getType() == OrderType.HIDDEN_LIMIT.getOrderType()){
            return -1;
        }else{
            return 1;
        }

    }

    private int binarySearch(final int startIndex, final int endIndex, OrderEntry search) {
        int compare;
        int lo = startIndex;
        int hi = endIndex - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            compare = timePriorityCompare(get(mid), search);
            if (compare < 0) {
                lo = mid + 1;
            } else if (compare > 0) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }
        return ~lo;
    }

    private void increaseCapacity() {
        int len = findNextPositivePowerOfTwo(length * 2);
        long newAddress = unsafe.allocateMemory(OBJECT_SIZE * len);
        unsafe.copyMemory(address, newAddress, capacity * OBJECT_SIZE);
        unsafe.freeMemory(address);
        address = newAddress;
        capacity = len;

        for (int i = length; i < capacity; i++) {
            get(i).init();
        }
    }

    public void trimToSize(){
        long bytesToCopy = OBJECT_SIZE * length;
        long newAddress = unsafe.allocateMemory(bytesToCopy);
        unsafe.copyMemory(address, newAddress, bytesToCopy);
        unsafe.freeMemory(address);
        address = newAddress;
        capacity = length;
    }

    public OrderEntry get(int index,OrderEntry orderEntry){
        long offset = address + (index * OBJECT_SIZE);
        orderEntry.setObjectOffset(offset);
        orderEntry.setOrderList(this);
        return orderEntry;
    }

    private OrderEntry get(int index){
        long offset = address + (index * OBJECT_SIZE);
        orderEntry.setObjectOffset(offset);
        orderEntry.setOrderList(this);
        return orderEntry;
    }

    private void set(int index, OrderEntry oe) {
        get(index).set(oe);
    }

    public int getTotalExecuteVolume(){
        int total = 0;

        for(int i=0; i<capacity; i++) {
            total += get(i).getExecuteVolume();
        }

        return total;
    }

    public void updateTotal(int quantityToRemove){
        total -=quantityToRemove;
    }

    @Override
    public String toString() {
        String result = "";
        for(int i=0; i<length; i++) {
            result +=get(i).toString();
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderListImpl orderList = (OrderListImpl) o;

        if (size() != orderList.size()) return false;

        for(int i=0; i<size(); i++){
            if (!get(i).equals(orderList.get(i))) {
                return false;
            }
        }

        return true;
    }

    public void free() {
        for(int i=0; i<length; i++) {
            get(i).clear();
        }

        unsafe.freeMemory(address);
    }

    @Override
    public Iterator<OrderListCursor> iterator() {
        orderListIterator.reset();
        return orderListIterator;
    }

    class OrderListIterator implements Iterator<OrderListCursor>{

        private OrderListCursor cursor;

        public OrderListIterator(){
            cursor = new OrderListCursor();
        }

        @Override
        public boolean hasNext() {
            return cursor.index + 1 < length;
        }

        @Override
        public OrderListCursor next() {
            get(++cursor.index, cursor.value);
            return cursor;
        }

        @Override
        public void remove(){
            OrderListImpl.this.remove(cursor.index,cursor.value);
            cursor.index--;
        }

        public void reset(){
            cursor.index = -1;
        }
    }
}