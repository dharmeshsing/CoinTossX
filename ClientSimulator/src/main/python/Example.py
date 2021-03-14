'''
Example:
- Python version: 3.8.5
- Java version: 1.8.0_271
- Authors: Ivan Jericevich, Dharmesh Sing, Tim Gebbie
- Structure:
    1. Preliminaries
    2. Login and start session
    3. Submit orders
    4. Market data updates
    5. End session and logout
'''
#---------------------------------------------------------------------------------------------------


#----- Preliminaries -----#
# Import the Java-Python interface module
import jpype as jp
# Initialize/start the JVM
jp.startJVM(jp.getDefaultJVMPath(), "-ea", classpath = "/home/ivanjericevich/CoinTossX/ClientSimulator/build/classes/main") # Start JVM
# Add the path to the ".jar" files containing the required java dependencies
jpype.addClassPath("/home/ivanjericevich/CoinTossX/ClientSimulator/build/install/ClientSimulator/lib/*.jar")
# Import the class containing the reqired methods
utilities = jp.JClass("example.Utilities")
#---------------------------------------------------------------------------------------------------


#----- Login and start session -----#
# Define the client ID corresponding the client to be logged in as well as the security ID corresponding to the security in which they will trade
clientId = 1
securityId = 1
# Load the simulation settings as well as all client data (ports, passwords and IDs) and create/initialize client
client = utilities.loadClientData(clientId, securityId)
# Start trading session by Logging in client to the gateways and connecting to ports
client.sendStartMessage()
#---------------------------------------------------------------------------------------------------


#----- Submit orders -----#
# Arguments for "submitOrder": volume, price, side, order type, time in force, display quantity, min execution size, stop price
client.submitOrder(1000, 99, "Buy", "Limit", "Day", 1000, 0, 0) # Buy limit order
client.submitOrder(1000, 101, "Sell", "Limit", "Day", 1000, 0, 0) # Sell limit order
client.submitOrder(1000, 0, "Buy", "Market", "Day", 1000, 0, 0) # Buy market order
client.submitOrder(1000, 0, "Buy", "StopLimit", "Day", 1000, 0, 0) # Stop buy limit order
client.submitOrder(1000, 0, "Buy", "Stop", "Day", 1000, 0, 0) # Stop buy market order
# Arguments for "cancelOrder": order id, side
client.cancelOrder("1", "Buy") # Cancel limit order
#---------------------------------------------------------------------------------------------------


#----- Market data updates -----#
client.calcVWAP("Buy") # VWAP of buy side of LOB
client.getBid() # Best bid price
client.getBidQuantity() # Best bid volume
client.getOffer() # Best ask price
client.getOfferQuantity() # Best ask volume
client.waitForMarketDataUpdate() # Pauses the client until a new event occurs
client.isAuction() # Current trading session
#---------------------------------------------------------------------------------------------------


#----- End session and logout -----#
# End trading session by logging out client and closing connections
client.sendEndMessage()
client.close()
# Stop JVM
jp.shutdownJVM()
#---------------------------------------------------------------------------------------------------