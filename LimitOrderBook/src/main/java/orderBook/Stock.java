package orderBook;

public class Stock {
    private int stockCode;
    private int mrs;
    private int tickSize;


    public int getStockCode() {
        return stockCode;
    }

    public void setStockCode(int stockCode) {
        this.stockCode = stockCode;
    }

    public int getMRS() {
        return mrs;
    }

    public void setMRS(int mrs) {
        this.mrs = mrs;
    }


    public int getTickSize() {
        return tickSize;
    }

    public void setTickSize(int tickSize) {
        this.tickSize = tickSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stock stock = (Stock) o;

        if (mrs != stock.mrs) return false;
        if (stockCode != stock.stockCode) return false;
        if (tickSize != stock.tickSize) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stockCode;
        result = 31 * result + mrs;
        result = 31 * result + tickSize;
        return result;
    }
}
