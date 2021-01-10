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
# Add the path to java classes as well as the path to the ".jar" files containing the required java dependencies
JavaCall.addClassPath("/home/ivanjericevich/CoinTossX/ClientSimulator/build/classes/main") # JavaCall.getClassPath()
JavaCall.addClassPath("/home/ivanjericevich/CoinTossX/ClientSimulator/build/install/ClientSimulator/lib/*.jar")
# Initialize JVM
JavaCall.init()
# Import the class containing the reqired methods
utilities = @jimport example.Utilities # JavaCall.listmethods(utilities)
#---------------------------------------------------------------------------------------------------


#----- Login and submit orders -----#
# Define the client ID corresponding the client to be logged in as well as the security ID corresponding to the security in which they will trade
clientId = 1; securityId = 1
# Load the simulation settings as well as all client data (ports, passwords and IDs) and create/initialize client
client = jcall(utilities, "loadClientData", JavaObject{Symbol("client.Client")}, (jint, jint), clientId, securityId)
# Start trading session by Logging in client to the gateways and connecting to ports
jcall(client, "sendStartMessage", Nothing, ())
# Submit orders
jcall(client, "submitOrder", Nothing, (jlong, jlong, JString, JString, JString), 1000, 100, "Buy", "Limit", "Day")
# End trading session by logging out client and closing connections
jcall(client, "sendEndMessage", Nothing, ()); jcall(client, "close", Nothing, ())
#---------------------------------------------------------------------------------------------------








#----- Supplementary definitions -----#
mutable struct Order
    volume::Integer
    price::Integer
    side::String
    type::String
    tif::String
    function Order(volume::Integer, price::Integer, side::String, type::String, tif::String)
        if (side ∈ ["Buy", "Sell"]) && (type ∈ ["Market", "Limit", "Stop", "StopLimit"]) && (tif ∈ ["Day", "GTC", "IOC", "FOK", "OPG", "GTD", "GTT", "GFA", "GFX", "ATC", "CPX"])
            new(volume, price, side, type, tif)
        else
            error("Incorrect order specification")
        end
    end
end
abstract type Agent end # Define abstract type so that functions can take in both fundamentalist and chartist
struct Client <: Agent
    id::Integer
    securityId::Integer
    javaObject::JavaObject{Symbol("client.Client")}
end
function Login(clientId::Integer, securityId::Integer)::Client
    utilities = @jimport example.Utilities
    javaObject = jcall(utilities, "loadClientData", JavaObject{Symbol("client.Client")}, (jint, jint), clientId, securityId)
    jcall(javaObject, "sendStartMessage", Nothing, ())
    client = Client(clientId, securityId, javaObject)
    return client
end
function SubmitOrder(client::Client, order::Order)
    jcall(client.javaObject, "submitOrder", Nothing, (jlong, jlong, JString, JString, JString), order.volume, order.price, order.side, order.type, order.tif)
end
function Logout(client::Client)
    jcall(client.javaObject, "sendEndMessage", Nothing, ());
    jcall(client.javaObject, "close", Nothing, ())
end

client = Login(1, 1)
order = Order(1000, 100, "Buy", "Limit", "Day")
SubmitOrder(client, order)
Logout(client)
#---------------------------------------------------------------------------------------------------
