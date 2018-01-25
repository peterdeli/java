h21422
s 00091/00000/00000
d D 1.1 03/03/06 00:57:08 pdel 1 0
c date and time created 03/03/06 00:57:08 by pdel
e
u
U
f e 0
t
T
I 1
#include <stdio.h>
#include <strings.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#define resolv_conf_file "/etc/resolv.conf"

/* if option == copy, arg 2 should be srcfile */
/* if option == restore, arg2 should be backupfile */
/* if option == backup, arg2 should be backupfile */

int main( int argc, char *argv[] ) {

  FILE        *from;
  FILE        *to;
	int c,i;
	char *FROM;
	char *TO; 
	char *type;

	if ( argc != 3 ){
		fprintf( stderr, "Usage: %s [copy/restore/backup] [src/destfile]\n", argv[0] );
		exit( 1 );
	}

	for ( i=0; argv[i] != NULL; i++ ){

		if ( i == 1 ) {
			printf ( "Option: %s\n", argv[i] );
			type = argv[i];
		}

		if ( i==2 ){
			if ( (strcmp( type, "copy" ) == 0) ){ 
				FROM = argv[i];
				TO = resolv_conf_file;
				printf( "Type = %s, %s = %s\n", type, "source file", FROM );
			} else if ( (strcmp( type, "restore" ) == 0) ){
				FROM = argv[i];
				TO = resolv_conf_file;
				printf( "Type = %s, %s = %s\n", type, "source file", FROM );
			} else if ( (strcmp( type, "backup" ) == 0) ){
				FROM = resolv_conf_file; 
				TO = argv[i];
				printf( "Type = %s, %s = %s\n", type, "destination file", TO );
			}
		}

	}


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

	fprintf(stdout, "Copying %s to %s complete\n", FROM, TO );
	fclose ( to );
	fclose ( from );

	/* delete backup files or restore files */
	if ( strcmp ( type, "restore" ) == 0 ) {
		fprintf(stdout, "Removing backup file %s\n", FROM );
		unlink( FROM );
	}

  exit( 0);
}
E 1
