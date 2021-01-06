#=
FarmerJoshi:
- Julia version: 1.5.3
- Java version: 1.8.0_271
- Author: Ivan Jericevich
- Date: 2021-01-01
- Structure:
    - 1. Preliminaries
    - 2. Supplementary definitions
    - 3. Login and submit orders
=#
#---------------------------------------------------------------------------------------------------


### 1. Preliminaries
using JavaCall, Dates
cd(@__DIR__) # pwd()
JavaCall.addClassPath("/home/ivanjericevich/CoinTossX/ClientSimulator/build/classes/main") # JavaCall.getClassPath()
JavaCall.init() # Initialize JVM
utilities = @jimport abm.Utilities # JavaCall.listmethods(utilities)
#---------------------------------------------------------------------------------------------------


### 2. Supplementary definitions
mutable struct Order
    volume::jlong
    price::jlong
    side::JavaObject{Symbol("SideEnum")}
    type::JavaObject{Symbol("OrderTypeEnum")}
end

function Login()
end

function SubmitOrder(order::Order)
end

function Logout()
end
#---------------------------------------------------------------------------------------------------


### 3. Login and submit orders
clientId = 1; securityId = 1
client = jcall(utilities, "loadClientData", JavaObject{Symbol("Client")}, (jint, jint), clientId, securityId) # Create and initialize client
jcall(client, "sendStartMessage", Nothing, ()); println("Started at" * Dates.format(now(), "HH:MM")) # Login client to the gateways and connect to ports
jcall(client, "submitOrder", Nothing, (jlong, jlong, SideEnum, OrderTypeEnum), 1000, 100, Buy, Limit) # Submit order
jcall(client, "sendEndMessage", Nothing, ()); jcall(client, "close", Nothing, ()) # Logout client and close connections
