--HENG LOW WEE
--U096901R
--Tut 6 Ex 2

ex2 :: (Integer, Integer, Integer) -> (Integer, Integer)
ex2 (a, b, 100) = (a, b)
ex2 (a, b, i)
	| (even a) = ex2 (a*2, (3*(a*2)+1), i+1)
	| otherwise = ex2 (2*b+1, b-1, i+1)