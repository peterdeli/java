if [file isfile ~/.vpntool/vpn_config] {
   set config_file_to_load ~/.vpntool/vpn_config
} else {
   set config_file_to_load $appdir/vpn_config
}

global pre_connect_script 
global post_connect_script 

global pre_disconnect_script 
global post_disconnect_script

if [file exists /etc/connect/pre-connect] {
	set pre_connect_script /etc/connect/pre-connect
} else {
	set pre_connect_script "" 
}
if [file exists /etc/connect/post-connect] {
	set post_connect_script /etc/connect/post-connect
} else {
	set post_connect_script "" 
}
if [file exists /etc/connect/pre-disconnect] {
	set pre_disconnect_script /etc/connect/pre-disconnect
} else {
	set pre_disconnect_script "" 
}
if [file exists /etc/connect/post-disconnect] {
	set post_disconnect_script /etc/connect/post-disconnect
} else {
	set post_disconnect_script "" 
}
