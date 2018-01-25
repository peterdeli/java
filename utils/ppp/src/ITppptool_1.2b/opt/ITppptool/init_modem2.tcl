#!/usr/bin/expectk

proc init_modem { init_string modem_port baud_rate } {
	set error [catch { [system "stty -F $modem_port -echoe -echo raw $baud_rate"] } err]
	if { $error != 1 } {
		set return -1
		return $return
	}
	set timeout 3
	if {	[spawn -noecho -open [ open $modem_port "RDWR NONBLOCK"]] != 0 } {
		set return -1
		return $return
	}   else {
		sleep 1
		puts "exp_send ATZ"
		exp_send "ATZ\r"
		expect "OK"
		sleep 1
		puts "exp_send atdt3034662756"
		exp_send "atdt3034662756\r"
		expect_background -i $spawn_id {
			sleep 2
			exp_continue
		}
	}
	return 0 
 }

set err [init_modem [lindex $argv 0] [lindex $argv 1] [lindex $argv 2]]
while { 1 } {
	sleep 5
}
#if { $err == 0 } { puts "OK" }
#exit $err
