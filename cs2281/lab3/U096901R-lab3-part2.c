// Name:	Heng Low Wee
// Email:	lwheng@nus.edu.sg
// Matric:	U096901R

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define DEFAULT_LINES (10)

#define INPUT_MAX (65536)
char input[INPUT_MAX];
int c;
int i;
int count = 0;
int pos = 0;
int j;
int back;

void reverse_line(int); // prototype for your function

int main(int argc, char *argv[])
{
	// do NOT modify
	// no need to understand argv[] yet but it is an array
	// of strings for the words in the command line
	// argc counts the number of words in the command line
	int n = -1;
	if (argc == 3 && !strcmp(argv[1], "-n"))
		n = atoi(argv[2]);
	else if (argc == 1) n = DEFAULT_LINES;
	else {
		fprintf(stderr, "Usage: %s [-n lines]\n", argv[0]);
		exit(1); // command line error
	}
	if (n < 0) n = 0;
	reverse_line(n);
	exit(0); // no errors
}

void printString(int a, int b) {
	while (a < b) {
		printf("%c", input[a]);
		a++;
	}
}

void reverse_line(int n) 
{
	// your code
	// you may define other functions called from here
	// For reading from stdin
	while ((c = getchar()) != EOF) {
		pos = pos%INPUT_MAX;
		input[pos] = c;
		pos++;
	}
	printf("\n");
	// Now we proceed to print N lines
	pos--;
	back = pos--;
	for (i=0; i<INPUT_MAX; i++) {
		j = ((pos-i) < 0) ? INPUT_MAX + (pos-i) : (pos-i)%INPUT_MAX;
		c = input[j];
		if (count == n) {
			break;
		}
		if (c == '\n') {
			if (back <= j) {
				printString(j+1, INPUT_MAX);
				printString(0, back);
				printf("\n");
				count += 1;
				back = j;
			}
			else {			
				printString(j+1, back);
				printf("\n");
				count += 1;
				back = j;
			}
		}
	}
}

