#include <stdio.h>

int main(int argc, char *argv[]) // cover argc & argv when we look at system calls
{
	int n;
	printf("enter n? ");
	scanf("%d", &n);

	//printf("Hello\n");
    printf("Hello");
    int i;
    for (i=0; i<n; i++) {
        printf("!");
    }
    printf("\n");
	printf("Welcome\n");

	return 0;
}
