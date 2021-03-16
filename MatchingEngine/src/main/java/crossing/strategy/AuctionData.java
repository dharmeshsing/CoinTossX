package crossing.strategy;

public class AuctionData implements Comparable<AuctionData> {
    private long price;
    private long aggregateBuy;
    private long aggregateSell;

    public AuctionData(long price){
        this.price = price;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getAggregateBuy() {
        return aggregateBuy;
    }

    public void setAggregateBuy(long aggregateBuy) {
        this.aggregateBuy = aggregateBuy;
    }

    public long getAggregateSell() {
        return aggregateSell;
    }

    public void setAggregateSell(long aggregateSell) {
        this.aggregateSell = aggregateSell;
    }

    public long getVolume() {
        return Math.min(aggregateBuy,aggregateSell);
    }

    public long getSurplus() {
        return aggregateBuy - aggregateSell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuctionData data = (AuctionData) o;

        if (aggregateBuy != data.aggregateBuy) return false;
        if (aggregateSell != data.aggregateSell) return false;
        if (price != data.price) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (price ^ (price >>> 32));
        result = 31 * result + (int) (aggregateBuy ^ (aggregateBuy >>> 32));
        result = 31 * result + (int) (aggregateSell ^ (aggregateSell >>> 32));
        return result;
    }


    @Override
    public int compareTo(AuctionData ad) {
        long result = ad.getVolume() - this.getVolume();
        if(result == 0){
            result = this.getSurplus() - ad.getSurplus();
        }

        return (int)result;
    }
}
