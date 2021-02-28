package vo;

import util.CommonUtil;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by dharmeshsing on 16/04/16.
 */
public class OrderVO implements Serializable {
    private int securityId;
    private long orderId;
    private long submittedTime;
    private long price;
    private long volume;
    private String side;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getSubmittedTime() {
        return submittedTime;
    }

    public void setSubmittedTime(long submittedTime) {
        this.submittedTime = submittedTime;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public String getFormattedTime(){
        return CommonUtil.creationTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(submittedTime), ZoneId.systemDefault()));
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getSecurityId() {
        return securityId;
    }

    public void setSecurityId(int securityId) {
        this.securityId = securityId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderVO orderVO = (OrderVO) o;

        return orderId == orderVO.orderId;

    }

    @Override
    public int hashCode() {
        return (int) (orderId ^ (orderId >>> 32));
    }

    @Override
    public String toString() {
        return securityId + "," +
                orderId + "," +
                getFormattedTime() + "," +
                price + "," +
                volume + "," +
                side;
    }

    public Object[] getValues(){
        return new Object[]{securityId,orderId,getFormattedTime(),price,volume,side};
    }

    public static String[] getFileHeader(){
        return new String[]{"SecurityId","OrderId","DateTime","Price",
                "Volume","Side"};
    }
}
