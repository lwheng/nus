# HENG LOW WEE
# U096901R
# Problem Set 7 Problem 2

def tail(g): # no haskell equivalent since no pattern matching
	def p():
		h = g()
		next(h)
		for x in h:
			yield x
        return p

def head(g): # no haskell equivalent since no pattern matching
    return next(g())
# haskell: map f l = (f (head l)):(map f (tail l))
def mapgen(f,g):
    def rets():
        yield f(head(g))
        for x in mapgen(f,tail(g))():
        	yield x
    return rets
# haskell: ints = 1:map (\x -> x+1) ints
def ints():
    yield 1
    h=mapgen(lambda x:x+1,ints)()
    for x in mapgen(lambda x:x+1,ints)():
	    yield x

def ints2(k):
    yield k
    g=ints2(k+1)
    while True:
        yield next(g)

"""
Description of the Systematic Translation Scheme
------------------------------------------------
Consider the given mapgen() function
In Haskell, the map function is used with 2
arguments:
    1. the function
    2. the list
Following this pattern, zipwithgen() takes in 2
arguments, namely g1 and g2, and f is the
function to zip the 2 lists with (as in zipWith
of Haskell)

Just like how Haskell's zipWith would handle
infinite lists by applying the function to the
head of both lists first and then their tails,
in this Python implementation, we make use of
the given head() to retrieve the first iteration
of the generators, before proceeding to applying
recursively to the tails

In the n-th position of the Fibonacci sequence,
the value is computed by adding the (n-1)-th
and (n-2)-th number. Therefore, we must "yield
0" and "yield 1", to start the ball rolling.

The Haskell implementation is:
fib :: Int -> Integer
fib n = fibs !! n
    where
        fibs = 0 : 1 : zipWith (+) fibs (tail fibs)
    
Following suit, we pass in fib() into
zipwithgen() as g1, and the tail of fib()
recursively to generate the stream.

To conclude, for each function in Haskell that
handles infinite lists, the Python
implementation is via generators. In this
generator we then "tell" it how the original
function works in Haskell.

"""

def zipwithgen(f,g1,g2):
    def rets():
        yield f(head(g1),head(g2))
        for x in zipwithgen(f,tail(g1),tail(g2))():
            yield x
    return rets

def fib():
    yield 0
    yield 1
    for x in zipwithgen((lambda x,y:x+y),fib,tail(fib))():
        yield x

i=fib()
k=1
while True:
    print next(i)
    k+=1
    if (k>15): break