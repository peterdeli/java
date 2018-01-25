#!/bin/sh

pppd_file=$1

dirname=`dirname $pppd_file`

if [ $dirname != "/var/run" ]; then
	echo "Cannot send a kill signal to file which isn't in /var/run"
	exit
fi

pid=`cat $pppd_file`
echo "sending $pid TERM signal"

kill -TERM $pid
