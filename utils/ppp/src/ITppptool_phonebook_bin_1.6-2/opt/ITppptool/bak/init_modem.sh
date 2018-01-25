#!/bin/sh

if [ $1 = "stty" ]; then
	cmd=$1
	shift
	args $@
	eval $cmd $args
fi
