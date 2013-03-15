#include <stdio.h>

int f(int a, int b) {
	while (a != b) {
		if ( a<b )
			b-=a;
		else
			a-=b;
	return a;
	}
}

int eax,ebx,ecx,edx,edi,esi;
unsigned char M[10000];

void exec() {
	// let's set eax as the return register
	ebx = 10;
	ecx = 20;
	loop:
	if ( ebx != ecx ) goto then_branch;
	goto end_loop;

	then_branch:
		if ( ebx < ecx ) goto then1;
		ebx -= ecx;
		goto loop;

	then1:
		ecx -= ebx;
		goto loop;

	end_loop:
		eax = ebx;
}

int main() {
	printf("C call : %d\n", f(10,20));
	exec();
	printf("exec call :  eax = %d\n", eax);
}