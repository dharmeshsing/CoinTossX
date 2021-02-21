#!/usr/bin/env bash

MAIN_DIR=@SOFTWARE_PATH@

#Start Socket
echo Starting WebSocket...
cd $MAIN_DIR/Socket/bin
./Socket @MEDIA_DRIVER_DIR@/web > $MAIN_DIR/WebSocket.log 2>&1 &

sleep 10s