require(rJava)
.jinit()
utilities = .jnew("abm.Utilities")
.jmethods(s,"print")

clientId = 1; securityId = 1
client = .jcall(utilties, returnSig = "Lclient/Client;", method = "loadClientData", clientId, securityId) # Create and initialize client
.jcall(client, returnSig = "V", method = "sendStartMessage") # Login client to the gateways and connect to ports
.jcall(client, returnSig = "V", method = "submitOrder", 1000, 100, Buy, Limit) # Submit order
.jcall(client, returnSig = "V", method = "sendEndMessage") # Logout client and close connections