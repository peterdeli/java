/* Copy /opt/ITppptool/options to /etc/ppp/options */
#include <stdio.h>
#include <strings.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>


int main( int argc, char *argv[] ) {

  FILE        *from;
  FILE        *to;
	FILE	*resolv;
	char	*RESOLV;
	int c;
	char *FROM;
	char *TO; 

	if ( argc != 1 ){
		fprintf( stderr, "Usage: %s <from> <to>\n", argv[0] );
		exit( 1 );
	}

	RESOLV = "/etc/resolv.conf";

/* Try to delete the existing file - just in case it is a symlink */
    unlink( TO );

    if ( ( resolv = fopen( RESOLV, "w" )) == NULL ) {
			fprintf( stderr, "Open of %s failed", RESOLV );
			exit(2);
		}

    if ( setgid( getgid()))
    {
        fprintf( stderr, "Unable to setgid\n");
        exit( 2);
    }
    if ( setuid( getuid()))
    {
        fprintf( stderr, "Unable to setuid\n");
        exit( 2);
    }

fprintf(stdout, "Copying %s to %s\n", "/opt/ITppptool/options", "/etc/ppp/options"); 
	while ( ( c = fgetc( from )) != EOF ){

		fputc( c, to );
	}
	fclose ( to );
	fclose ( from );

    exit( 0);
}
