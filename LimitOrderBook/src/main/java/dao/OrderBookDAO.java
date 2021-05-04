package dao;

import com.carrotsearch.hppc.LongObjectHashMap;
import orderBook.OrderBook;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class OrderBookDAO {
    private static final String [] ORDER_BOOK_HEADER_MAPPING = {"SecurityId"};

    public static LongObjectHashMap<OrderBook> loadOrderBooks(String dataPath) throws IOException{
        LongObjectHashMap<OrderBook>  orderBooks = new LongObjectHashMap<>();
        try(Reader in = new FileReader(dataPath + File.separator + "Stock.csv")) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(ORDER_BOOK_HEADER_MAPPING).withSkipHeaderRecord().parse(in);
            for (CSVRecord record : records) {
                int securityId = Integer.parseInt(record.get(ORDER_BOOK_HEADER_MAPPING[0]));
                OrderBook orderBook = new OrderBook(securityId);
                orderBooks.put(securityId,orderBook);

            }
        }

        return orderBooks;
    }
}
