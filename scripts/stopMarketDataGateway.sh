#!/usr/bin/env bash

ps -ef | grep NativeMarketDataGateway | grep -v grep | awk '{print $2}' | xargs kill