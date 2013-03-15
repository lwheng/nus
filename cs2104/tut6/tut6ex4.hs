--HENG LOW WEE
--U096901R
--Tut 6 Ex 4

partition :: (Integer, [Integer]) -> ([Integer], [Integer])
partition(_, []) = ([],[])
partition(pivot, list) = (filter (<pivot) list, filter (>pivot) list)