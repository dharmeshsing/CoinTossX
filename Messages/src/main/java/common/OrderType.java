package common;

/**
 * Created by dharmeshsing on 15/04/04.
 */
public enum OrderType {
    MARKET((byte)1),
    LIMIT((byte)2),
    STOP((byte)3),
    STOP_LIMIT((byte)4),
    HIDDEN_LIMIT((byte)5);

    private byte orderType;

    OrderType(byte orderType){
        this.orderType = orderType;
    }

    public byte getOrderType(){
        return this.orderType;
    }

    public static OrderType getOrderType(byte value){
        switch (value) {
            case 1: return MARKET;
            case 2: return LIMIT;
            case 3: return STOP;
            case 4: return STOP_LIMIT;
            case 5: return HIDDEN_LIMIT;
        }

        return null;
    }
}
