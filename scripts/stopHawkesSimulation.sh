#!/usr/bin/env bash

ps -ef | grep HawkesSimulation | grep -v grep | awk '{print $2}' | xargs kill