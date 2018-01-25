#include <stdio.h>
#include <strings.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>


int main( int argc, char *argv[] ) {

    FILE        *from;
    FILE        *to;
	int c;
	char *FROM;
	char *TO; 
	printf ( "argc = %d\n", argc );
	if ( argc != 2 ){
		fprintf( stderr, "Usage: %s %s\n", argv[0], argv[1] );
		exit( 1 );
	}

	FROM = argv[1]; 
	TO = "/etc/resolv.conf";

/* Try to delete the existing file - just in case it is a symlink */
    unlink( TO );

    if ( ( from = fopen( FROM, "r" )) == NULL ) {
			fprintf( stderr, "Open of %s failed\n", FROM );
			exit(2);
		}
    if ( ( to = fopen( TO, "w" )) == NULL ) {
			fprintf( stderr, "Open of %s failed\n", TO );
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

fprintf(stdout, "Copying %s to %s\n", FROM, TO); 
	while ( ( c = fgetc( from )) != EOF ){

		fputc( c, to );
	}
	fclose ( to );
	fclose ( from );

    exit( 0);
}
