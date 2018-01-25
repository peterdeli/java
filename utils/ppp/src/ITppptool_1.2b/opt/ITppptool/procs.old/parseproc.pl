#!/usr/bin/perl

open F, $ARGV[0];
my $postfix = $ARGV[1];

@contents = <F>;
close F;

my $proc, $name, @rest; 
my $start = "true";
my $proc_open = "false";
foreach $line ( @contents ) {

	if ( $line =~ /^proc/ || $line =~ /^\s+proc/ ) {
		# close old
		# start new
		if ( defined $name ){
			close PROC; 
			print "Closing $name.$postfix\n";
			$proc_open = "false";
		}
		# get name
		( $proc, $name, @rest ) = split /\s+/, $line;
		print "Opening $name.$postfix\n";
		open PROC, ">$name.$postfix";
		$proc_open = "true";
		print PROC $line;
	} else {
		print PROC $line if $proc_open ne "false"; 
	}

}
