# by rctay

from itertools import ifilter as ifilter

def num(k):
    while True:
        yield k
        k+=1

def make_filter_pred(p):
    return lambda x: x % p != 0

def sieve(g):
    while True:
        p = next(g)
        yield p
        g = ifilter(make_filter_pred(p), g)

	
i=sieve(num(2))
for j in range(100):
    print next(i)
	