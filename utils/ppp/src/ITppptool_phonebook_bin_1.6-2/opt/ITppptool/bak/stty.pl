#!/usr/bin/perl

$port=$ARGV[0];
speed=$ARGV[1];

print "stty -F $port -echoe -echo raw $speed\n";
system ( stty -F $port -echoe -echo raw $speed );
