// HENG LOW WEE
// Problem Set 1 Further Practice Problem 4

#include <stdio.h>

int eax,ebx,ecx,edx,edi,esi;
unsigned char M[10000];

void exec() {
	esi = ebx;
	edx = *(int*)&M[esi+4];
	edx -= *(int*)&M[esi]; // Now we know the difference

	if (edx == 0) goto done;
	esi+=4;
	edi = ecx;
	edi -= 1;
	edi *= 4;
	loop:
	if (esi >= edi) goto end;
	eax = *(int*)&M[esi+4];
	eax -= *(int*)&M[esi];
	if (eax == edx) goto set_eax;
	goto done;

	set_eax:
	eax = 1;
	esi+=4;
	goto loop;

	done:
	eax = 0;
	goto end;

	end:{}
}

int main() {
  int i ;
  ebx = 0 ;
  ecx = 5 ;
  // *(int*)&M[0] = 1;
  // *(int*)&M[4] = 2;
  // *(int*)&M[8] = 3;
  // *(int*)&M[12] = 4;
  // *(int*)&M[16] = 5;

  // *(int*)&M[0] = 5;
  // *(int*)&M[4] = 4;
  // *(int*)&M[8] = 3;
  // *(int*)&M[12] = 2;
  // *(int*)&M[16] = 1;

  *(int*)&M[0] = 1;
  *(int*)&M[4] = 3;
  *(int*)&M[8] = 5;
  *(int*)&M[12] = 7;
  *(int*)&M[16] = 9;
  exec() ;
  printf("eax = %d\n", eax);
  return 0 ;
}