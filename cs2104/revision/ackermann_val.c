#include <stdio.h>

int eax,ebx,ecx,edx,edi,esi,esp,ebp;
unsigned char M[10000];

int Ackermann(int m, int n) {
	if (m==0) return n+1;
	else if (m>0 && n==0) return Ackermann(m-1,1);
	else return Ackermann(m-1, Ackermann(m,n-1));
}

void exec() {
	esp = 10000;
	esp -= 4; *(int*)&M[esp] = eax;			// push n
	esp -= 4; *(int*)&M[esp] = ecx;			// push m
	eax = (int) && return_address;
	esp -= 4; *(int*)&M[esp] = eax;
	goto ackermann;

	ackermann:
		esp -= 4; *(int*)&M[esp] = ebp;
		ebp = esp;
		eax = *(int*)&M[ebp+8];				// get m;
		ecx = *(int*)&M[ebp+12];			// get n;

		if (eax==0) goto return1;						// return n+1
		if (eax>0 && ecx==0) goto ackermann1_setup;		// return Ackermann(m-1,1)
		goto ackermann2_setup;							// return Ackermann(m-1, Ackermann(m,n-1))
	
	return1:
		eax = *(int*)&M[ebp+12];
		eax += 1;
		goto exit_ackermann;

	exit_ackermann:
		ebp = *(int*)&M[esp]; esp += 4;
		esp += 4; goto *(void**)&M[esp-4];

	ackermann1_setup:						// return Ackermann(m-1,1)
		eax = *(int*)&M[ebp+8];				// get m 
		eax -= 1;							// m-1
		esp -= 4; *(int*)&M[esp] = 1;		// push 1
		esp -= 4; *(int*)&M[esp] = eax;		// push m-1
		goto ackermann;

	ackermann2_setup:
		eax = *(int*)&M[ebp+8];
		ecx = *(int*)&M[ebp+12];
		ecx -= 1;
		esp -= 4; *(int*)&M[esp] = ecx;
		esp -= 4; *(int*)&M[esp] = eax;
		eax = (int) && return_ackermann2;
		esp -= 4; *(int*)&M[esp] = eax;
		goto ackermann;

	return_ackermann2:
		*(int*)&M[ebp+12] = eax;
		eax = *(int*)&M[ebp+8];
		ecx = *(int*)&M[ebp+12];
		eax -= 1;
		esp -= 4; *(int*)&M[esp] = ecx;
		esp -= 4; *(int*)&M[esp] = eax;
		goto ackermann;

	return_address:{}
		
}

int main () {
	eax = 1;
	ecx = 0;
	exec();
	printf("eax = %d\n", eax);
	return 1;
}