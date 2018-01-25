h27296
s 00015/00000/00000
d D 1.1 03/03/04 21:22:30 pdel 1 0
c date and time created 03/03/04 21:22:30 by pdel
e
u
U
f e 0
t
T
I 1
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
E 1
