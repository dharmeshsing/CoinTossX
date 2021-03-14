package crossing;

import com.carrotsearch.hppc.ObjectArrayList;
import common.TimeInForce;
import leafNode.OrderEntry;
import leafNode.OrderEntryFactory;
import orderBook.Stock;
import orderBook.Trade;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import sbe.msg.OrderStatusEnum;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dharmeshsing on 15/03/17.
 */
public class OrderLoader {

    public static final String [] MASTER_HEADER_MAPPING = {"TestNumber","Description","InitialState","AggressingOrder","ExpectedState","Stock","Trades"};
    public static final String [] ORDER_HEADER_MAPPING = {"Side","OrderID","Type","MES","Time","Size","Price","StopPrice","TimeInForce","ExpireTime"};
    public static final String [] STOCK_HEADER_MAPPING = {"StockCode","MRS","TickSize"};
    public static final String [] TRADE_HEADER_MAPPING = {"TradeId","Price","Quantity"};
    private String DATA_PATH = Paths.get("").toAbsolutePath() + "/src/test/resources/crossing/";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public enum OrderDataType{
        MARKET_ORDER("Market Order"),
        LIMIT_ORDER("Limit Order"),
        STOP_ORDER("Stop Order"),
        STOP_LIMIT_ORDER("Stop Limit Order"),
        HIDDEN_ORDER("Hidden Order"),
        TIME_IN_FORCE("Time In Force"),
        AUCTION_ORDER("Auction Order"),
        FILTER_AND_UNCROSS_ORDER("Filter And Uncross Order"),
        CANCEL_ORDER("Cancel Order"),
        REPLACE_ORDER("Replace Order");

        private String value;
        private OrderDataType(String value){
            this.value = value;
        }
        public String getValue(){
            return this.value;
        }
    }

    private Date currentDate = Calendar.getInstance().getTime();

    public ObjectArrayList<OrderData> getMarketOrders() throws IOException, ParseException {
        return readMaster(DATA_PATH + "uncross/marketOrder","MarketOrderMaster.csv",OrderDataType.MARKET_ORDER.getValue());
    }

    public ObjectArrayList<OrderData> getLimitOrders() throws IOException, ParseException {
        return readMaster(DATA_PATH + "uncross/limitOrder","LimitOrderMaster.csv",OrderDataType.LIMIT_ORDER.getValue());
    }

    public ObjectArrayList<OrderData> getStopOrders() throws IOException, ParseException {
        return readMaster(DATA_PATH + "uncross/stopOrder","StopOrderMaster.csv",OrderDataType.STOP_ORDER.getValue());
    }

    public ObjectArrayList<OrderData> getStopLimitOrders() throws IOException, ParseException {
        return readMaster(DATA_PATH + "uncross/stopLimitOrder","StopLimitOrderMaster.csv",OrderDataType.STOP_LIMIT_ORDER.getValue());
    }

    public ObjectArrayList<OrderData> getHiddenOrders() throws IOException, ParseException {
        return readMaster(DATA_PATH + "uncross/hiddenOrder","HiddenOrderMaster.csv",OrderDataType.HIDDEN_ORDER.getValue());
    }

    public ObjectArrayList<OrderData> getTimeInForceOrders() throws IOException, ParseException {
        return readMaster(DATA_PATH + "uncross/timeInForce","TimeInForceMaster.csv",OrderDataType.TIME_IN_FORCE.getValue());
    }

    public ObjectArrayList<OrderData> getAuctionOrders() throws IOException, ParseException {
        return readMaster(DATA_PATH + "auction", "AuctionMaster.csv", OrderDataType.AUCTION_ORDER.getValue());
    }

    public ObjectArrayList<OrderData> getFilterAndUncrossOrders() throws IOException, ParseException {
        return readMaster(DATA_PATH + "filterAndUncross", "FilterAndUncrossMaster.csv", OrderDataType.FILTER_AND_UNCROSS_ORDER.getValue());
    }

    public ObjectArrayList<OrderData> getCancelOrders() throws IOException, ParseException {
        return readMaster(DATA_PATH + "cancelOrder", "CancelOrderMaster.csv", OrderDataType.CANCEL_ORDER.getValue());
    }

    public ObjectArrayList<OrderData> getReplaceOrders() throws IOException, ParseException {
        return readMaster(DATA_PATH + "replaceOrder", "ReplaceOrderMaster.csv", OrderDataType.REPLACE_ORDER.getValue());
    }

    private ObjectArrayList<OrderData> readMaster(String directory,String masterFileName,String type) throws IOException, ParseException {
        ObjectArrayList<OrderData> orderData = new ObjectArrayList<>();

        try(Reader in = new FileReader(directory + File.separator + masterFileName)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(MASTER_HEADER_MAPPING).withSkipHeaderRecord().parse(in);
            for (CSVRecord record : records) {
                OrderData od = new OrderData();
                od.setTestNumber(Integer.parseInt(record.get(MASTER_HEADER_MAPPING[0])));
                od.setDescription(record.get(MASTER_HEADER_MAPPING[1]));
                od.setType(type);

                String initFileName = record.get(MASTER_HEADER_MAPPING[2]);
                if(!initFileName.equals("")) {
                    ObjectArrayList<OrderEntry> initState = readOrders(directory + File.separator + initFileName);
                    od.setInitState(initState);
                }

                if(!type.equals(OrderDataType.AUCTION_ORDER.getValue())) {
                    String aggOrderFileName = record.get(MASTER_HEADER_MAPPING[3]);
                    ObjectArrayList<OrderEntry> aggOrders = readOrders(directory + File.separator + aggOrderFileName);
                    od.setAggOrder(aggOrders);
                }

                String expectedFileName = record.get(MASTER_HEADER_MAPPING[4]);
                ObjectArrayList<OrderEntry> expState =  readOrders(directory + File.separator + expectedFileName);
                od.setExpState(expState);

                String stockFileName = record.get(MASTER_HEADER_MAPPING[5]);
                Stock stock = readStock(directory + File.separator + stockFileName);
                od.setStock(stock);

                String tradeFileName = record.get(MASTER_HEADER_MAPPING[6]);
                ObjectArrayList<Trade> trades = readTrades(directory + File.separator + tradeFileName);
                od.setTrades(trades);

                orderData.add(od);
            }
        }

        return orderData;
    }

    private ObjectArrayList<OrderEntry> readOrders(String fileName) throws ParseException, IOException {
        ObjectArrayList<OrderEntry> orderEntries = new ObjectArrayList<>(0);

        if(fileName != null && new File(fileName).exists()) {
            try (Reader in = new FileReader(fileName)) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(ORDER_HEADER_MAPPING).withSkipHeaderRecord().parse(in);
                for (CSVRecord record : records) {
                    OrderEntry orderEntry = OrderEntryFactory.getOrderEntry();
                    orderEntry.setSide(record.get(ORDER_HEADER_MAPPING[0]).equals("B") ? (byte)1 : (byte)2);
                    orderEntry.setOrderId(Long.parseLong(record.get(ORDER_HEADER_MAPPING[1])));
                    orderEntry.setType(Byte.parseByte(record.get(ORDER_HEADER_MAPPING[2])));
                    if (!record.get(ORDER_HEADER_MAPPING[3]).equals("")) {
                        orderEntry.setMinExecutionSize(Integer.parseInt(record.get(ORDER_HEADER_MAPPING[3])));
                    } else {
                        orderEntry.setMinExecutionSize(0);
                    }

                    if(!record.get(ORDER_HEADER_MAPPING[4]).equals("")) {
                        String[] submittedTime = record.get(ORDER_HEADER_MAPPING[4]).split(":");
                        currentDate.setHours(Integer.parseInt(submittedTime[0]));
                        currentDate.setMinutes(Integer.parseInt(submittedTime[1]));
                        orderEntry.setSubmittedTime(currentDate.getTime());
                    }

                    if(!record.get(ORDER_HEADER_MAPPING[5]).equals("")) {
                        orderEntry.setQuantity(Integer.parseInt(record.get(ORDER_HEADER_MAPPING[5])));
                        orderEntry.setDisplayQuantity(orderEntry.getQuantity());
                    }

                    if(!record.get(ORDER_HEADER_MAPPING[6]).equals("")) {
                        orderEntry.setPrice(Long.parseLong(record.get(ORDER_HEADER_MAPPING[6])));
                    }else{
                        orderEntry.setPrice(0);
                    }

                    if(record.isSet(ORDER_HEADER_MAPPING[7]) && !record.get(ORDER_HEADER_MAPPING[7]).equals("")) {
                        orderEntry.setStopPrice(Long.parseLong(record.get(ORDER_HEADER_MAPPING[7])));
                    }else{
                        orderEntry.setStopPrice(orderEntry.getPrice());
                    }

                    if(record.isSet(ORDER_HEADER_MAPPING[8]) && !record.get(ORDER_HEADER_MAPPING[8]).equals("")) {
                        orderEntry.setTimeInForce(Byte.parseByte(record.get(ORDER_HEADER_MAPPING[8])));
                    }else{
                        orderEntry.setTimeInForce(TimeInForce.DAY.getValue());
                    }

                    if(record.isSet(ORDER_HEADER_MAPPING[9]) && !record.get(ORDER_HEADER_MAPPING[9]).equals("")
                            && !record.get(ORDER_HEADER_MAPPING[9]).equals("0")) {
                        long time = sdf.parse(record.get(ORDER_HEADER_MAPPING[9])).getTime();
                        orderEntry.setExpireTime(time);
                    }else{
                        orderEntry.setExpireTime(0);
                    }

                    orderEntry.setTrader(1);
                    orderEntry.setOrderStatus((byte) OrderStatusEnum.New.value());

                    orderEntries.add(orderEntry);
                }
            }
        }

        return orderEntries;
    }

    private Stock readStock(String fileName) throws ParseException, IOException {
        Stock stock = null;

        if(fileName != null && new File(fileName).exists()) {
            try (Reader in = new FileReader(fileName)) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(STOCK_HEADER_MAPPING).withSkipHeaderRecord().parse(in);
                for (CSVRecord record : records) {
                    stock = new Stock();
                    stock.setStockCode(Integer.parseInt(record.get(STOCK_HEADER_MAPPING[0])));
                    stock.setMRS(Integer.parseInt(record.get(STOCK_HEADER_MAPPING[1])));
                    stock.setTickSize(Integer.parseInt(record.get(STOCK_HEADER_MAPPING[2])));
                }
            }
        }

        return stock;
    }

    private ObjectArrayList<Trade> readTrades(String fileName) throws ParseException, IOException {
        ObjectArrayList<Trade> trades = new ObjectArrayList<>(0);

        if(fileName != null && new File(fileName).exists()) {
            try (Reader in = new FileReader(fileName)) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(TRADE_HEADER_MAPPING).withSkipHeaderRecord().parse(in);
                for (CSVRecord record : records) {
                    Trade trade = new Trade();
                    trade.setId(Long.parseLong(record.get(TRADE_HEADER_MAPPING[0])));
                    trade.setPrice(Long.parseLong(record.get(TRADE_HEADER_MAPPING[1])));
                    trade.setQuantity(Long.parseLong(record.get(TRADE_HEADER_MAPPING[2])));

                    trades.add(trade);
                }
            }
        }

        return trades;
    }
}
