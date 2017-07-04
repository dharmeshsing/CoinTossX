package unsafe;

import com.carrotsearch.hppc.ObjectArrayList;
import leafNode.OrderEntry;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by dharmeshsing on 15/04/05.
 */
public class UnsafeUtil {
    private static final Unsafe unsafe;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Unsafe getUnsafe(){
        return unsafe;
    }

    public static void freeOrderEntryMemory(OrderEntry orderEntry){
        if(orderEntry != null) {
            orderEntry.clear();
            unsafe.freeMemory(orderEntry.getObjectOffset());
        }
    }

    public static void freeOrderEntryMemory(ObjectArrayList<OrderEntry> orderEntries){
        if(orderEntries != null && orderEntries.size() > 0) {
            int size = orderEntries.size();
            Object[] oeArr = orderEntries.buffer;

            for (int i=0; i<size; i++) {
                freeOrderEntryMemory((OrderEntry)oeArr[i]);
            }
        }
    }
}
