/* backup /etc/resolv.conf */ 
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

	if ( argc != 2 ){
		fprintf( stderr, "Usage: %s %s\n", argv[0], "resolv.conf.<ppp account>" );
		exit( 1 );
	}

	FROM = "/etc/resolv.conf";
	char resolv_name[100];
	strcpy( resolv_name, "/etc/resolv.conf." ); 
	TO = strcat( resolv_name, argv[1] );

	printf ( "From = %s, To = %s\n", FROM, TO );


/* Try to delete the existing file - just in case it is a symlink 
    unlink( TO );
*/

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

fprintf(stdout, "Copying %s to %s\n", "/etc/resolv.conf", TO ); 
	while ( ( c = fgetc( from )) != EOF ){

		fputc( c, to );
	}
	fclose ( to );
	fclose ( from );

    exit( 0);
}
