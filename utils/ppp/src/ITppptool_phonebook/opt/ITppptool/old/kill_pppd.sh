#!/bin/sh

pppd_file=$1

if [ $# -ne 1 ]; then
	echo "$0 called without an argument"
	exit 1
fi

if [ -f "$pppd_file" ]; then

	dirname=`dirname $pppd_file`

	if [ $dirname != "/var/run" ]; then
		echo "Cannot send a kill signal to file which isn't in /var/run"
		exit
	fi

	postfix=`basename $pppd_file | cut -d'.' -f2`

	if [ -z $postfix -o $postfix != "pid" ]; then 
		echo "Cannot send a kill signal to file which doesn't end in 'pid'" 
		exit
	fi


	# if we get this far, make sure the file prefix is an interface

	prefix=`basename $pppd_file | cut -d'.' -f1`
	valid_if=false
	for if in `ifconfig -a |grep ':' | cut -d':' -f1`; do

		if [ $prefix = $if ]; then
			echo "Found valid interface $if to match pid file $pppd_file"
			valid_if="true"
			break
		fi

	done

	if [ $valid_if = "false" ]; then
		echo "Cannot send a kill signal to file which doesn't match an interface name"
		exit
	fi
		

	pid=`cat $pppd_file`
	echo "sending $pid TERM signal"
	kill -TERM $pid
fi

