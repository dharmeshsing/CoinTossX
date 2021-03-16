package leafNode;

import common.OrderType;
import sbe.msg.OrderStatusEnum;
import sun.misc.Unsafe;
import unsafe.UnsafeUtil;

/**
 * Created by dharmeshsing on 15/03/09.
 */
public class OrderEntry {
    private static final Unsafe unsafe = UnsafeUtil.getUnsafe();
    private static int offset = 0;

    private static long orderIdOffset = offset += 0;
    private static long clientOrderIdOffset = offset += 0;
    private static long origClientOrderIdOffset = offset += 0;
    private static long typeOffset = offset += Long.BYTES;
    private static long sideOffSet = offset += Byte.BYTES;
    private static long timeInForceOffSet = offset += Byte.BYTES;
    private static long orderStatusOffSet = offset += Byte.BYTES;

    private static long quantityOffSet = offset += Byte.BYTES;
    private static long minExecutionSizeOffSet = offset += Integer.BYTES;
    private static long executeVolumeOffset = offset += Integer.BYTES;
    private static long displayQuantityOffset = offset += Integer.BYTES;
    private static long traderOffset = offset += Integer.BYTES;

    private static long submittedTimeOffset = offset += Integer.BYTES;
    private static long priceOffSet = offset += Long.BYTES;
    private static long stopPriceOffset = offset += Long.BYTES;
    private static long expireTimeOffset = offset += Long.BYTES;

    private static long padding = offset += Long.BYTES;

    private static long objectSize = offset += (Byte.BYTES * 4);
    private long objectOffset;
    private OrderListImpl orderList;

    public void setObjectOffset(long objectOffset) {
        this.objectOffset = objectOffset;
    }

    public long getObjectOffset(){return objectOffset;}

    public static long getObjectSize(){
        return objectSize;
    }

    public long getOrderId() {
        return unsafe.getLong(objectOffset + orderIdOffset);
    }

    public void setOrderId(long orderId) {
        unsafe.putLong(objectOffset + orderIdOffset, orderId);
    }

    public long getClientOrderId() {
        return unsafe.getLong(objectOffset + orderIdOffset);
    }

    public void setClientOrderId(long clientOrderId) {
        unsafe.putLong(objectOffset + orderIdOffset, clientOrderId);
    }

    public long getOrigClientOrderId() {
        return unsafe.getLong(objectOffset + orderIdOffset);
    }

    public void setOrigClientOrderId(long origClientOrderId) { unsafe.putLong(objectOffset + orderIdOffset, origClientOrderId); }

    public byte getType() {
        return unsafe.getByte(objectOffset + typeOffset);
    }

    public void setType(byte type) {
        unsafe.putByte(objectOffset + typeOffset, type);
    }

    public byte getOrderStatus() {
        return unsafe.getByte(objectOffset + orderStatusOffSet);
    }

    public void setOrderStatus(byte orderStatus) {
        unsafe.putByte(objectOffset + orderStatusOffSet, orderStatus);
    }

    public long getSubmittedTime() {
        return unsafe.getLong(objectOffset + submittedTimeOffset);
    }

    public void setSubmittedTime(long submittedTime) {
        unsafe.putLong(objectOffset + submittedTimeOffset, submittedTime);
    }

    public int getQuantity() {
        return unsafe.getInt(objectOffset + quantityOffSet);
    }

    public void setQuantity(int quantity) {
        unsafe.putInt(objectOffset + quantityOffSet, quantity);
        setExecuteVolume(quantity);

        if(getType() != OrderType.HIDDEN_LIMIT.getOrderType()){
            setDisplayQuantity(quantity);
        }
    }

    public int getDisplayQuantity() {
        return unsafe.getInt(objectOffset + displayQuantityOffset);
    }

    public void setDisplayQuantity(int quantity) {
        if(getType() != OrderType.HIDDEN_LIMIT.getOrderType()) {
            unsafe.putInt(objectOffset + displayQuantityOffset, quantity);
        }else{
            unsafe.putInt(objectOffset + displayQuantityOffset, 0);
        }
    }

    public int getTrader() {
        return unsafe.getInt(objectOffset + traderOffset);
    }

    public void setTrader(int trader) {
        unsafe.putInt(objectOffset + traderOffset, trader);
    }

    public long getExpireTime() {
        return unsafe.getLong(objectOffset + expireTimeOffset);
    }

    public void setExpireTime(long expireTime) {
        unsafe.putLong(objectOffset + expireTimeOffset, expireTime);
    }

    public void removeQuantity(int quantity){
        if(quantity <= getQuantity()) {
            setOrderStatus((byte)OrderStatusEnum.PartiallyFilled.value());
            setQuantity(getQuantity() - quantity);
            if (orderList != null) {
                orderList.updateTotal(quantity);
            }
        }
    }

    public void setMinExecutionSize(int minExecutionSize) {
        unsafe.putInt(objectOffset + minExecutionSizeOffSet, minExecutionSize);
    }

    public int getMinExecutionSize() {
        return unsafe.getInt(objectOffset + minExecutionSizeOffSet);
    }

    public long getPrice() {
        return unsafe.getLong(objectOffset + priceOffSet);
    }

    public void setStopPrice(long stopPrice) {
        unsafe.putLong(objectOffset + stopPriceOffset, stopPrice);
    }

    public long getStopPrice() {
        return unsafe.getLong(objectOffset + stopPriceOffset);
    }

    public void setPrice(long price) {
        unsafe.putLong(objectOffset + priceOffSet, price);
    }

    public byte getSide() {
        return unsafe.getByte(objectOffset + sideOffSet);
    }

    public void setSide(byte side) {
        unsafe.putByte(objectOffset + sideOffSet, side);
    }

    public boolean isEmpty(){
        return getOrderId() == 0 ? true:false;
    }

    public int getExecuteVolume() {
        return unsafe.getInt(objectOffset + executeVolumeOffset);
    }

    public void setExecuteVolume(int executeVolume) {
        unsafe.putInt(objectOffset + executeVolumeOffset, executeVolume);
    }

    public byte getTimeInForce() {
        return unsafe.getByte(objectOffset + timeInForceOffSet);
    }

    public void setTimeInForce(byte timeInForce) {
        unsafe.putByte(objectOffset + timeInForceOffSet, timeInForce);
    }

    public void clear(){
        if(objectOffset != 0) {
            setOrderId(0);
            setClientOrderId(0);
            setOrigClientOrderId(0);
            setPrice(0);
            setStopPrice(0);
            setQuantity(0);
            setMinExecutionSize(0);
            setSubmittedTime(0);
            setType((byte) 0);
            setSide((byte) 0);
            setTimeInForce((byte) 0);
            setDisplayQuantity(0);
            setTrader(0);
            setExpireTime(0);
            setOrderList(null);
        }
    }

    public void set(OrderEntry oe){
        setOrderId(oe.getOrderId());
        setClientOrderId(oe.getClientOrderId());
        setOrigClientOrderId(oe.getOrigClientOrderId());
        setPrice(oe.getPrice());
        setStopPrice(oe.getStopPrice());
        setQuantity(oe.getQuantity());
        setMinExecutionSize(oe.getMinExecutionSize());
        setSubmittedTime(oe.getSubmittedTime());
        setType(oe.getType());
        setSide(oe.getSide());
        setTimeInForce(oe.getTimeInForce());
        setDisplayQuantity(oe.getDisplayQuantity());
        setTrader(oe.getTrader());
        setExpireTime(oe.getExpireTime());
        setOrderStatus(oe.getOrderStatus());
    }

    public void init(){
        clear();
    }

    @Override
    public String toString() {
        if(objectOffset != 0) {
            return "OrderEntry{" +
                    "OrderId=" + getOrderId() + "," +
                    "ClientOrderId=" + getClientOrderId() + "," +
                    "OrigClientOrderId=" + getOrigClientOrderId() + "," +
                    "Price=" + getPrice() + "," +
                    "StopPrice=" + getStopPrice() + "," +
                    "Quantity=" + getQuantity() + "," +
                    "MES=" + getMinExecutionSize() + "," +
                    "SubmittedTime=" + getSubmittedTime() + "," +
                    "Type=" + getType() + "," +
                    "Side=" + getSide() + "," +
                    "isEmpty=" + isEmpty() + "," +
                    "ExecuteVolume=" + getExecuteVolume() + "," +
                    "TimeInForce=" + getTimeInForce() + "," +
                    "DisplayQuantity=" + getDisplayQuantity() + "," +
                    "Trader=" + getTrader() + "," +
                    "ExpireTime=" + getExpireTime() + "," +
                    "OrderStatus=" + getOrderStatus() + "," +
                    '}';
        }

        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderEntry that = (OrderEntry) o;

        if (getOrderId() != that.getOrderId()) return false;
        if (getClientOrderId() != that.getClientOrderId()) return false;
        if (getOrigClientOrderId() != that.getOrigClientOrderId()) return false;
        if (Long.compare(that.getPrice(), getPrice()) != 0) return false;
        if (getQuantity() != that.getQuantity()) return false;
        if (getMinExecutionSize() != that.getMinExecutionSize()) return false;
        if (getSubmittedTime() != that.getSubmittedTime()) return false;
        if (getType() != that.getType()) return false;
        if (getSide() != that.getSide()) return false;
        if (isEmpty() != that.isEmpty()) return false;
        if (getExecuteVolume() != that.getExecuteVolume()) return false;
        if (getStopPrice() != that.getStopPrice()) return false;
        if (getTimeInForce() != that.getTimeInForce()) return false;
        if (getDisplayQuantity() != that.getDisplayQuantity()) return false;
        if (getTrader() != that.getTrader()) return false;
        if (getExpireTime() != that.getExpireTime()) return false;
        //if (getOrderStatus() != that.getOrderStatus()) return false;

        return true;
    }


    OrderListImpl getOrderList() {
        return orderList;
    }

    void setOrderList(OrderListImpl orderList) {
        this.orderList = orderList;
    }
}