package dao;

import com.carrotsearch.hppc.ObjectArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import vo.StockVO;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;

/**
 * Created by dharmeshsing on 6/05/16.
 */
public class StockDAO {
    private static final String[] FILE_HEADER = {"SecurityId","StockCode","Name"};

    public static void writeCsvFile(String fileName,ObjectArrayList<StockVO> stockVOList) throws Exception {
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator());

        File stockFile = new File(fileName);
        boolean fileDeleted = stockFile.delete();

        if(fileDeleted) {
            try (
                    FileWriter fileWriter = new FileWriter(fileName);
                    CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat)
            ) {

                csvFilePrinter.printRecord(FILE_HEADER);

                for (int i=0, max=stockVOList.size(); i < max; i++) {
                    StockVO stockVO = stockVOList.get(i);
                    if(stockVO != null) {
                        csvFilePrinter.printRecord(stockVO);
                    }
                }

            }
        }
    }

    public static void loadStocks(String fileName,ObjectArrayList<StockVO> stockVOList) throws Exception {
        try(Reader in = new FileReader(fileName)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(FILE_HEADER).withSkipHeaderRecord().parse(in);

            for (CSVRecord record : records) {
                StockVO stockVO = new StockVO();
                stockVO.setSecurityId(Integer.parseInt(record.get(FILE_HEADER[0])));
                stockVO.setStockCode(record.get(FILE_HEADER[1]));
                stockVO.setName(record.get(FILE_HEADER[2]));

                stockVOList.add(stockVO);
            }
        }
    }
}
