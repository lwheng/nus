// Name:	Heng Low Wee
// Email:	lwheng@nus.edu.sg
// Matric:	U096901R

#include <stdio.h>

int main () {
	int N, i, j;
	scanf("%d", &N);
	printf("\n");
	int var[N];
	char c = 97; // 'a'=97
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
	
	// if ( a > b && ....) printf( ........... );
	char d;
	int count = N-1;
	for (i=0; i<N; i++) {
		c = var[i];
		printf("\tif (");
		for (j=0; j<N; j++) {
			d = var[j];
			if (c != d) {
				printf("%c > %c", c, d);
				if (j == N-1 || (i == N-1 && j == N-2)) continue;
				printf (" && ");
			}
		}
		printf(") printf(\"%%d\\n\", %c);\n", c);
	}
	printf("\treturn 0;\n");
	printf("}\n");
	return 0;
}