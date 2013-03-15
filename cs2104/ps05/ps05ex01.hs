--HENG LOW WEE
--U096901R
--Problem 5 Problem 1

insert a pos l = 
    fst ( foldr
        (\x (b,c) -> (if c==pos-1 then a:(x:b) else x:b,c+1))
        ((drop pos [a]),0) l)