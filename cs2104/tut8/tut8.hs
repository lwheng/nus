--HENG LOW WEE
--U096901R
--Tut 8

power x = zipWith (/) (map (x^) [0..]) (map (\x -> product [1..x]) [0..]) :: [Rational]