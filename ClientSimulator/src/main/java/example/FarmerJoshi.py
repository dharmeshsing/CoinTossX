import jpype as jp

jp.startJVM(jp.getDefaultJVMPath(), "-ea", classpath = "/home/ivanjericevich/CoinTossX/ClientSimulator/build/classes/main") # Start JVM
utilities = jp.JClass("abm.Utilities") # Import class

clientId = 1
securityId = 1
client = utilities.loadClientData(1, 1) # Create and initialize client
client.sendStartMessage() # Login client to the gateways and connect to ports
client.submitOrder(1000, 100, Buy, Limit) # Submit order
client.sendEndMessage() # Logout client and close connections
jp.shutdownJVM() # Stop JVM