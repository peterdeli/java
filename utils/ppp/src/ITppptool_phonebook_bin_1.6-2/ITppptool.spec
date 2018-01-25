Summary: SunIT Technology Office VPN Tool
Name: ITppptool
Version: 1.4
Release: 19
Copyright: Commercial
Group: Applications/Internet
Source: ITppptool.tar.gz
BuildRoot: %{_builddir}/%{name}-root
BuildArchitectures: i586
URL: http://crit.central/pdts/software-library/
Packager: Peter.Delevoryas@Sun.COM
Requires: expect >= 5.3, glibc 
AutoReq: no

%description
ppptool is a graphical tool for connecting to AGNS accounts and Sun Modempools 

%install
mkdir ${RPM_BUILD_ROOT}
cd ${RPM_BUILD_ROOT}
gzip -cd ${RPM_SOURCE_DIR}/ITppptool.tar.gz | tar xvmpf -

%files
%defattr(-,root,root)
%dir /opt/iWork
%dir /opt/iWork/Gnome
%dir /opt/iWork/Gnome/applications
%dir /opt/local
%dir /opt/local/bin
%dir /opt/ITppptool
%dir /opt/ITppptool/procs
%attr(4755,root,root) /opt/ITppptool/ppptool
/etc/ppp/ip-up.local
/opt/local/bin/ppptool
/opt/ITppptool/ppptool.tk
/opt/ITppptool/copy_pppopts
/opt/ITppptool/kill_pppd
/opt/ITppptool/manage_resolv
/opt/ITppptool/start_pppd
/opt/iWork/Gnome/applications/ITppptool.desktop
/opt/ITppptool/procs/add_account_menu
/opt/ITppptool/procs/add_accounts
/opt/ITppptool/procs/backup_resolv
/opt/ITppptool/procs/blink_single
/opt/ITppptool/procs/build_log_win
/opt/ITppptool/procs/build_menus
/opt/ITppptool/procs/choose_account
/opt/ITppptool/procs/cleanup_resolv
/opt/ITppptool/procs/close_pppd
/opt/ITppptool/procs/connect
/opt/ITppptool/procs/create_account
/opt/ITppptool/procs/create_des_dialog
/opt/ITppptool/procs/create_ip_down
/opt/ITppptool/procs/create_ip_up
/opt/ITppptool/procs/create_resolv
/opt/ITppptool/procs/delete_account
/opt/ITppptool/procs/delete_account_menu
/opt/ITppptool/procs/des_countdown
/opt/ITppptool/procs/disconnect
/opt/ITppptool/procs/down_interface
/opt/ITppptool/procs/edit_account
/opt/ITppptool/procs/edit_prefs
/opt/ITppptool/procs/get_helptext
/opt/ITppptool/procs/help
/opt/ITppptool/procs/init
/opt/ITppptool/procs/init_blinking_bulbs
/opt/ITppptool/procs/init_modem_Linux
/opt/ITppptool/procs/init_modem_SunOS
/opt/ITppptool/procs/isp_connect_Linux
/opt/ITppptool/procs/isp_connect_SunOS
/opt/ITppptool/procs/load_accts
/opt/ITppptool/procs/load_current_accts
/opt/ITppptool/procs/load_global
/opt/ITppptool/procs/log_message
/opt/ITppptool/procs/log_messages
/opt/ITppptool/procs/manage_accounts
/opt/ITppptool/procs/modempool_connect_Linux
/opt/ITppptool/procs/modempool_connect_SunOS
/opt/ITppptool/procs/monitor_link_Linux
/opt/ITppptool/procs/monitor_link_SunOS
/opt/ITppptool/procs/post_connect
/opt/ITppptool/procs/post_disconnect
/opt/ITppptool/procs/pre_connect
/opt/ITppptool/procs/pre_disconnect
/opt/ITppptool/procs/Random
/opt/ITppptool/procs/RandomInit
/opt/ITppptool/procs/RandomRange
/opt/ITppptool/procs/read_ip_up_Linux
/opt/ITppptool/procs/read_ip_up_SunOS
/opt/ITppptool/procs/restore_resolv
/opt/ITppptool/procs/save_accounts
/opt/ITppptool/procs/save_prefs
/opt/ITppptool/procs/set_account
/opt/ITppptool/procs/set_active
/opt/ITppptool/procs/set_all_bulbcolors
/opt/ITppptool/procs/set_blinking_off
/opt/ITppptool/procs/set_bulb_color
/opt/ITppptool/procs/set_menu
/opt/ITppptool/procs/view_log
/opt/ITppptool/procs/write_ppp_resolv
/opt/ITppptool/procs/write_resolv_file

%clean
rm -rf $RPM_BUILD_ROOT

%pre
ROOTDIR=${RPM_INSTALL_PREFIX:-}
	
%post
ROOTDIR=${RPM_INSTALL_PREFIX:-}
if test -f ${ROOTDIR}/opt/iWork/bin/it_gintegrate 
then
	if [ "XX$ROOTDIR" = "XX" ]; then
		/opt/iWork/bin/it_gintegrate -i -R / -d ITppptool.desktop
	else 
		${ROOTDIR}/opt/iWork/bin/it_gintegrate -i -R $ROOTDIR -d ITppptool.desktop
	fi
fi

%postun
ROOTDIR=${RPM_INSTALL_PREFIX:-}
if [ -f ${ROOTDIR}/opt/iWork/bin/it_gintegrate ]; then
	if [ "XX$ROOTDIR" = "XX" ]; then
		/opt/iWork/bin/it_gintegrate -r -R / -d ITppptool.desktop
	else 
		${ROOTDIR}/opt/iWork/bin/it_gintegrate -r -R $ROOTDIR -d ITppptool.desktop
	fi
fi
