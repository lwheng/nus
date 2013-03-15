#include <stdio.h>

struct Exception {
	enum {
		NOEXCEPTION,E1
	} type;
	char* msg;
} ex;

jmp_buf stack[100];
int sp = -1;

jmp_buf* push() {
	return &stack[++sp];
}

jmp_buf* pop() {
	return &stack[sp--];
}

int x = 0;

void g() {
	if (!setjmp(*push())) {
		if (x > 0) {
			ex.type = E1;
			ex.msg = "Exception from g";
			longjmp(*pop(),1);
		}
	}
}

void f() {
	if(!setjmp(*push())) {
		g();
		pop();
	}
	else {
		switch(ex.type) {
			case E1:
				printf("%s\n", ex.msg);
			default:
			finally:
				printf("Inside 'finally'\n");
			if(ex.type != NOEXCEPTION)
				longjmp(*pop(),1);
		}
	}
}

int main(int argc, char** argv) {
	x = atoi(argv[1]);
	f();
	pop();
	return 1;
}