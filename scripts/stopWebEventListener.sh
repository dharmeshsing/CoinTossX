#!/usr/bin/env bash

ps -ef | grep WebEventListener | grep -v grep | awk '{print $2}' | xargs kill -9