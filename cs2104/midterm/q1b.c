// B. Translate the following C procedure into VAL

// In your translation, apply the systematic translation schemes that were given in class. Pay particular attention to the correct set-up of the activation record.

int f (int n) {
	int x = 1 ;
	while ( n > 0 ) {
		x += f(n) ;
		n--;
	}
	return x ;
}

int eax,ebx,ecx,edx,edi,esi,esp,ebp;
unsigned char M[10000];

void exec() {
	f:
	esp -= 4;
	*(int*)&M[esp] = ebp;
	ebp = esp;
	esp -= 4;
	*(int*)&M[ebp-4] = 1;

	while_loop:
	if (*(int*)&M[ebp+8] <= 0) goto end_while;
	eax = *(int*)&M[ebp+8];
	esp -= 4;
	*(int*)&M[esp] = eax;
	esp -= 4;
	* (void **)&M[esp] = && return_to_x;
	goto f;

	return_to_x:
	esp += 8 ;
	eax += *(int*)&M[ebp-4] ;
	*(int*)&M[ebp-4] = eax ;
	*(int*)&M[ebp+8] -= 1 ;
	goto while_loop ;

	end_while:
	eax = *(int*)&M[ebp-4];
	esp = ebp;
	ebp = *(int*)&M[ebp];
	esp += 4;
	goto * (void**)&M[esp];
}

int main() {
	return 1;
}