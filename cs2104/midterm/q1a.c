// CS2104 Midterm

// A. Write a VAL program that reverses the order of the elements in an array of integers. The address of the array will be given in the register EBX, and its size (i.e. number of elements) in the register ECX. At the end of the program, the registers EBX and ECX must remain unchanged. Your answer should only consist of the exec() procedure. You do not need to write a main() procedure.

#include <stdio.h>

int eax,ebx,ecx,edx,edi,esi;
unsigned char M[10000];

void exec() {
	esi = ebx;
	edi = ecx;
	edi += esi;
	edi -= 1;
	edi <<= 2;
	// now we have the address of the first and last element

	loop:
	if (esi >= edi) goto end_loop;
	eax = *(int*)&M[esi];
	edx = *(int*)&M[edi];
	*(int*)&M[esi] = edx;
	*(int*)&M[edi] = eax;
	esi += 4;
	edi -= 4;
	goto loop;

	end_loop:{}
}

int main() {
	int i=0;
	for (i=0; i<40; i+=4) {
		// printf("value of i = %d\n", i);
		*(int*)&M[i] = i;
	}
	i=0;
	printf("Expected Results:\n");
	int print;
	for (i=36; i>=0; i-=4) {
		print = *(int*)&M[i];
		printf("%d\n", print);
	}

	ebx = 0;
	ecx = 10;
	exec();

	printf("Actual Results after exec()\n");
	for (i=0; i<40; i+=4) {
		print = *(int*)&M[i];
		printf("%d\n", print);
	}

	return 1;
}