#!/bin/sh

port=$1
speed=$2

echo "stty -F $port -echoe -echo raw $speed"
stty -F $port -echoe -echo raw $speed
