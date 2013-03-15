#include <stdio.h>

// Say we have this function
int f(int n, int k) {
	if (n==0 || n==k) return 1;
	return f(n-1,k)+f(n-1,k-1)+f(n,k-1);
}

// the benefit of writing this complicated function below,
// which returns the same output as the one above,
// is that we are able to convert the above recursive function
// into a tail-recursive one, which is simply better

int champ(int n, int k) {
	int aux(int n, int k, int (*c)(int)) {
		int c1(int r1) {
			int c2(int r2) {
				int c3(int r3) {
					return c(r1+r2+r3);
				}
				return aux(n,k-1,c3);
			}
			return aux(n-1,k-1,c2);
		}
		if (n==0 || n==k) return c(1);
		else return aux(n-1,k,c1);
	}
	int identity(int x) { return x; }
	return aux(n, k, identity);
}

int main() {
	return 0;
}
