#!/usr/bin/env bash

MAIN_DIR=@SOFTWARE_PATH@

#Start Socket
echo Starting Socket...
cd $MAIN_DIR/Socket/bin
./Socket @MEDIA_DRIVER_DIR@ > $MAIN_DIR/Socket.log 2>&1 &

sleep 10s