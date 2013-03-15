// HENG LOW WEE
// U096901R
// Problem Set 6 Problem 2

#include <stdio.h>
// builds a string showing the sequence of moves that
// solves the towers of hanoi puzzle -- moving all discs // from peg 'a' to peg 'b' using peg 'c' as aux
// n is the number of discs, and assumed to be less than 10 
void hanoi(char ** p, int n, int a, int b, int c) {
	if ( n == 0 ) return ;
	hanoi(p,n-1,a,c,b) ;
	**p = '0'+(char)a ;
	(*p) ++ ;
	**p = ' ' ;
	(*p) ++ ;
	**p = 't' ;
	(*p) ++ ;
	**p = 'o' ;
	(*p) ++ ;
	**p = ' ' ;
	(*p) ++ ;
	**p = '0'+(char)b ;
	(*p) ++ ;
	**p = '\n' ;
	(*p) ++ ;
	hanoi(p,n-1,c,b,a) ;
}

int eax,ebx,ecx,edx,esi,edi,ebp,esp;
unsigned char M[10000];

void exec() {
	esp = 10000;
	ebp = esp;
	esp -= 4 ; *(int*)&M[esp] = eax ; // push eax
	esp -= 4 ; *(int*)&M[esp] = ecx ; // push ecx
	esp -= 4 ; *(int*)&M[esp] = edx ; // push edx
	esp -= 4 ; *(int*)&M[esp] = 3 ; // push c
	esp -= 4 ; *(int*)&M[esp] = 2 ; // push b
	esp -= 4 ; *(int*)&M[esp] = 1 ; // push a
	esp -= 4 ; *(int*)&M[esp] = 4 ; // push n
	esp -= 4 ; // push in char ** p here // push p
	eax = (int) && return_address ;
	esp -= 4 ; *(int*)&M[esp] = eax ; // push return_address
	goto hanoi;

	hanoi:
		esp -= 4 ; *(int*)&M[esp] = ebp ; // push ebp
		ebp = esp ;
		esp -= 4 ; *(int*)&M[esp] = ebx ; // push ebx
		esp -= 4 ; *(int*)&M[esp] = edi ; // push edi
		esp -= 4 ; *(int*)&M[esp] = esi ; // push esi
		// there is no declaration of local variables
		// so no need to allocate space for local variables

		// get value of n
		eax = *(int*)&M[ebp+12]; // this is assuming char ** p is also 4 bytes

		// check if n = 0
		if (eax == 0) goto return_hanoi;
		// set up for next call
		esp -= 4 ; *(int*)&M[esp] = eax ; // push eax
		esp -= 4 ; *(int*)&M[esp] = ecx ; // push ecx
		esp -= 4 ; *(int*)&M[esp] = edx ; // push edx
		// hanoi(p,n-1,a,c,b)
		eax = *(int*)&M[ebp+20]; // b
		esp -= 4 ; *(int*)&M[esp] = eax ; // push b
		eax = *(int*)&M[ebp+24]; // c
		esp -= 4 ; *(int*)&M[esp] = eax ; // push c
		eax = *(int*)&M[ebp+16]; // a
		esp -= 4 ; *(int*)&M[esp] = eax ; // push a
		eax = *(int*)&M[ebp+12]; // n-1
		esp -= 4 ; *(int*)&M[esp] = eax ; // push n-1
		// get p
		esp -= 4 ; // push in char ** p here
		eax = (int) && proc ;
		esp -= 4 ; *(int*)&M[esp] = eax ; // push the return-to address
		goto hanoi;
	
	proc:
		// some clearing needed here
		edx = *(int*)&M[esp] ; esp += 4 ; // pop edx
		ecx = *(int*)&M[esp] ; esp += 4 ; // pop ecx
		eax = *(int*)&M[esp] ; esp += 4 ; // pop eax
		// get p, a, b, c to carry out procedure
		// note: void hanoi(char ** p, int a, int b, int c)
		p = *(int*)&M[ebp+8];
		a = *(int*)&M[ebp+16];
		b = *(int*)&M[ebp+20];
		// carry out procedure
		**p = '0'+(char)a ;
		(*p) ++ ;
		**p = ' ' ;
		(*p) ++ ;
		**p = 't' ;
		(*p) ++ ;
		**p = 'o' ;
		(*p) ++ ;
		**p = ' ' ;
		(*p) ++ ;
		**p = '0'+(char)b ;
		(*p) ++ ;
		**p = '\n' ;
		(*p) ++ ;

		// set up for next call
		esp -= 4 ; *(int*)&M[esp] = eax ; // push eax
		esp -= 4 ; *(int*)&M[esp] = ecx ; // push ecx
		esp -= 4 ; *(int*)&M[esp] = edx ; // push edx
		// hanoi(p,n-1,c,b,a)
		eax = *(int*)&M[ebp+16]; // a
		esp -= 4 ; *(int*)&M[esp] = eax ; // push a
		eax = *(int*)&M[ebp+20]; // b
		esp -= 4 ; *(int*)&M[esp] = eax ; // push b
		eax = *(int*)&M[ebp+24]; // c
		esp -= 4 ; *(int*)&M[esp] = eax ; // push c
		eax = *(int*)&M[ebp+12]; // n-1
		esp -= 4 ; *(int*)&M[esp] = eax ; // push n-1
		// get p
		esp -= 4 ; // push in char ** p here
		eax = (int) && proc ;
		esp -= 4 ; *(int*)&M[esp] = eax ; // push return_address
		goto hanoi;

	return_hanoi:
		esi = *(int*)&M[esp] ; esp += 4 ; //pop esi
		edi = *(int*)&M[esp] ; esp += 4 ; //pop edi
		ebx = *(int*)&M[esp] ; esp += 4 ; //pop ebx
		ebp = *(int*)&M[esp] ; esp += 4 ; //pop ebp
		esp += 4 ; goto * *(void**)&M[esp-4] ; // return to wherever
	return_address:
		// clear arguments
		// no arguments to clear
		edx = *(int*)&M[esp] ; esp += 4 ; // pop edx
		ecx = *(int*)&M[esp] ; esp += 4 ; // pop ecx
		eax = *(int*)&M[esp] ; esp += 4 ; // pop edx
		// should be nothing else to do?
}

int main() {
	char a[1000] ; // string buffer
	char *p = a ; // current position in string 
	hanoi(&p,4,1,2,3) ; // build the string of moves for 4 discs 
	*p = '\0' ; // terminate the string
	printf(a) ; // string could be printed, but not in VAL code
	//exec();
	return 0;
} 