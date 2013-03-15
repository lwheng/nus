pascal l = 1:(zipWith (\x y -> x+y) l (tail l))++[1]

--triangle l = do
--	print l
--	triangle (pascal l)

triangle l = l:(triangle (pascal l))