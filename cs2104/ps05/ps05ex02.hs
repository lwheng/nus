--HENG LOW WEE
--U096901R
--Problem Set 5 Problem 2

-- count x l = foldr <code> 0 <code> 

count x l = foldr (\a b->b+1) 0 (filter(==x) l)