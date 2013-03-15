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
	printf("int g(int a, int b) {\n");
	printf("\tif (a >= b) return a;\n");
	printf("\treturn b;\n");
	printf("}\n");

	printf("int m(int a, int b, int c) {\n");
	printf("\tif (a >= b && a < c) return a;\n");
	printf("\treturn b;\n");
	printf("}\n");	

	printf("int l(int a, int b) {\n");
	printf("\tif (a <= b) return a;\n");
	printf("\treturn b;\n");
	printf("}\n");	
	printf("int main() {\n");

	if (N==2) {
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

		printf( "\tif (a == b) printf(\"NIL\\n\");\n");
		printf( "\tif (a < b) printf(\"%%d\\n%%d\\n\", a, b-a);\n");
		printf( "\tif (b < a) printf(\"%%d\\n%%d\\n\", b, a-b);\n");
	}
	else if (N > 2) {
		// int a, b, .............
		printf("\tint X = 1<<31, Y = X, Z = -(X+1), ");
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
		// Case: all equals
		printf( "\tif ( " );
		char d;
		c = var[0];
		for (j=1; j<N-1; j++) {
			d = var[j];
			printf("%c == %c && ", c, d);	
		}
		d = var[N-1];
		printf("%c == %c ) {\n\t\tprintf(\"NIL\\n\");\n\t\treturn 0;\n\t}\n", c, d);

		// Case: Else, first find largest number
		for (i=0; i<N; i++) {
			c = var[i];
			printf("\tX=g(%c,X);\n", c);
			// printf("\tif (%c >= X) X = %c;\n", c, c);
			
		}
		for (i=0; i<N; i++) {
			c = var[i];
			printf("\tY=m(%c,Y,X);\n",c);
			// printf("\tif (%c >= Y && %c < X) Y = %c;\n", c, c, c, c);
		}
		for (i=0; i<N; i++) {
			c = var[i];
			printf("\tZ=l(%c,Z);\n", c);
			// printf("\tif (%c <= Z) Z = %c;\n", c, c, c);
		}
		printf("\tprintf(\"%%d\\n%%d\\n\",Y,X-Z);\n");
	}
	
	printf("\treturn 0;\n");
	printf("}\n");	
	return 0;
}