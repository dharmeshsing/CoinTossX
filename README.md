# CoinTossX <img align="right" width="200" src=".github/images/Logo.PNG">

![Build](https://github.com/IvanJericevich/CoinTossX/workflows/Build/badge.svg)

Below are the instructions for deploying CoinTossX to a user's local machine or virtual machine in a cloud environment. These instructions apply to Windows, Linux and OSX operating systems. CoinTossX is a Java web application and is built using the [Gradle](https://gradle.org/) build tool. The user need __not__ install Gradle or change the version of Gradle installed on their system as the project uses the Gradle wrapper to download and run the required Gradle version. Currently CoinTossX is compatible with Java version 8 and Gradle version 6.7.1. The application can be started from the command line, however, it is recommended that the user make use of a Java IDE such as Eclipse or IntelliJ IDEA to automate the start-up process and simplify deployment.

## Quick start
![](.github/images/Terminal-Instructions.gif)

## Prerequisites
* JDK version 8 (see Installation section for more details)
* A computer with 4 or more CPU cores and a sufficient amount of RAM (ideally 4 cores 32 GB but can be less depending on the number of clients and stocks used).
* A Java IDE such as Eclipse or IntelliJ IDEA.
* The user may be required to run commands using Command Prompt (Windows) or Bash (Linux and OSX).

## Additional resources
* [Tutorial for building a java project with gradle.](https://docs.gradle.org/current/samples/sample_building_java_applications.html).
* [Tutorial for building a simple Java web application.](https://www.javahelps.com/2015/04/java-web-application-hello-world.html)
* [Introduction to Spring Boot.](https://www.tutorialspoint.com/spring_boot/spring_boot_introduction.htm)
* [Introduction to Apache Wicket.](https://wicket.apache.org/learn/examples/index.html)
* [Tutorial for setting up a student Azure account.](https://www.learningjournal.guru/courses/modern-web-development/core-concepts/free-vm-in-azure/)
* [Instructions for the installation of Oracle JDK 8 and configuration of the system environment variables on Linux](https://www.javahelps.com/2015/03/install-oracle-jdk-in-ubuntu.html)
* Calling Java in [Julia](https://juliainterop.github.io/JavaCall.jl/), [Python](https://jpype.readthedocs.io/en/latest/) and [R](https://cran.r-project.org/web/packages/rJava/index.html).

## Installation
If the correct version of Java is already installed and configured correctly, the user can skip step 1 below.
1. CoinTossX is currently only compatible with version 8 of Java. Therefore, the user should install version 8 of either the [Oracle](https://www.oracle.com/za/java/technologies/javase/javase-jdk8-downloads.html) or Open JDK (Java Development Kit). Note that the user should install the JDK (which includes the JRE), not only the JRE. For simplicity, it is assumed that Windows and OSX users will install the Oracle JDK while Linux users will install the Open JDK.
    * **Windows** - After installing Oracle JDK 8 using the link above, if not done so automatically by the java install wizard, ensure that `JAVA_HOME` is set and that the java executable is set in the `path` environment variable. To set/add java to the system's `JAVA_HOME` and `path` environment variables, go to `Settings > Advanced System Settings > Environment Variables > System Variables`. Then add the location of the java installation to a new variable called `JAVA_HOME` which points to the relevant java distribution (e.g. `C:/Program Files/Java/jdk1.8.0_271`). Thereafter append a new pointer in the `path` environment variable by adding `%JAVA_HOME%/bin`.
    * **Linux** - Installation of Open JDK 8 is done by executing the commands below:
        ```bash
        sudo apt-get update
        sudo apt-get install openjdk-8-jdk
        ```
        After this, if the correct version of Java is still not being used, the user can switch to the correct version using
        ```bash
        sudo update-alternatives --set java /usr/lib/jvm/jdk1.8.0_[version]/bin/java
        ```
    * **OSX** - To set `JAVA_HOME` run
        ```bash
        export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_[version].jdk/Contents/Home
        ```
2. Clone the CoinTossX repository (using either `git` in the terminal or Github Desktop). The user can clone the repository to any location of their choosing.
    ```bash
    cd [directory to clone into]
    git clone https://github.com/dharmeshsing/CoinTossX.git
    ```
   The next set of instructions provides guidelines for the deployment of CoinTossX.
3. By this point the user will have cloned the repository to a location of their choosing for either local or remote deployment. Depending on the operating system, different deployment configurations would need to be employed - hence the multiple configuration and deployment files. The files with the `.properties` extensions define the required ports for each component as well as user specific path definitions. To match the location to which the repository was cloned, the user would have to configure the [local.properties](local.properties) file (for Linux) or the [windows.properties](windows.properties) file (for Windows) to correspond with the user's directories. For remote deployment the user would have to configure the [remote.properties](remote.properties) with the corresponding paths on the remote server. The path variables which need to be configured are:
    * `MEDIA_DRIVER_DIR` pointing to the Aeron Media Driver. This folder will only be created after the application is started. For Linux users it is recommended that the default path (`/dev/shm`) is not deviated from in order to achieve optimal performance. Otherwise the path can be amended as follows:
        ```bash
        MEDIA_DRIVER_DIR=[...]/aeron
        ```
        Note that if the user deviates from the default media driver path, they would have to make the same change in the [build.gradle](build.gradle) file.
    * `SOFTWARE_PATH` pointing to the start-up folder that will be created upon deployment. This path can be set to any valid path on the user's machine and may be given any name. For example:
        ```bash
        SOFTWARE_PATH=[...]/[folder name]
        ```
    * `DATA_PATH` points to the data folder within the software path. For example:
        ```bash
        DATA_PATH=[SOFTWARE_PATH]/data
        ```

4. Before the application can be started, we are required to change a few system settings to ensure that network performance and system memory are utilised correctly. Firstly, the receive and send UDP buffer sizes/limits need to be configured as follows:
    ```bash
    sudo sysctl net.core.rmem_max=2097152
    sudo sysctl net.core.wmem_max=2097152
    ```
    Secondly, this Java application requires large pages to be enabled. By default Linux does not enable any HugePages (portions of memory set aside to be solely used by the application). To determine the current HugePage usage, run `grep Huge /proc/meminfo`. Furthermore, the default HugePage size is 2MB (2048kB). So if the user wishes to enable approximately 20GB of HugePages (dedicated memory), this would require 10000 HugePages - so the command to run would be
    ```bash
    sudo sysctl vm.nr_hugepages=10000
    ```

5. Last is the running of the application. Users deploying the application to Microsoft Azure, CHPC, Wits Server or any other server may choose to do so locally or remotely. Remote deployment will require that the user specify the above paths to correspond with that of the remote server. The instructions below demonstrate both local and remote deployment. For users deploying remotely, one must first ensure that SSH is enabled on the server and that port 22 is open for the transfer of files. Additionally, the username, IP address and password fields in the [deploy_remote.gradle](deploy_remote.gradle) file should match that of the server.
    1. Set the current directory to the location of CoinTossX:
        ```bash
        cd [path to]/CoinTossX
        ```
    2. To build the project, use the provided gradle wrapper to run:
        ```bash
        ./gradlew -Penv=local build -x test # For local deployment
        ./gradlew -Penv=remote build -x test # For remote deployment
        ./gradlew.bat -Penv=windows build -x test # For windows deployment
        ```
    3. To deploy the project to a start-up folder, execute one of the following.
        ```bash
        ./gradlew -Penv=local clean installDist bootWar copyResourcesToInstallDir copyToDeploy deployLocal # For local deployment
        ./gradlew -Penv=remote clean installDist bootWar copyResourcesToInstallDir copyToDeploy deployRemote # For remote deployment
        ./gradlew.bat -Penv=windows clean installDist bootWar copyResourcesToInstallDir copyToDeploy deployLocal # For windows deployment
        ```
    4. Finally, to run the program, execute the shell script:
        ```bash
        sh [SOFTWARE_PATH]/scripts/startAll.sh
        ```
    5. Access the web app by typing `localhost:8080` (if deployed locally) or `[server IP]:8080` (if deployed remotely) into the URL search bar of a browser.

## Usage
To configure the trading sessions to be fired during the simulation refer to the [data/tradingSessionsCron.properties](data/tradingSessionsCron.properties) file. The usage/syntax of the cron expressions within that file are as follows. A [cron expression](https://docs.oracle.com/cd/E12058_01/doc/doc.1014/e12030/cron_expressions.htm) is a string consisting of six or seven fields, separated by white space, that describe individual details of the schedule:
```
[seconds] [minutes] [hours] [day of month] [month] [day of week] [year]
```
A few examples, provided in [data/tradingSessionsCron.properties](data/tradingSessionsCron.properties), are shown below.
```
TRADING_SESSIONS=ContinuousTrading2

#StartOfTrading.name=ContinuousTrading
#StartOfTrading.cron=0 0 7 * * 1-7

#ContinuousTrading.name=ContinuousTrading
#ContinuousTrading.cron=0 0 9 * * 1-7

#IntraDayAuctionCall.name=IntraDayAuctionCall
#IntraDayAuctionCall.cron=0 0 17 * * 1-7

ContinuousTrading2.name=ContinuousTrading
ContinuousTrading2.cron=0 15 17 * * 1-7

#RandomAuction.name=VolatilityAuctionCall
#RandomAuction.cron=0 0/5 * * * 1-7
```
Each component of the system as well as other actions can be started independently via the shell scripts. The list of all the runnable shell scripts can be found in the [scripts](scripts) directory. Each shell script simply executes the Java byte code of a "main" method whose class is specified in the `build.gradle` file of the corresponding module. For example, the `startAll.sh` script starts each component consecutively (equivalent to clicking the "Start" button on the Hawkes simulation web page). Similarly, `stopAll.sh` stops all the components (equivalent to clicking the `Shut Down` button on the Hawkes simulation webpage). Furthermore, any other actions on the website can also be done from the command line. To start the Hawkes simulation for a single client and stock simply run `./startHawkesSimulation.sh 1 1`. This starts the simulation for client 1 and stock 1 by submitting the client ID and stock ID as the first and second argument, respectively.

The list of clients that can be activated can be found in the [data/ClientData.csv](data/ClientData.csv) file. Consider, for example, the first client shown below.
```
CompID,Password,NGInputURL,NGInputStreamId,NGOutputURL,NGOutputStreamId,MDGInputURL,MDGInputStreamId,MDGOutputURL,MDGOutputStreamId,SecurityId
1,test111111,udp://localhost:5000,10,udp://localhost:5001,10,udp://localhost:5002,10,udp://localhost:5003,10,1
```
Each client is assigned input and output URLs for both the Native Gateway and Market Data Gateway. These URLs specify the IP addresses (in this case localhost - the user's local machine) to/from which messages will be sent/received as well as the ports on which these components are listening.

From the website the user can: view/add/edit/delete clients, view the simulation status of clients and stocks, start the Hawkes simulation, edit the Hawkes simulation parameters as well as view a snapshot of the limit order books of each stock. With regards to the output of results, after all clients have submitted an end of trading session message, the orders along with their submission times are written to file and stored in the \lstinline[columns=fixed, language=custom]{deploy/data} directory. At the end of each Hawkes simulation the HdrHistogram latency results are written to a text file in the same directory.

<p float="left">
  <img src="https://user-images.githubusercontent.com/10845401/28061338-9b8ec63e-666d-11e7-9e3c-3e6fc84b6cc1.png" width="150" />
  <img src="https://user-images.githubusercontent.com/10845401/28061471-0abc67c8-666e-11e7-8f74-5f8bc937a817.png" width="145" /> 
  <img src="https://user-images.githubusercontent.com/10845401/28061480-0fa64858-666e-11e7-80c6-88cb93d1b975.png" width="160" />
</p>

## Submitting orders
The subsections below demonstrate the submission of orders to the trading gateway (on a user's local machine) in three programming languages: Java, Julia and Python. Note that currently CoinTossX only allows for Java implementations. Nonetheless, most programming languages include packages/libraries which provide interfaces for calling Java. Although sub-optimal, this temporary solution allows us to bypass the problem of a Java-only implementation. The below programming languages are only a few of those that provide these types of libraries. The code snippets below can be found in the CoinTossX project under the directory [ClientSimulator/src/main/java/example](ClientSimulator/src/main/java/example). More specifically, all necessary static Java methods are defined in the [Utilities](ClientSimulator/src/main/java/example/Utilities.java) class. Clients have the following functionality that will be demonstrated in the code snippets:
* Submit limit, market, stop, and stop limit orders with different time-in-force combinations.
* Cancel limit orders.
* Market data updates.
    * VWAP of buy/sell side of LOB.
    * Current best bid/ask price/quantity.
    * Active trading session.
    * LOB snapshot
    * Static price reference

### Java
As opposed to the other implementations below, the Java implementation is shown in full without reference to the [Utilities](ClientSimulator/src/main/java/example/Utilities.java) class to provide more detail for how the other implementations function.
```Java
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

        //----- Submit orders -----//
        // Arguments for "submitOrder": order ID, volume, price, side, order type, time in force, display quantity, min execution size, stop price
        client.submitOrder("1", 1000, 99, "Buy", "Limit", "Day", 1000, 0, 0); // Buy limit order
        client.submitOrder("2", 1000, 101, "Sell", "Limit", "Day", 1000, 0, 0); // Sell limit order
        client.submitOrder("3", 1000, 0, "Buy", "Market", "Day", 1000, 0, 0); // Buy market order
        client.submitOrder("4", 1000, 0, "Buy", "StopLimit", "Day", 1000, 0, 0); // Stop buy limit order
        client.submitOrder("5", 1000, 0, "Buy", "Stop", "Day", 1000, 0, 0); // Stop buy market order
        client.cancelOrder("1", "Buy", 99); // Cancel limit order

        //----- Market data updates -----//
        client.calcVWAP("Buy"); // VWAP of buy side of LOB
        client.getBid(); // Best bid price
        client.getBidQuantity(); // Best bid volume
        client.getOffer(); // Best ask price
        client.getOfferQuantity(); // Best ask volume
        client.isAuction(); // Current trading session
        client.lobSnapshot(); // Snapshot of the entire LOB
        client.getStaticPriceReference(); // Static price reference at the end of a trading session

        //----- End trading session by logging out client and closing connections -----//
        client.sendEndMessage();
        client.close();

        System.exit(0);
    }
    //------------------------------------------------------------------------------------------------------------------
}
```

### Julia
The Julia-Java interface is provided in the [JavaCall.jl](https://github.com/JuliaInterop/JavaCall.jl) package. This package works by specifying the paths to the compiled Java byte code (.jar files) as well as the path to the required dependencies. Thereafter the JVM is started and the utilities for logging a client in, starting the trading session, submitting an order and logging out are imported. With the execution of each command, the Java stacktrace is printed in the REPL as well.
```Julia
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
JavaCall.addClassPath("[...]/CoinTossX/ClientSimulator/build/install/ClientSimulator/lib/*.jar") # JavaCall.getClassPath()
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
# Arguments for "submitOrder": order ID, volume, price, side, order type, time in force, display quantity, min execution size, stop price
jcall(client, "submitOrder", Nothing, (jlong, jlong, JString, JString, JString, jlong, jlong, jlong), "1", 1000, 99, "Buy", "Limit", "Day", 1000, 0, 0) # Buy limit order
jcall(client, "submitOrder", Nothing, (jlong, jlong, JString, JString, JString, jlong, jlong, jlong), "2", 1000, 101, "Sell", "Limit", "Day", 1000, 0, 0) # Sell limit order
jcall(client, "submitOrder", Nothing, (jlong, jlong, JString, JString, JString, jlong, jlong, jlong), "3", 1000, 0, "Buy", "Market", "Day", 1000, 0, 0) # Buy market order
jcall(client, "submitOrder", Nothing, (jlong, jlong, JString, JString, JString, jlong, jlong, jlong), "4", 1000, 0, "Buy", "StopLimit", "Day", 1000, 0, 0) # Stop buy limit order
jcall(client, "submitOrder", Nothing, (jlong, jlong, JString, JString, JString, jlong, jlong, jlong), "5", 1000, 0, "Buy", "Stop", "Day", 1000, 0, 0) # Stop buy market order
# Arguments for "cancelOrder": order id, side, price
jcall(client, "cancelOrder", Nothing, (jlong, jlong), "1", "Buy", 99) # Cancel limit order
#---------------------------------------------------------------------------------------------------


#----- Market data updates -----#
jcall(client, "calcVWAP", jlong, (JString,), "Buy") # VWAP of buy side of LOB
jcall(client, "getBid", jlong, ()) # Best bid price
jcall(client, "getBidQuantity", jlong, ()) # Best bid volume
jcall(client, "getOffer", jlong, ()) # Best ask price
jcall(client, "getOfferQuantity", jlong, ()) # Best ask volume
jcall(client, "isAuction", jboolean, ()) # Current trading session
jcall(client, "lobSnapshot", JavaObject{Symbol("java.util.ArrayList")}, ()) # Snapshot of the entire LOB
jcall(client, "getStaticPriceReference", jlong, ()) # Static price reference at the end of a trading session
#---------------------------------------------------------------------------------------------------


#----- End session and logout -----#
# End trading session by logging out client and closing connections
jcall(client, "sendEndMessage", Nothing, ()); jcall(client, "close", Nothing, ())
#---------------------------------------------------------------------------------------------------
```

### Python
The Python Java interface is provided in the [JPype](https://github.com/jpype-project/jpype) module. This code snippet follows a similar process as above but with different syntax.
```Python
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
# Initialize/start the JVM and add the path to the ".jar" files containing the required java dependencies
jp.startJVM(jp.getDefaultJVMPath(), "-ea", classpath = "[...]/CoinTossX/ClientSimulator/build/install/ClientSimulator/lib/*.jar") # Start JVM
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
# Arguments for "submitOrder": order ID, volume, price, side, order type, time in force, display quantity, min execution size, stop price
client.submitOrder("1", 1000, 99, "Buy", "Limit", "Day", 1000, 0, 0) # Buy limit order
client.submitOrder("2", 1000, 101, "Sell", "Limit", "Day", 1000, 0, 0) # Sell limit order
client.submitOrder("3", 1000, 0, "Buy", "Market", "Day", 1000, 0, 0) # Buy market order
client.submitOrder("4", 1000, 0, "Buy", "StopLimit", "Day", 1000, 0, 0) # Stop buy limit order
client.submitOrder("5", 1000, 0, "Buy", "Stop", "Day", 1000, 0, 0) # Stop buy market order
# Arguments for "cancelOrder": order id, side, price
client.cancelOrder("1", "Buy", 99) # Cancel limit order
#---------------------------------------------------------------------------------------------------


#----- Market data updates -----#
client.calcVWAP("Buy") # VWAP of buy side of LOB
client.getBid() # Best bid price
client.getBidQuantity() # Best bid volume
client.getOffer() # Best ask price
client.getOfferQuantity() # Best ask volume
client.isAuction() # Current trading session
client.lobSnapshot() # Snapshot of the entire LOB
client.getStaticPriceReference() # Static price reference at the end of a trading session
#---------------------------------------------------------------------------------------------------


#----- End session and logout -----#
# End trading session by logging out client and closing connections
client.sendEndMessage()
client.close()
# Stop JVM
jp.shutdownJVM()
#---------------------------------------------------------------------------------------------------
```

## Required updates
* Deatiled web-interface
* Improve user-friendliness
* Data output detail
* Use the latest JDK