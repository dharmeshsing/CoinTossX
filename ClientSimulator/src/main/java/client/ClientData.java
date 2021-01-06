package client;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.carrotsearch.hppc.IntObjectMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

/**
 * Created by dharmeshsing on 2/08/15.
 */
public class ClientData {
    private int compID;
    private String password;
    private String ngInputURL;
    private int ngInputStreamId;
    private String ngOutputURL;
    private int ngOutputStreamId;
    private String mdgInputURL;
    private int mdgInputStreamId;
    private String mdgOutputURL;
    private int mdgOutputStreamId;
    private boolean loggedIn;

    public static final String [] CLIENT_HEADER_MAPPING = {"CompID","Password","NGInputURL","NGInputStreamId","NGOutputURL","NGOutputStreamId","MDGInputURL","MDGInputStreamId","MDGOutputURL","MDGOutputStreamId"};

    public static IntObjectMap<ClientData> loadClientDataData(String dataPath) throws Exception {
        IntObjectMap<ClientData> clients = new IntObjectHashMap<>();
        try(Reader in = new FileReader(dataPath + File.separator + "clientData.csv")) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(CLIENT_HEADER_MAPPING).withSkipHeaderRecord().parse(in);
            for(CSVRecord record : records) {
                ClientData client = new ClientData();
                int compId = Integer.parseInt(record.get(CLIENT_HEADER_MAPPING[0]));
                client.setCompID(compId);
                client.setPassword(record.get(CLIENT_HEADER_MAPPING[1]));
                client.setNgInputURL(record.get(CLIENT_HEADER_MAPPING[2]));
                client.setNgInputStreamId(Integer.parseInt(record.get(CLIENT_HEADER_MAPPING[3])));

                client.setNgOutputURL(record.get(CLIENT_HEADER_MAPPING[4]));
                client.setNgOutputStreamId(Integer.parseInt(record.get(CLIENT_HEADER_MAPPING[5])));

                client.setMdgInputURL(record.get(CLIENT_HEADER_MAPPING[6]));
                client.setMdgInputStreamId(Integer.parseInt(record.get(CLIENT_HEADER_MAPPING[7])));

                client.setMdgOutputURL(record.get(CLIENT_HEADER_MAPPING[8]));
                client.setMdgOutputStreamId(Integer.parseInt(record.get(CLIENT_HEADER_MAPPING[9])));

                clients.put(compId,client);

            }
        }


        return clients;
    }

    public int getCompID() {
        return compID;
    }

    public void setCompID(int compID) {
        this.compID = compID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getNgInputURL() {
        return ngInputURL;
    }

    public void setNgInputURL(String ngInputURL) {
        this.ngInputURL = ngInputURL;
    }

    public String getNgOutputURL() {
        return ngOutputURL;
    }

    public void setNgOutputURL(String ngOutputURL) {
        this.ngOutputURL = ngOutputURL;
    }

    public int getNgInputStreamId() {
        return ngInputStreamId;
    }

    public void setNgInputStreamId(int ngInputStreamId) {
        this.ngInputStreamId = ngInputStreamId;
    }

    public int getNgOutputStreamId() {
        return ngOutputStreamId;

    }

    public void setNgOutputStreamId(int ngOutputStreamId) {
        this.ngOutputStreamId = ngOutputStreamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientData client = (ClientData) o;

        return compID == client.compID;

    }

    @Override
    public int hashCode() {
        return compID;
    }

    public String getMdgOutputURL() {
        return mdgOutputURL;
    }

    public void setMdgOutputURL(String mdgOutputURL) {
        this.mdgOutputURL = mdgOutputURL;
    }

    public int getMdgOutputStreamId() {
        return mdgOutputStreamId;
    }

    public void setMdgOutputStreamId(int mdgOutputStreamId) {
        this.mdgOutputStreamId = mdgOutputStreamId;
    }

    public String getMdgInputURL() {
        return mdgInputURL;
    }

    public void setMdgInputURL(String mdgInputURL) {
        this.mdgInputURL = mdgInputURL;
    }

    public int getMdgInputStreamId() {
        return mdgInputStreamId;
    }

    public void setMdgInputStreamId(int mdgInputStreamId) {
        this.mdgInputStreamId = mdgInputStreamId;
    }
}
