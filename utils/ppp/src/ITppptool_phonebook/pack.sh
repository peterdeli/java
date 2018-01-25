#!/bin/sh


if [ $# -ne 1 ]; then
	echo "Usage: $0 <directory to tar and gzip>"
	exit
fi

item=$1

if [ ! -d "$item" ]; then
	echo "Argument must be directory"
	exit
fi

item_dir=`dirname $item`
cd $item_dir

tar_item=`basename "${item}.tar"`
gzip_item=`basename "${tar_item}.gz"`

if [ -f $gzip_item ]; then
	echo "Press return to delete $gzip_item"
	read this
	rm -rf $gzip_item
fi

echo "tar cvf $tar_item `basename $item`" 
tar cvf $tar_item `basename $item` 

echo "gzip $tar_item"
gzip $tar_item
