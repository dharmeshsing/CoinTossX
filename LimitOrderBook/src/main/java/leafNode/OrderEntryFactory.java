package leafNode;

import unsafe.UnsafeUtil;

public class OrderEntryFactory {

    public static OrderEntry getOrderEntry(){
        long address = UnsafeUtil.getUnsafe().allocateMemory(OrderEntry.getObjectSize());
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setObjectOffset(address);
        orderEntry.init();

        return orderEntry;
    }
}
