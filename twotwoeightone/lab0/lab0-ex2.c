#include <stdio.h>

extern int mystery(int);

int main(int argc, char *argv[]) 
{
	int  n;

	printf("Enter n? ");
	scanf("%d", &n); 

	printf("mystery(%d) = %d\n", n, mystery(n));
	return 0;
}
