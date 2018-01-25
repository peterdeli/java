#!/usr/bin/expect -f
# almost entirely rewritten by alec.muffett@uk.sun.com
# version 3.1 - Tue Nov 28 21:13:29 GMT 2000

##################################################################

# callcard
set callcard    ""

# watchmoor uk
#set dialup     01276671304

# farnborough uk
#set dialup     01252375886

# linlithgow uk
#set dialup     01506670833

# minley uk
#set dialup      01252662257

# Brisbane Aus
set dialup      32216860

set dialup 3034662756

##################################################################

# these are the parameters for the program

set user     	pd33162 

set password  NOT-USED-BY-MOST-SWAN-SYSTEMS
set modem     /dev/ttyS0
#set modem     /dev/term/b
set timeout   60
set pppd      "/usr/sbin/pppd"
set pppopt    "debug"

##################################################################

# this option is autodetected/used on older Annex dialin banks; it
# *is* possible that it may be un-necessary in some of these cases,
# but it fixes the "hanging sessions when heavy i/o occurs" problem.
# this option is *not* used by default on newer dialins.

set pppkludge "novj noccp"

##################################################################
##################################################################
##################################################################

# ---- ORIGINAL CREDITS ----
# This script was written by Jim Isaacson <jcisaac@crl.com>.  It is
# designed to work as a script to use the SecureCARD(tm) device.  This
# little device is mated with a central controller. The number
# displayed on this card changes every so often and you need to enter
# the number along with your user account name in order to gain
# access.  Since chat is based upon fixed strings this procedure will
# not work with chat.  It is included by permission. An excellent
# reference for the expect program used by this script is in the book:
# "Exploring Expect" by Don Libes Published by O'Reilly and Associates

#debug ON/OFF
#exp_internal 1

# load ppp devices
#send_user "doing: modprobe ppp\n"
#system "modprobe ppp"

# "extb" = 38400; "spd_hi" converts 38400 into 57600
#system "setserial $modem spd_vhi"
#system "stty -F $modem -echoe -echo raw 115200"
system "stty -F $modem -echoe -echo raw 57600"

# print it
#system "stty -F $modem -a"
#system "setserial -a -G $modem"

# open it
spawn -noecho -open [ open $modem "RDWR NONBLOCK" ]

# wakey wakey
exp_send "AT\r"
expect "OK"

# reset string
exp_send "AT &F1 S11=65\r"
expect "OK"

# dial
exp_send "ATDT $callcard$dialup\r"

set counter 0
set timeout 30
set challenge_issued 0
set modem_not_connected 1

expect {
    -re ".*CONNECT.*\n" {
	set timeout 15
	set modem_not_connected 0
	exp_continue
    }
    -re ".*CONNECT.*\r" {
	set timeout 15
	set modem_not_connected 0
	exp_continue
    }
    -re ".*NO.*CARRIER" {
	exp_send_user "\nfailed to connect.\n"
	exit
    }
    -re ".*NO.*DIAL.*TONE" {
	exp_send_user "\nfailed to connect.\n"
	exit
    }
    -re ".*VOICE" {
	exp_send_user "\nfailed to connect.\n"
	exit
    }
    -re ".*BUSY" {
	exp_send_user "\nfailed to connect.\n"
	exit
    }
    -re ".*ID:" {
	exp_send "$user\r"
	set challenge_issued 1
	exp_continue
    }
    -re ".*sername:" {
	exp_send "$user\r"
	set challenge_issued 1
	exp_continue
    }
    -re "password: " {
	exp_send "\r"
	exp_continue
    }
    -re "\[0-9] \[0-9]\[0-9]\[0-9]\[0-9].*" {
	if { $challenge_issued > 0 } {
	    set timeout -1
	    exp_send_user "\n:::: ENTER RESPONSE FOR THE CHALLENGE ::::\n"
	    expect_user -re "(.*)\n"
	    exp_send "$expect_out(1,string)\r"
	    exp_send "\r\rppp\r"
	    set timeout 30 
	}
	exp_continue
    }
    -re "PPP" {
	exp_send_user "\ninvoking pppd, options: $pppopt\n"
    }
    -re ".*!\}!\}!\}.*" {
	set pppopt "$pppopt $pppkludge"
	exp_send_user "\ninvoking pppd, options: $pppopt\n"
    }
    timeout {
	if { $modem_not_connected > 0 } {
	    exp_continue
	}
	set timeout 15
	exp_send "\r"
	incr counter
	if { $counter > 8 } {
	    exp_send_user "\ncannot connect. exiting.\n"
	    exit
	} else {
	    exp_continue
	}
    }
}

puts "exp_send $pppd crtscts lock defaultroute noipdefault [split $pppopt] updetach"
exp_send "$pppd crtscts lock defaultroute noipdefault [split $pppopt] updetach\r"

# ##################################################################
# # go
# puts "eval overlay -0 $spawn_id -1 $spawn_id $pppd crtscts lock defaultroute noipdefault [split $pppopt] updetach"
# eval overlay -0 $spawn_id -1 $spawn_id $pppd crtscts lock defaultroute noipdefault [split $pppopt] updetach
# 
# ##################################################################
