# # HENG LOW WEE
# U096901R
# Tut 5 Ex 3

mydict = {"a":123, "b":234, "c":345}
mylist = []

def dict2list(dict1):
	for key, val in dict1.iteritems():
		mylist.append(key)
		mylist.append(val)

dict2list(mydict)
print mydict
print mylist