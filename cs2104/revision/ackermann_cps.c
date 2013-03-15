#include <stdio.h>

int ackermann (int m, int n) {
	if (m==0) return n+1;
	else if (m>0 && n==0) return ackermann(m-1,1);
	else return ackermann(m-1, ackermann(m,n-1));
}

int ackermann_cps (int m, int n) {
	int aux (int m, int n, int(*j)(int)) {
		int j1 (int ret) {
			return j(ret);
		}
		int j2 (int ret) {
			int j3 (int ret2) {
				return j(ret2);
			}
			return aux(m-1,ret,j3);
		}
		if (m==0) return j(n+1);
		else if (m>0 && n==0) return aux(m-1, 1, j1);
		else return aux(m, n-1, j2);
	}
	int identity(int x) { return x; }
	return aux(m, n, identity);
}

int main () {
	printf("ackermann(4,3) = %d\n", ackermann(4,3));
	printf("ackermann_cps(4,3) = %d\n", ackermann_cps(4,3));
	return 1;
}