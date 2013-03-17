// Name:	Heng Low Wee
// Email:	lwheng@nus.edu.sg
// Matric:	U096901R

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#define DEFAULT_LINES (10)
#define CHUNK_SIZE (16)

void reverse_line(int); // prototype for your function
void startprog(void);
void endprog(void);

int main(int argc, char *argv[])
{
	// do NOT modify
	int n = -1;
	if (argc == 3 && !strcmp(argv[1], "-n"))
		n = atoi(argv[2]);
	else if (argc == 1) n = DEFAULT_LINES;
	else {
		fprintf(stderr, "Usage: %s [-n lines]\n", argv[0]);
		exit(1); // command line error
	}
	if (n < 0) n = 0;
	startprog();
	reverse_line(n);
	endprog();
	exit(0); // no errors
}

void reverse_line(int n) 
{
	// your code
	// you may define other functions called from here

	// Restriction:
	// Heap usage <= 64K + S/2 + 4n bytes
	// S: input size
	// n: no. of pointers (no. of lines)
	
	// To eat a character
	int c;
	// To store the n lines
	char *record[n];
	// Index for the array of n lines
	int index = 0;
	// To keep track of number of characters eaten
	int pos = 0;
	// For character transfer
	char *buf;
	// To keep track of no. of chunks
	int last = index;
	// To keep track of line size
	int size = CHUNK_SIZE;

	int i;
	for (i=0; i<n; i++) {
		record[i] = NULL;
	}

	while ((c = getchar()) != EOF) {
		if (record[index] == NULL) {
			record[index] = malloc(CHUNK_SIZE);
		}
		if (pos < size) {
			if (c == '\n') {
				// Reach newline character, write \0
				*(record[index]+pos) = '\0';
				// Allocate a temp space
				buf = malloc(pos+1);
				// Copy current to temp
				memcpy(buf, record[index], pos+1);
				// Free current
				free(record[index]);
				// Point pointer to temp
				record[index] = buf;
				// Keep track of where the last line was store
				last = index;
				// Reset the index
				index = (index+1)%n;
				// Reset pos
				pos = 0;
				// Reset size
				size = CHUNK_SIZE;
			}
			else {
				// Eat that character
				*(record[index]+pos) = c;
				// Increase pos
				pos++;
			}
		}
		else {
			// We need to re-allocate memory
			// Double the size
			size *= 2;
			// Allocate double the size
			buf = malloc(size);
			// Copy data onto new memory space
			memcpy(buf, record[index], pos+1);
			// Free old space
			free(record[index]);
			// Point pointer to new space
			record[index] = buf;
			// Eat that character
			*(record[index]+pos) = c;
			// Increase pos
			pos++;
		}
	}
	printf("\n");
	for (i=0; i<n; i++) {
		index = (n+last-i)%n;
		if (record[index] != NULL)
			printf("%s\n", record[index]);
	}

}

