#!/bin/sh

 #\
 PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin:/opt/local/bin:/opt/sfw/bin:/usr/ucb:/opt/local/exe:/opt/local/pkgs/tk:/opt/ITTOtcltk/bin:/usr/openwin/bin:/usr/X11R6/bin
 #\
 export PATH
 #\
 umask 022
 
 #\
 if [ -z "$DISPLAY" ]; then
 #\
 	echo "`date`: ERROR: DISPLAY environment not set"
 #\
 	exit 1
 #\
 fi
 
 
 #\
 OS=`uname -s`
 
 #\
 umask 022
 #\
 if [ -z "$USER" ] ;then
 		#\
 		echo "USER -> $USER"
 		#\
 			echo "no"
 		#\
 			exit
 		#\
 		if [ $OS = "SunOS" ]; then
 		#\
 			if [ -f "/usr/ucb/whoami" ]; then
 		#\
 				USER_ID=`/usr/ucb/whoami`
 		#\
 			else
 		#\
 				USER_ID=`/bin/who am i | awk '{print $1}'`
 		#\
 			fi
 		#\
 		elif [ $OS = "Linux" ]; then
 		#\
 			USER_ID=`whoami`
 		#\
 		fi
 	#\
 	export USER_ID
 #\
 else
 	#\
 	USER_ID=$USER
 	#\
 	export USER_ID
 #\
 fi
 
 #\
 echo "USER_ID: $USER_ID"
 
 #\
 CONNECTION_TYPE="ppp"
 #\
 export CONNECTION_TYPE
 
 #\
 PPP_HOME=$HOME/.ppptool
 #\
 export PPP_HOME
 
 #\
 if [ ! -d $PPP_HOME ]; then
 #\
   mkdir $PPP_HOME >> /tmp/ppp.err.$$ 2>&1 
 #\
 	chown $USER_ID $PPP_HOME
 #\
   if [ $? -ne 0 ]; then
 #\
     echo "`date`: Error creating $PPP_HOME" 
 #\
     exit
 #\
   fi
 #\
 else
 #\
   chmod 755 $PPP_HOME # just in case
 #\
 fi
 
 

# exec link target
#\
if [ -h $0 ]; then
#\
	cd `dirname $0`
#\
	basename=`basename $0`
#\
	link_target=`ls -l $basename | awk '{print $NF}'`
#\
	target=`basename $link_target`
#\
	cd `dirname $link_target`
#\
	echo "pppdir = `pwd`"
#\
	PKGHOME=`pwd`
#\
	export PKGHOME
#\
	echo "exec expectk $target  $@" 
#\
	eval exec expectk $target  "$@"  > /dev/null 2>&1
	#exec expectk $target  "$@" 
#\
else
#\
	cd `dirname $0`
#\
	echo "pppdir = `pwd`"
#\
	PKGHOME=`pwd`
#\
	export PKGHOME
#\
	target=`basename $0`
#\
	echo "exec expectk $target  $@" 
#\
	eval exec expectk $target  "$@" > /dev/null 2>&1 
	#exec expectk $target  "$@"
#\
fi
