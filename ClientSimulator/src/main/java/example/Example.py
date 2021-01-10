'''
Example:
- Python version: 3.8.5
- Java version: 1.8.0_271
- Authors: Ivan Jericevich, Dharmesh Sing, Tim Gebbie
- Structure:
    1. Preliminaries
    2. Login and submit orders
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


#----- Login and submit orders -----#
# Define the client ID corresponding the client to be logged in as well as the security ID corresponding to the security in which they will trade
clientId = 1
securityId = 1
# Load the simulation settings as well as all client data (ports, passwords and IDs) and create/initialize client
client = utilities.loadClientData(clientId, securityId)
# Start trading session by Logging in client to the gateways and connecting to ports
client.sendStartMessage()
# Submit orders
client.submitOrder(1000, 100, "Buy", "Limit", "Day")
# End trading session by logging out client and closing connections
client.sendEndMessage()
client.close()
# Stop JVM
jp.shutdownJVM()
#---------------------------------------------------------------------------------------------------
