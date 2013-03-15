// Written by: HENG LOW WEE
// Student ID: U096901R
// Module: CS2104
// Problem Set 1

// Problem 1
#include <stdio.h>
#include <string.h>

int eax, ebx, ecx, edx, esi, edi;
// esi is the pointer to the string
// eax is the output
unsigned char M[10000];
void exec();

int main() {
	
	// These values are here for testing
	// let's say ebx is the length of string
	// start address is esi, end address is ebx-1
	esi = 1234;
	char a[] = {'9','9','9'};
	ebx = 3;
	memcpy(&M[esi], a, ebx);
	//
	
	exec();
	eax += 1; // just to make sure eax captured an int
	printf("When testing, we expect eax=999\n");
	printf("eax+1 = %d\n", eax);
	getchar();
}

void exec() {
	
	// ASCII code for digits 0-9 is 48-57
	edi = esi; // edi is used as the index
	edi += ebx; // add length to go to the last character
	edi -= 1; // start from the last character
	ecx = 1; // multiplier
	
	loop_start:
	
	if ( edi >= esi ) goto then_branch;
	goto loop_end;
	
	then_branch:
	edx = (int)M[edi]; // get the character's ASCII number
	edx -= 48; // and we have get the numeric value of the character
	edx *= ecx; // multiply according to its place. e.g. tenth, hundredth
	eax += edx; // add this number
	ecx *= 10; // increase its place
	edi -= 1; // decrement the index
	goto loop_start;
	
	loop_end:{}
}