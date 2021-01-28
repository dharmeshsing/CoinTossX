#!/bin/sh

MAIN_DIR=@SOFTWARE_PATH@

#Start All
echo Starting All Engines...
cd $MAIN_DIR/scripts

./startLowLatencyDriver.sh
./startTGDriver.sh
./startMDGDriver.sh
./startWebDriver.sh
./startNativeGateway.sh
./startMatchingEngine.sh
./startMarketDataGateway.sh
./startWebEventListener.sh
./startWeb.sh
