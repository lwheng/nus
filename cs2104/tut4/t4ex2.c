/*
HENG LOW WEE
U096901R

Tut 4 Ex 2

int **(*(*p()))[10];

http://cedcl.org
declare p as pointer to function returning pointer to array 10 of pointer to pointer to int
declare f as function returning pointer to array of pointer to pointer to int

p=&f is legal

*/

#include <stdio.h>

int **(*f())[] {
	static int **v[10];
	return &v;
}

int main() {
	int **(*(*p)())[10];
	p = &f;
	return 0;
}