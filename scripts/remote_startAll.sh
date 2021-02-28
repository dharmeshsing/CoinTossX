#!/bin/sh

export JAVA_HOME=/usr/lib/jvm/jdk1.8.0_271
MAIN_DIR=@SOFTWARE_PATH@

#Start Socket
echo Starting Socket...
cd $MAIN_DIR/Socket/build/install/Socket/bin
./Socket > $MAIN_DIR/Socket.log 2>&1 &

sleep 10s

#Start Native Gateway
echo Starting Native Gateway...
cd $MAIN_DIR/NativeGateway/build/install/NativeGateway/bin
./NativeGateway > $MAIN_DIR/NativeGateway.log 2>&1 &

#Start Matching Engine
echo Starting Matching Engine...
cd $MAIN_DIR/MatchingEngine/build/install/MatchingEngine/bin
./MatchingEngine > $MAIN_DIR/MatchingEngine.log 2>&1 &

#Start Market Data Gateway
echo Starting Market Data Gateway...
cd $MAIN_DIR/MarketDataGateway/build/install/MarketDataGateway/bin
./MarketDataGateway > $MAIN_DIR/MarketDataGateway.log 2>&1 &

#Start Client Simulator - Trading Gateway Subscriber
echo Starting OneSubscriber...
cd $MAIN_DIR/ClientSimulator/build/install/ClientSimulator/bin
./OneSubscriber > $MAIN_DIR/OneSubscriber.log 2>&1 &

#Start Client Simulator - Market Data Gateway Subscriber
echo Starting Market Data Subscriber...
cd $MAIN_DIR/ClientSimulator/build/install/ClientSimulator/bin
./MktDataSubscriber > $MAIN_DIR/MktDataSubscriber.log 2>&1 &

#Start Client Simulator - Publisher
echo Starting One Publisher...
cd $MAIN_DIR/ClientSimulator/build/install/ClientSimulator/bin
./OnePublisher > $MAIN_DIR/OnePublisher.log 2>&1 &
