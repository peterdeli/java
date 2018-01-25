#!/bin/sh

usage(){
	cat <<EOF	
--------------------------------------------------

    Usage: $0 [options], where options are:


    -d domain name 

		-ns1 nameserver 1
		-ns2 nameserver 2

--------------------------------------------------
EOF
}

if [ $# -ne 6 ]; then
	echo ""
	echo "Error: Incorrect number of arguments" 
	echo ""
	usage
	exit 2
fi

args=$*

opt_error="FALSE"

# domain name
# ns 1
# ns 2
# ( search )
#domain earthlink.net
#search earthlink.net central.sun.com sun.com
#nameserver 24.221.208.5
#nameserver 24.221.192.5

while getopts d:ns1:ns2 opt
do
		#echo "args = $*"	
		case $opt in
			d)	domain_name=$OPTARG; echo "domain = $domain_name"
					;;
			ns1)	ns1=$OPTARG; echo "nameserver 1 = $ns1"
					;;
			ns2)	ns2=$OPTARG; echo "nameserver 2 = $ns2"
					;;
			*)					echo "Bad argument $opt"; usage
								  opt_error="TRUE"	
									;;
		esac
done

shift `expr $OPTIND - 1`
if [ $opt_error = "TRUE" ]; then
	usage
	exit 2
fi

cat <<EOF
	domain name = $domain_name
	ns1 = $ns1
	ns2 = $ns2
EOF


