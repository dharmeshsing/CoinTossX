package dao;

import com.carrotsearch.hppc.ObjectIntHashMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by dharmeshsing on 13/08/15.
 */
public class TraderDAO {
    private static final String [] TRADER_HEADER_MAPPING = {"TraderId","TraderName"};
    private static ObjectIntHashMap<String> traders;

    public static void loadTraders(String dataPath) throws IOException {
        traders = new ObjectIntHashMap<>();
        try(Reader in = new FileReader(dataPath + File.separator + "Trader.csv")) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(TRADER_HEADER_MAPPING).withSkipHeaderRecord().parse(in);
            for (CSVRecord record : records) {
                int traderId = Integer.parseInt(record.get(TRADER_HEADER_MAPPING[0]));
                String traderName = record.get(TRADER_HEADER_MAPPING[1]);

                traders.put(traderName,traderId);

            }
        }
    }

    public static int getTrader(String traderName){
        return traders.get(traderName);
    }
    public static String getTraderById(int tradeId){
        int[] values = traders.values;
        int size = values.length;

        for(int i=0; i<size; i++){
            if(values[i] == tradeId){
                return (String)traders.keys[i];
            }
        }

        return null;
    }
}
