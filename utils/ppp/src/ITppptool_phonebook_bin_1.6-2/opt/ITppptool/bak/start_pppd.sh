#!/bin/sh

if [ $1 = "/usr/bin/pppd" ]; then
	/usr/bin/pppd file $2
fi
