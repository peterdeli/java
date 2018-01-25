#!/usr/bin/expect

proc init_modem { init_string modem_port baud_rate } {

	log_user 0
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
		set port_id $spawn_id
		exp_send -i $port_id "ATZ\r"
		expect {
			-i $port_id "OK" {
				return 0
			}
			-i $port_id "NO CARRIER" {
				return 0 
			}
			-i $port_id timeout {
				return -1
			}
		}
	}
	log_user 1
	return -1 
 }

set err [init_modem [lindex $argv 0] [lindex $argv 1] [lindex $argv 2]]
if { $err == 0 } { puts "OK" }
exit $err
