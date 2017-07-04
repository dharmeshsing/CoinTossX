#!/usr/bin/env bash

MAIN_DIR=@SOFTWARE_PATH@

#Start Matching Engine
echo Starting Matching Engine...
cd $MAIN_DIR/MatchingEngine/bin
./MatchingEngine > $MAIN_DIR/MatchingEngine.log 2>&1 &