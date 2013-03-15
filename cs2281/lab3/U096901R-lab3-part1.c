// Name:	Heng Low Wee
// Email:	lwheng@nus.edu.sg
// Matric:	U096901R

#include <stdio.h>
#define INPUT_MAX 65536
#define NUM_OF_LINE 10

// This is the char array to capture the entire input
char input[INPUT_MAX];
int c;
int i;
int count = 0;
int pos = 0;
int back;

void printString(int a, int b) {
	while (a < b) {
		printf("%c", input[a]);
		a++;
	}
	printf("\n");
}

int main() {
	while ((c = getchar()) != EOF) {
		// putchar(c); // output a byte
		input[pos] = c;
		pos += 1;
	}
	// By now, index should be pointing at after the last
	// character of the buffer

	// Now we proceed to print N lines
	back = pos;
	for (i=pos; i>=0; i--) {
		c = input[i];
		if (count-1 == NUM_OF_LINE) {
				break;
			}
		if (c == '\n') {
			printString(i+1,back);
			count += 1;
			back = i;
		}
		if (i==0) {
			printString(i, back);
			count += 1;
		}
	}
	return 0;
}
