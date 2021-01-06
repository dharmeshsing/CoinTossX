package abm;

import client.*;
import sbe.msg.*;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.time.LocalDateTime;
import com.carrotsearch.hppc.IntObjectMap;

public class FarmerJoshi {
    private static final String PROPERTIES_FILE =  "simulation.properties";
    private static Properties properties = new Properties();

    private static void loadProperties(Properties properties, String propertiesFile) throws IOException {
        try (InputStream inputStream = FarmerJoshi.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new IOException("Unable to load properties file " + propertiesFile);
            }
        }
    }

    //----- Implementation -----//
    public static void main (String[] args) throws Exception {
        int clientId = 1;
        int securityId = 1;

        loadProperties(properties, PROPERTIES_FILE);
        String dataPath = properties.get("DATA_PATH").toString();
        IntObjectMap<ClientData> clientData = ClientData.loadClientDataData(dataPath);

        Client client = new Client(clientData.get(clientId), securityId);
        client.init(properties);
        client.sendStartMessage();
        System.out.println("Start at " + LocalDateTime.now());

        client.submitOrder(1000, 150, SideEnum.Sell, OrdTypeEnum.Limit);
        client.submitOrder(1000, 150, SideEnum.Buy, OrdTypeEnum.Limit);

        client.sendEndMessage();
        client.close();
        System.out.println("Complete at " + LocalDateTime.now());

        System.exit(0);
    }
    //------------------------------------------------------------------------------------------------------------------
}
