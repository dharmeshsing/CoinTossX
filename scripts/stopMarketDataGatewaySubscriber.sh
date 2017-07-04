#!/usr/bin/env bash

ps -ef | grep MktDataSubscriber | grep -v grep | awk '{print $2}' | xargs kill