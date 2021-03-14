/*
Example:
- Java version: 1.8.0_271
- Authors: Ivan Jericevich, Dharmesh Sing, Tim Gebbie
- Structure:
    1. Dependencies
    2. Supplementary methods
    3. Implementation
        - Login and start session
        - Submit orders
        - Market data updates
        - End session and logout
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

        //----- Login and start session -----//
        int clientId = 1; int securityId = 1; // Define the client ID corresponding the client to be logged in as well as the security ID corresponding to the security in which they will trade
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

        //----- Submit orders -----//
        // Arguments for "submitOrder": volume, price, side, order type, time in force, display quantity, min execution size, stop price
        client.submitOrder("1", 1000, 99, "Buy", "Limit", "Day", 1000, 0, 0); // Buy limit order
        client.submitOrder("2", 1000, 101, "Sell", "Limit", "Day", 1000, 0, 0); // Sell limit order
        client.submitOrder("3", 1000, 0, "Buy", "Market", "Day", 1000, 0, 0); // Buy market order
        client.submitOrder("4", 1000, 0, "Buy", "StopLimit", "Day", 1000, 0, 0); // Stop buy limit order
        client.submitOrder("5", 1000, 0, "Buy", "Stop", "Day", 1000, 0, 0); // Stop buy market order
        client.cancelOrder("1", "Buy"); // Cancel limit order

        //----- Market data updates -----//
        client.calcVWAP("Buy"); // VWAP of buy side of LOB
        client.getBid(); // Best bid price
        client.getBidQuantity(); // Best bid volume
        client.getOffer(); // Best ask price
        client.getOfferQuantity(); // Best ask volume
        client.waitForMarketDataUpdate(); // Pauses the client until a new event occurs
        client.isAuction(); // Current trading session

        //----- End trading session by logging out client and closing connections -----//
        client.sendEndMessage();
        client.close();
        System.out.println("Complete at " + LocalDateTime.now());

        System.exit(0);
    }
    //------------------------------------------------------------------------------------------------------------------
}
