// Name:	Heng Low Wee
// Email:	lwheng@nus.edu.sg
// Matric:	U096901R

#include <stdio.h>

int main() {
	int N, i, j;
	scanf("%d", &N);
	printf("\n");
	int var[N];
	int var1[N-1];
	char c = 97;
	char d;
	for (i=0; i<N; i++) {
		var[i] = c;
		c++;
	}
	printf("#include <stdio.h>\n");
	printf("int main() {\n");
	
	// int a, b, .............
	printf("\tint ");
	for (i=0; i<N-1; i++) {
		c=var[i];
		printf("%c, ", c);
	}
	c=var[N-1];
	printf("%c;", c);
	printf("\n");

	// scanf(....................................)
	printf("\tscanf(\"");
	for (i=0; i<N-1; i++) {
		printf("%%d ");
	}
	printf("%%d\", ");
	for (i=0; i<N-1; i++) {
		c = var[i];
		printf("&%c, ", c);
	}
	c = var[N-1];
	printf("&%c);\n", c);

	if (N==2) {
		printf( "\tif ( a == b ) printf( \"NIL\\n\" );\n" );
		printf( "\tif ( a <  b ) printf( \"%%d\\n\", a );\n" );
		printf( "\tif ( b <  a ) printf( \"%%d\\n\", b );\n" );
	}
	else if (N > 2) {
		// Case: all equals
		printf( "\tif ( " );
		char d;
		c = var[0];
		for (j=1; j<N-1; j++) {
			d = var[j];
			printf("%c == %c && ", c, d);	
		}
		d = var[N-1];
		printf("%c == %c ) {\n\t\tprintf( \"NIL\\n\" );\n\t\treturn 0;\n\t}\n", c, d);

		// Case: Else, first find largest number
		printf("\tint max = 1<<31;\n");
		for (i=0; i<N; i++) {
			c = var[i];
			printf("\tif (%c > max || %c == max) max = %c;\n", c, c, c);
			
		}
		printf("\tint secondMax = 1<<31;\n");
		for (i=0; i<N; i++) {
			c = var[i];
			printf("\tif ((%c > secondMax || %c == secondMax) && %c < max) secondMax = %c;\n", c, c, c, c);
			
		}
		printf("\tprintf(\"%%d\\n\", secondMax);\n");
	}
	
	printf("\treturn 0;\n");
	printf("}\n");	
	return 0;
}
