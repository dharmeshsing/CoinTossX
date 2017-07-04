package orderBook;

/**
 * Created by dharmeshsing on 15/04/16.
 */
public class Trade {

    private long id;
    private long price;
    private long quantity;


    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trade trade = (Trade) o;

        if (id != trade.id) return false;
        if (price != trade.price) return false;
        if (quantity != trade.quantity) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (price ^ (price >>> 32));
        result = 31 * result + (int) (quantity ^ (quantity >>> 32));
        return result;
    }


}
