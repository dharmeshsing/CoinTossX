package crossing;

import com.carrotsearch.hppc.ObjectArrayList;
import orderBook.Stock;
import leafNode.OrderEntry;
import orderBook.Trade;

/**
 * Created by dharmeshsing on 15/03/17.
 */
public class OrderData {
    private int testNumber;
    private String description;
    private String type;
    private ObjectArrayList<OrderEntry> initState;
    private ObjectArrayList<OrderEntry> aggOrder;
    private ObjectArrayList<OrderEntry> expState;
    private Stock stock;
    private ObjectArrayList<Trade> trades;


    public int getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(int testNumber) {
        this.testNumber = testNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ObjectArrayList<OrderEntry> getInitState() {
        return initState;
    }

    public void setInitState(ObjectArrayList<OrderEntry> initState) {
        this.initState = initState;
    }

    public ObjectArrayList<OrderEntry> getAggOrder() {
        return aggOrder;
    }

    public void setAggOrder(ObjectArrayList<OrderEntry> aggOrder) {
        this.aggOrder = aggOrder;
    }

    public ObjectArrayList<OrderEntry> getExpState() {
        return expState;
    }

    public void setExpState(ObjectArrayList<OrderEntry> expState) {
        this.expState = expState;
    }


    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }


    public ObjectArrayList<Trade> getTrades() {
        return trades;
    }

    public void setTrades(ObjectArrayList<Trade> trades) {
        this.trades = trades;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
