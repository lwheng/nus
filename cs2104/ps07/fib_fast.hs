fib :: Int -> Integer
fib n = fibs !! n
	where
		fibs = 0 : 1 : zipWith (+) fibs (tail fibs)

--What is actually happening when we run the command:
--0 : 1 : zipWith (+) (0 : 1 : ...) (1 : ...)
--0 : 1 : (0 + 1) : zipWith (+) (1 : 1 : ...) (1 : ...)
--0 : 1 : 1 : (1 + 1) : zipWith (+) (1 : 2 : ...) (2 : ...)
--0 : 1 : 1 : 2 : (1 + 2) : zipWith (+) (2 : 3 : ...) (3 : ...)