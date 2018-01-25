#include <stdio.h>

int main ( int argc, char* argv ) {

	char x[] = "x"; 
	char y[] = "y"; 
	char *z = strcat ( x, y );

	printf ( "z=%s\n", z );


}
