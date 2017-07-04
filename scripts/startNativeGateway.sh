#!/usr/bin/env bash

MAIN_DIR=@SOFTWARE_PATH@

#Start Native Gateway
echo Starting Native Gateway...
cd $MAIN_DIR/NativeGateway/bin
./NativeGateway > $MAIN_DIR/NativeGateway.log 2>&1 &