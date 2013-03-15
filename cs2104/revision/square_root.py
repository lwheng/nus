def tail(g): # no haskell equivalent since no pattern matching
	def p():
		h = g()
		next(h)
		for x in h:
			yield x
        return p

def head(g): # no haskell equivalent since no pattern matching
    return next(g())

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


def fun(f,g):
	def rets():
		yield f(head(g))
		for x in fun(f,tail(g))():
			yield x
	return rets

def sqroot():
	yield 1.0
	# g=mapgen(lambda x:x/2.0 + 1.0/x,sqroot)()
	for x in mapgen(lambda x:x/2.0 + 1.0/x,sqroot)():
		yield x

i=sqroot()
k=1
while True:
	print next(i)
	k+=1
	if (k>15): break
