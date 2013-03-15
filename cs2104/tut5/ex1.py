# HENG LOW WEE
# U096901R
# Tut 5 Ex 1

mylist = [123, 234, 345, 456]

def sumitup(list1):
	temp = 0
	for num in list1:
		temp += num
	print 'Total is %d' % (temp)

def sumPro(list1):
	init = 0
	print 'Total is %d' % reduce(lambda x,y:x+y, list1, init)

# reduce is like the foldr/foldl as seen in Haskell
	
sumitup(mylist)
sumPro(mylist)