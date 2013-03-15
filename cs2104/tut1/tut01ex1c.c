// HENG LOW WEE
// U096901R
// Tut 1 Ex 1C

#include <stdio.h>

int eax,ebx,ecx,edx,edi,sdi;
unsigned char M[10000];

void exec() {
	void *caseptr[4] = { &&L1, &&L2, &&L3, &&L4 } ;
	if ( ebx < 0 ) goto return_neg ;
	if ( ebx > 3 ) goto return_neg ;
	goto *caseptr[ebx] ;
	L1 : 
		eax = 13 ;
	L2 : 
		eax = 8 ;
	L3 : 
		eax = 5 ;
	L4 : 
		eax = 3 ;
	return_neg:
		eax = -1 ;
}

int f(int x) {
	switch(x) {
		case 0:
			return 13;
		case 1:
			return 8;
		case 2:
			return 5;
		case 3:
			return 3;
		default:
			return -1;
	}
}

int main() {
	printf("f(3) = %d\n", f(13));
	ebx = 3;
	exec();
	printf("exec() = %d\n", eax);
}