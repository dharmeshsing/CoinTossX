package dao;

import com.carrotsearch.hppc.ObjectArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import vo.ClientVO;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;

public class ClientDAO {
    private static final String[] FILE_HEADER = {"CompID","Password","NGInputURL","NGInputStreamId","NGOutputURL",
                                                   "NGOutputStreamId","MDGInputURL","MDGInputStreamId",
                                                    "MDGOutputURL","MDGOutputStreamId","SecurityId"};

    public static void writeCsvFile(String fileName,ObjectArrayList<ClientVO> clientVOList) throws Exception {
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator());

        File clientFile = new File(fileName);
        boolean fileDeleted = clientFile.delete();

        if(fileDeleted) {
            try (
                    FileWriter fileWriter = new FileWriter(fileName);
                    CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat)
            ) {

                csvFilePrinter.printRecord(FILE_HEADER);
                for (int i=0, max=clientVOList.size(); i < max; i++) {
                    ClientVO clientVO = clientVOList.get(i);
                    if(clientVO !=null) {
                        csvFilePrinter.printRecord(clientVO);
                    }
                }

            }
        }
    }

    public static void loadClients(String fileName,ObjectArrayList<ClientVO> clientVOList) throws Exception {
        try(Reader in = new FileReader(fileName)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(FILE_HEADER).withSkipHeaderRecord().parse(in);

            for (CSVRecord record : records) {
                ClientVO clientVO = new ClientVO();
                clientVO.setCompId(Integer.parseInt(record.get(FILE_HEADER[0])));
                clientVO.setPassword(record.get(FILE_HEADER[1]));

                clientVO.setTradingInputURL(record.get(FILE_HEADER[2]));
                clientVO.setTradingInputStreamId(Integer.parseInt(record.get(FILE_HEADER[3])));

                clientVO.setTradingOutputURL(record.get(FILE_HEADER[4]));
                clientVO.setTradingOutputStreamId(Integer.parseInt(record.get(FILE_HEADER[5])));

                clientVO.setMarketDataInputURL(record.get(FILE_HEADER[6]));
                clientVO.setMarketDataInputStreamId(Integer.parseInt(record.get(FILE_HEADER[7])));

                clientVO.setMarketDataOutputURL(record.get(FILE_HEADER[8]));
                clientVO.setMarketDataOutputStreamId(Integer.parseInt(record.get(FILE_HEADER[9])));

                clientVO.setSecurityId(Integer.parseInt(record.get(FILE_HEADER[10])));

                clientVOList.add(clientVO);
            }
        }
    }
}
