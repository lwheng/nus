quicksort [] = []
quicksort (p:xs) = (quicksort smaller) ++ [p] ++ (quicksort bigger)
	where
		smaller = filter (<p) xs
		bigger = filter (>=p) xs