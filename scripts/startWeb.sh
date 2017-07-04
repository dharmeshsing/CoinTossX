#!/usr/bin/env bash

MAIN_DIR=@SOFTWARE_PATH@

#Start Matching Engine
#perf stat -e L1-dcache-loads,L1-dcache-load-misses,cycles,instructions,cache-references,cache-misses,bus-cycles,dTLB-loads,dTLB-load-misses,context-switches
echo Starting Web...
cd $MAIN_DIR/Web
java -jar -Xmx3G -Xms3G -Dagrona.disable.bounds.checks=true -XX:+UseG1GC -XX:+UseLargePages -XX:+OptimizeStringConcat -d64 -server -XX:+UseStringDeduplication -XX:+UseCondCardMark -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=300000 Web-1.0.jar > $MAIN_DIR/Web.log 2>&1 &