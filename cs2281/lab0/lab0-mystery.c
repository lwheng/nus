#include <stdio.h>

int mystery(int n) {
    printf("Current n is: %d\n", n);
    int x;
    if (n == 0) return 0;
    if (n & 1) x = 2;
    else x = 0;
    return x + (mystery(n >> 1) << 1);
}
