package vo;

import util.CommonUtil;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TradeVO implements Serializable {
    private int tradeId;
    private long clientOrderId;
    private int price;
    private int quantity;
    private LocalDateTime creationTime;

    public TradeVO(int tradeId,long clientOrderId,int price,int quantity,LocalDateTime creationTime){
        this.tradeId = tradeId;
        this.clientOrderId = clientOrderId;
        this.price = price;
        this.quantity = quantity;
        this.creationTime = creationTime;
    }

    public TradeVO(){}


    public int getTradeId() {
        return tradeId;
    }

    public void setTradeId(int tradeId) {
        this.tradeId = tradeId;
    }

    public long getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(long clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFormattedTime(){
        return CommonUtil.creationTimeFormatter.format(creationTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TradeVO tradeVO = (TradeVO) o;

        return tradeId == tradeVO.tradeId;

    }

    @Override
    public int hashCode() {
        return tradeId;
    }

    @Override
    public String toString() {
        return tradeId + "," +
               clientOrderId + "," +
               price + "," +
               quantity + "," +
               getFormattedTime();
    }

    public Object[] getValues(){
        return new Object[]{tradeId,clientOrderId,price,quantity,getFormattedTime()};
    }

    public static String[] getFileHeader(){
        return new String[]{"OrderId","ClientOrderId","Price","Volume","DateTime"};
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
}
