--HENG LOW WEE
--U096901R
--CS2104 Tut 6 Ex 1

getmin :: [Integer] -> Integer
getmin [] = error "Form is emptiness; Emptiness is form"
getmin (h:t) = foldl (min) h t