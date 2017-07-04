#!/usr/bin/env bash

ps -ef | grep LowLatencyMediaDriver | grep -v grep | awk '{print $2}' | xargs kill