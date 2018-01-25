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
		/* fprintf( stdout, "USER_ID: %s\n", getenv("USER_ID")); */
		
		int err = 0;
		 if ( (err = execle( PPPTOOL, PPPTOOL, NULL, environ)) != 0 ){ 
			fprintf( stderr, "Error %d from execl\n", err );
			exit(err);	
		}
		/* fprintf( stdout, "USER_ID: %s\n", getenv("USER_ID")); */
}
