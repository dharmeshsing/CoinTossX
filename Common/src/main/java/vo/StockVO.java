package vo;

import java.io.Serializable;

/**
 * Created by dharmeshsing on 6/05/16.
 */
public class StockVO implements Serializable {
    private int securityId;
    private String stockCode;
    private String name;
    private String statusText;


    public int getSecurityId() {
        return securityId;
    }

    public void setSecurityId(int securityId) {
        this.securityId = securityId;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockVO stockVO = (StockVO) o;

        if (securityId != stockVO.securityId) return false;
        if (stockCode != null ? !stockCode.equals(stockVO.stockCode) : stockVO.stockCode != null) return false;
        return !(name != null ? !name.equals(stockVO.name) : stockVO.name != null);

    }

    @Override
    public int hashCode() {
        int result = securityId;
        result = 31 * result + (stockCode != null ? stockCode.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    @Override
    public String toString() {
        return "StockVO{" +
                "securityId=" + securityId +
                ", stockCode='" + stockCode + '\'' +
                ", name='" + name + '\'' +
                ", statusText='" + statusText + '\'' +
                '}';
    }
}
