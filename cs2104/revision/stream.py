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

def zipwithgen(f,g1,g2):
    def rets():
        yield f(head(g1),head(g2))
        for x in zipwithgen(f,tail(g1),tail(g2))():
            yield x
    return rets

def num():
	yield 0
	for x in mapgen(lambda x:x+1,num)():
		yield x

def alt():
	yield 1
	yield -1
	g=alt()
	while True:
		yield next(g) 

def stream():
	for x in zipwithgen(lambda x,y:x*y, num, alt)():
		yield x

i=stream();
k=1
while True:
    print next(i)
    k+=1
    if (k>15): break