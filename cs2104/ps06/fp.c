#include <stdio.h>

void hanoi(char ** p, int n, int a, int b, int c) {
	int aux (char ** p, int n, int a, int b, int c, int(*j)(int)) {
		int j1 (char ** ret) {
			int j2 (char ** ret2) {
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
			}
			return aux(p, n-1, c, b, a, j2);
		}
		if (n == 0) return;
		return aux(p, n-1, a, c, b, j1);
	}
	int identity (int x) { return x; }
	return aux(p, n, a, b, c, identity);
}

int main() {
	char a[1000] ; // string buffer
	char *p = a ; // current position in string 
	hanoi(&p,4,1,2,3) ; // build the string of moves for 4 discs 
	*p = '\0' ; // terminate the string
	printf(a) ; // string could be printed, but not in VAL code
	return 0;
} 