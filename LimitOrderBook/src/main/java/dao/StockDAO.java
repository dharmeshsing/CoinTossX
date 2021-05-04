package dao;

import com.carrotsearch.hppc.LongObjectHashMap;
import orderBook.Stock;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class StockDAO {
    private static final String [] STOCK_HEADER_MAPPING = {"StockCode","MRS","TickSize"};
    private static LongObjectHashMap<Stock> stocks;

    public static Stock getStock(long stockCode){
        return stocks.get(stockCode);

    }

    public static void loadStocks(String dataPath){
        stocks = new LongObjectHashMap<>();
            try(Reader in = new FileReader(dataPath + File.separator + "Stock.csv")) {
                Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(STOCK_HEADER_MAPPING).withSkipHeaderRecord().parse(in);
                for (CSVRecord record : records) {
                    Stock stock = new Stock();
                    stock.setStockCode(Integer.parseInt(record.get(STOCK_HEADER_MAPPING[0])));
                    stock.setMRS(Integer.parseInt(record.get(STOCK_HEADER_MAPPING[1])));
                    stock.setTickSize(Integer.parseInt(record.get(STOCK_HEADER_MAPPING[2])));

                    stocks.put(stock.getStockCode(),stock);

                }
            } catch (IOException e) {
                //TODO: handle exception
                e.printStackTrace();
        }
    }
}
