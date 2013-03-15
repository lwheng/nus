--HENG LOW WEE
--U096901R
--Problem 5 Problem 1

insert a pos l = 
    fst ( foldr
        (\x (b,c) -> (if c==pos-1 then a:(x:b) else x:b,c+1))
        ((drop pos [a]),0) l)

--HENG LOW WEE
--U096901R
--Problem Set 5 Problem 2

-- count x l = foldr <code> 0 <code> 

count x l = foldr (\a b->b+1) 0 (filter(==x) l)


// HENG LOW WEE
// U096901R
// Problem Set 5 Problem 3
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

// function declaration
double halfInterval(double (*f)(double), double x1, double x2, double esp);

// this function is to test a custom function
double g_x(double x) {
	return x*x;
}

// main function
int main (int argc, char** argv) {	
	printf("%f\n",halfInterval(&sin, 1.0, 4.0, 0.000000000000001));
	printf("%f\n",halfInterval(&cos, 1.0, 4.0, 0.000000000000001));
	printf("%f\n",halfInterval(&tan, 1.0, 4.0, 0.000000000000001));
	printf("%f\n",halfInterval(&g_x, -1.0, 4.0,0.000000000000001));
	return 0;
}

// function definition
double halfInterval (double (*f)(double),double x1,double x2,double esp) {
	if (fabs(x1-x2) < esp) {
		return (x1+x2)/2;
	}
	else if (f(x1)*f((x1+x2)/2) <= 0) {
		return halfInterval(f, x1, (x1+x2)/2, esp);
	}
	else if (f(x2)*f((x1+x2)/2) <= 0) {
		return halfInterval(f, (x1+x2)/2, x2, esp);
	}
}