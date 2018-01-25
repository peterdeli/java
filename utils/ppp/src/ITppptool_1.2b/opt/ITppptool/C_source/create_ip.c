/* Create /etc/ppp/ip-up or /etc/ppp/ip-down */ 
#include <stdio.h>
#include <strings.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>


int main( int argc, char *argv[] ) {

  FILE        *to;
	char *TO; 
	int c;

	if ( argc != 1 ){
		fprintf( stderr, "Usage: %s <from> <to>\n", argv[0] );
		exit( 1 );
	}

	FROM = "/opt/ITppptool/options";
	TO = "/etc/ppp/options";

/* Try to delete the existing file - just in case it is a symlink */
    unlink( TO );

    if ( ( from = fopen( FROM, "r" )) == NULL ) {
			fprintf( stderr, "Open of %s failed", FROM );
			exit(2);
		}
    if ( ( to = fopen( TO, "w" )) == NULL ) {
			fprintf( stderr, "Open of %s failed", TO );
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
