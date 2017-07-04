#!/usr/bin/env bash

ps -ef | grep NativeGateway | grep -v grep | awk '{print $2}' | xargs kill