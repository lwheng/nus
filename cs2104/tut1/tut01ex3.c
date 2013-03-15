#include <stdio.h>
#include <stdlib.h>

int eax, ebx, ecx, edx, esi, edi ;
unsigned char M[10000] ;

void exec() {
	eax = *(int*)&M[ebx] ; // gets the first element and assigns to eax
	edx = ebx ;
	loop:
		edx += 1 ;
		edi = edx;
		edi *= 4;
		if ( edx < ecx ) goto then_branch;
		goto return_p ;
	then_branch:
		esi = *(int*)&M[edi] ;
		if ( esi < eax ) goto then_branch2 ;
		goto loop ;
	then_branch2:
		eax = esi ;
		goto loop ;
	return_p:{}
}

int main() {
	*(int*)&M[0] = 20;
	*(int*)&M[4] = 54;
	*(int*)&M[8] = 3;
	*(int*)&M[12] = 76;
	*(int*)&M[16] = 92;
	ebx = 0;
	ecx = 5;
	exec();
	printf("Mininum expected is eax = 3\n");
	printf("Mininum = %d\n", eax);
	return 0;
}