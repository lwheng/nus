#include <stdio.h>

int pascal (int n, int k) {
	if (n==0 || n==k) return 1;
	return pascal(n-1,k)+pascal(n-1,k-1);
}

int pascal_cps (int n, int k) {
	int aux (int n, int k, int (*j)(int)) {
		int j1 (int ret) {
			int j2 (int ret2) {
				return j(ret+ret2);
			}
			return aux(n-1,k-1,j2);
		}

		if (n==0 || n==k) return j(1);
		else return aux(n-1, k, j1);
	}
	int identity(int x) { return x; }
	return aux(n, k, identity);
}

int main () {
	int n = 2;
	int k = 1;
	printf("pascal = %d\n", pascal(n,k));
	//printf("pascal_cps = %d\n", pascal_cps(n,k));
	return 1;
}