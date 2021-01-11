/*
Example:
- Java version: 1.8.0_271
- Authors: Ivan Jericevich, Dharmesh Sing, Tim Gebbie
- Structure:
    1. Dependencies
    2. Supplementary method
    3. Implementation
*/
//---------------------------------------------------------------------------------------------------

//----- Import the necessary dependencies -----//
package example;

import client.*;
import sbe.msg.*;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.time.LocalDateTime;
import com.carrotsearch.hppc.IntObjectMap;
//---------------------------------------------------------------------------------------------------

public class Example {
    private static final String PROPERTIES_FILE =  "simulation.properties"; // The name of the file specifying the simulation configuration
    private static Properties properties = new Properties();

    //----- Supplementary method for extracting the simulation settings -----//
    private static void loadProperties(Properties properties, String propertiesFile) throws IOException {
        try (InputStream inputStream = Example.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new IOException("Unable to load properties file " + propertiesFile);
            }
        }
    }
    //---------------------------------------------------------------------------------------------------

    //----- Implementation -----//
    public static void main (String[] args) throws Exception {
        // Define the client ID corresponding the client to be logged in as well as the security ID corresponding to the security in which they will trade
        int clientId = 1;
        int securityId = 1;

        // Load the simulation settings as well as all client data (ports, passwords and IDs)
        loadProperties(properties, PROPERTIES_FILE);
        String dataPath = properties.get("DATA_PATH").toString();
        IntObjectMap<ClientData> clientData = ClientData.loadClientDataData(dataPath);

        // Create and initialize client
        Client client = new Client(clientData.get(clientId), securityId);
        client.init(properties);

        // Start trading session by Logging in client to the gateways and connecting to ports
        client.sendStartMessage();
        System.out.println("Start at " + LocalDateTime.now());

        // Submit orders
        client.submitOrder(1000, 150, "Sell", "Limit", "Day");
        client.submitOrder(1000, 150, "Buy", "Limit", "Day");

        // End trading session by logging out client and closing connections
        client.sendEndMessage();
        client.close();
        System.out.println("Complete at " + LocalDateTime.now());

        System.exit(0);
    }
    //------------------------------------------------------------------------------------------------------------------
}
