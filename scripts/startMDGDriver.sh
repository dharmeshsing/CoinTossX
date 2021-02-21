#!/usr/bin/env bash

MAIN_DIR=@SOFTWARE_PATH@

#Start Socket
echo Starting Market Data Gateway Socket...
cd $MAIN_DIR/Socket/bin
./Socket @MEDIA_DRIVER_DIR@/marketDataGateway > $MAIN_DIR/MDGSocket.log 2>&1 &

sleep 10s