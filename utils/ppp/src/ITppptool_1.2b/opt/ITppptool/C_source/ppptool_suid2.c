#include <stdio.h>
#include <strings.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

#define PPPTOOL "ppptool.tk"

int main( int argc, char **argv, char **environ) {

		char USER_ID[50]; 
    if (setreuid(0,0) != 0) {
       	fprintf(stderr, "setreuid failed. Exiting....\n");
				exit(2);
    }
		fprintf( stdout, "Command: %s\n", PPPTOOL );
		fprintf( stdout, "User: %s\n", getenv("USER"));
		int err = 0;
		if ( argc ==2 ){
			fprintf( stdout, "Arg: %s\n", argv[1] );
		 err = execl( PPPTOOL, PPPTOOL, argv, NULL);
		} else {
		 err = execl( PPPTOOL, PPPTOOL, NULL);
		}
		 if ( err != 0 ){
			fprintf( stderr, "Error %d from execl\n", err );
			exit(err);	
		}
}
