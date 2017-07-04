#!/usr/bin/env bash

ps -ef | grep MatchingEngine | grep -v grep | awk '{print $2}' | xargs kill