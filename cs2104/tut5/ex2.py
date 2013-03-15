# HENG LOW WEE
# U096901R
# Tut 5 Ex 2

mylist = ["a", 123, "b", 234, "c", 345]
mydict = {}

def list2dict(list1):
	for i in range(0, len(list1),2):
		# iterating even index
		key = list1[i]
		val = list1[i+1]
		mydict[key] = val
		
	
list2dict(mylist)
print mylist
print mydict
