#!/usr/bin/perl

sub usage{

	print "Usage: $0 ",
		"-backup_file <file> [ -create -copy -restore] -srcfile <srcfile>", "\n";
	exit 0;

}

sub copyfile{

	print "Backup Name for $RESOLV_FILE: $BACKUP\n",
				"Source File to copy to $RESOLV_FILE: $SRC_FILE\n";

	if ( ! -e $SRC_FILE ) {
		print "Source file $SRC_FILE not found\n";
		return 1;
	} else {
		if ( -e $RESOLV_FILE ) {
			# backup any existing backup files
			while ( -e $BACKUP ){
				system( "/bin/mv $BACKUP ${BACKUP}.$$" );
			}
			# backup resolv.conf
			system ( "/bin/mv $RESOLV_FILE $BACKUP" );
			# copy
			system( "/bin/cp $SRC_FILE $RESOLV_FILE" );
		} else {
			print "Destination file $RESOLV_FILE not found";
			return 1;
		}
	}
	return 0;
}

sub restore{

		system ( "/bin/mv $BACKUP $RESOLV_FILE" );
		if ( ! -e $RESOLV_FILE )  {
			print "Error moving $BACKUP file to $RESOLV_FILE\n";
			return 1;
		}
		return 0;
}

sub init{

	$RESOLV_FILE = "/etc/resolv.conf";
	$ENV{PATH}= undef; 

	use File::Copy;
	use Getopt::Long;

	GetOptions(
		"copy"  => \$COPY_FLAG,
		"restore"  => \$RESTORE_FLAG,
		"srcfile=s"  => \$SRC_FILE,
		"backup_file=s"  => \$BACKUP
	);

	print "$EUID, $EGID = $UID, $GID\n";
	($EUID, $EGID) = ($UID, $GID);

	if ( $COPY_FLAG == 1 ) {

		if ( ! defined $SRC_FILE ) {
			print "Source file option -srcfile is required with copy option\n"; 
			exit 1;
		} elsif ( $SRC_FILE =~ /\s+/ ){
			print "Source file name cannot contain spaces\n";
			exit 1;
		} 
		$err = copyfile();
	} elsif ( $RESTORE_FLAG == 1 ){

		if ( ! defined $BACKUP ) {
			print "Backup file option -backup is required with restore option\n"; 
			exit 1;
		} elsif ( $BACKUP =~ /\s+/ ){
			print "Backup file name cannot contain spaces\n";
			exit 1;
		}
		$err = restore();

	}

	return $err;
}

sub main{
	return ( &init );
}

&main;
