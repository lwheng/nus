// Name:	Heng Low Wee
// Email:	lwheng@nus.edu.sg
// Matric:	U096901R

#include <stdio.h>

int main () {
	int N, i, j;
	scanf("%d", &N);
	printf("\n");
	printf("#include <stdio.h>\n");
	printf("int main() {\n");
	for (i=0; i<N; i++) {
		printf("\tprintf(\"");
		for (j=0; j<N; j++) {
			printf("*");
		}
		printf("\\n\");");
		printf("\n");
	}
	printf("\treturn 0;\n");
	printf("}\n");
	return 0;
}
