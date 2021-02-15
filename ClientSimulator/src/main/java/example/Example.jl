#=
Example:
- Julia version: 1.5.3
- Java version: 1.8.0_271
- Authors: Ivan Jericevich, Dharmesh Sing, Tim Gebbie
- Structure:
    1. Preliminaries
    2. Login and start session
    3. Submit orders
    4. Market data updates
    5. End session and logout
=#
#---------------------------------------------------------------------------------------------------


#----- Preliminaries -----#
# Import the Java-Julia interface package
using JavaCall
cd(@__DIR__); clearconsole() # pwd()
# Add the path to java classes as well as the path to the ".jar" files containing the required java dependencies
JavaCall.addClassPath("/home/ivanjericevich/CoinTossX/ClientSimulator/build/classes/main") # JavaCall.getClassPath()
JavaCall.addClassPath("/home/ivanjericevich/CoinTossX/ClientSimulator/build/install/ClientSimulator/lib/*.jar")
# Initialize JVM
JavaCall.init()
# Import the class containing the reqired methods
utilities = @jimport example.Utilities # JavaCall.listmethods(utilities)
#---------------------------------------------------------------------------------------------------


#----- Login and start session -----#
# Define the client ID corresponding the client to be logged in as well as the security ID corresponding to the security in which they will trade
clientId = 1; securityId = 1
# Load the simulation settings as well as all client data (ports, passwords and IDs) and create/initialize client
client = jcall(utilities, "loadClientData", JavaObject{Symbol("client.Client")}, (jint, jint), clientId, securityId)
# Start trading session by Logging in client to the gateways and connecting to ports
jcall(client, "sendStartMessage", Nothing, ())
#---------------------------------------------------------------------------------------------------


#----- Submit orders -----#
# Arguments for "submitOrder": volume, price, side, order type, time in force, display quantity, min execution size, stop price
jcall(client, "submitOrder", Nothing, (jlong, jlong, JString, JString, JString, jlong, jlong, jlong), 1000, 99, "Buy", "Limit", "Day", 1000, 0, 0) # Buy limit order
jcall(client, "submitOrder", Nothing, (jlong, jlong, JString, JString, JString, jlong, jlong, jlong), 1000, 101, "Sell", "Limit", "Day", 1000, 0, 0) # Sell limit order
jcall(client, "submitOrder", Nothing, (jlong, jlong, JString, JString, JString, jlong, jlong, jlong), 1000, 0, "Buy", "Market", "Day", 1000, 0, 0) # Buy market order
jcall(client, "submitOrder", Nothing, (jlong, jlong, JString, JString, JString, jlong, jlong, jlong), 1000, 0, "Buy", "StopLimit", "Day", 1000, 0, 0) # Stop buy limit order
jcall(client, "submitOrder", Nothing, (jlong, jlong, JString, JString, JString, jlong, jlong, jlong), 1000, 0, "Buy", "Stop", "Day", 1000, 0, 0) # Stop buy market order
# Arguments for "cancelOrder": order id, side
jcall(client, "cancelOrder", Nothing, (jlong, jlong), 1, "", "Buy") # Cancel limit order
#---------------------------------------------------------------------------------------------------


#----- Market data updates -----#
jcall(client, "calcVWAP", jlong, (JString,), "Buy") # VWAP of buy side of LOB
jcall(client, "getBid", jlong, ()) # Best bid price
jcall(client, "getBidQuantity", jlong, ()) # Best bid volume
jcall(client, "getOffer", jlong, ()) # Best ask price
jcall(client, "getOfferQuantity", jlong, ()) # Best ask volume
jcall(client, "waitForMarketDataUpdate", Nothing, ()) # Pauses the client until a new event occurs
jcall(client, "isAuction", jboolean, ()) # Current trading session
#---------------------------------------------------------------------------------------------------


#----- End session and logout -----#
# End trading session by logging out client and closing connections
jcall(client, "sendEndMessage", Nothing, ()); jcall(client, "close", Nothing, ())
#---------------------------------------------------------------------------------------------------
