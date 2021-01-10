#=
Example:
- Julia version: 1.5.3
- Java version: 1.8.0_271
- Authors: Ivan Jericevich, Dharmesh Sing, Tim Gebbie
- Structure:
    1. Preliminaries
    2. Login and submit orders
=#
#---------------------------------------------------------------------------------------------------


#----- Preliminaries -----#
# Import the Java-Julia interface package
using JavaCall
cd(@__DIR__) # pwd()
# Add the path to java classes as well as the path to the ".jar" files containing the required packages
JavaCall.addClassPath("/home/ivanjericevich/CoinTossX/ClientSimulator/build/classes/main") # JavaCall.getClassPath()
JavaCall.addClassPath("/home/ivanjericevich/CoinTossX/ClientSimulator/build/install/ClientSimulator/lib/*.jar")
# Initialize JVM
JavaCall.init()
# Import the class containing the reqired methods
utilities = @jimport example.Utilities # JavaCall.listmethods(utilities)
#---------------------------------------------------------------------------------------------------


#----- Supplementary definitions -----#
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


#----- Login and submit orders -----#
# Define the client ID corresponding the client to be logged in as well as the security ID corresponding to the security in which they will trade
clientId = 1; securityId = 1
# Load the simulation settings as well as all client data (ports, passwords and IDs) and create/initialize client
client = jcall(utilities, "loadClientData", JavaObject{Symbol("client.Client")}, (jint, jint), clientId, securityId)
# Start trading session by Logging in client to the gateways and connecting to ports
jcall(client, "sendStartMessage", Nothing, ()); println("Started at" * Dates.format(now(), "HH:MM"))
# Submit orders
jcall(client, "submitOrder", Nothing, (jlong, jlong, SideEnum, OrderTypeEnum), 1000, 100, Buy, Limit)
# End trading session by logging out client and closing connections
jcall(client, "sendEndMessage", Nothing, ()); jcall(client, "close", Nothing, ())
#---------------------------------------------------------------------------------------------------
