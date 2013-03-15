// Written by: HENG LOW WEE
// Student ID: U096901R
// Module: CS2104
// Problem Set 1

// Problem 3
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

int eax, ebx, ecx, edx, esi, edi;
unsigned char M[10000];
void exec();

int main() {
	int i=0;
	int total=0;
	int val;
	
	int num[] = {12, 34, 56, 128, 9};
	int ptr[] = {16, 56, 80, 64, 8, 0};
	
	// compute the expected total value
	for (i=0; i<5; i++) {
		val = num[i];
		total+=val;
	}
	ptr[5] = 0; // set last ptr to 0, which represents null pointer

	esi = ptr[0];
	int index = esi;
	for (i=0; i<5; i++) {
		printf("num=%d, ptr=%d\n", num[i], ptr[i+1]);
		*(int*)&M[index] = num[i];
		index+=4;
		*(int*)&M[index] = ptr[i+1];
		index = ptr[i+1];
	}
	printf("##################\n");
	
	exec();
	printf("expected total = %d\n", total);
	printf("eax = %d\n", eax);
	getchar();
}

void exec() {
	eax = 0;
	edi = esi; // edi is used to store the pointer
	
	loop_start:
	// get the value
	ebx = *(int*)&M[edi];
	// get the ptr
	ecx = *(int*)&M[edi+4]; // ecx is a temp for pointer
	edi = ecx;
	eax += ebx;
	
	if (edi) goto loop_start;
}