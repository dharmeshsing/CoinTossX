package abm;

import client.*;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.time.LocalDateTime;
import com.carrotsearch.hppc.IntObjectMap;

public class Utilities {
    private static final String PROPERTIES_FILE =  "simulation.properties";
    private static Properties properties = new Properties();
    IntObjectMap<ClientData> clientData;

    private static void loadProperties(Properties properties, String propertiesFile) throws IOException {
        try (InputStream inputStream = FarmerJoshi.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new IOException("Unable to load properties file " + propertiesFile);
            }
        }
    }

    public static Client loadClientData(int clientId, int securityId) throws Exception {
        loadProperties(properties, PROPERTIES_FILE);
        String dataPath = properties.get("DATA_PATH").toString();
        IntObjectMap<ClientData> clientData = ClientData.loadClientDataData(dataPath);
        Client client = new Client(clientData.get(clientId), securityId);
        client.init(properties);
        return client;
    }
    //------------------------------------------------------------------------------------------------------------------
}
