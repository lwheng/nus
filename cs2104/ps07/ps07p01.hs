-- Haskell program to simulate digital circuits
--
--  a signal is an infinite list of Bool; each element 
--     represents the value of the signal for a specific
--     clock cycle
--
--  logic gates are higher order functions that take
--     signals as arguments and output a signal ;
--     each gate also has a certain delay
--
--  complex circuits, like the /SR NAND latch given below
--     are obtained by connecting inputs and outputs of gates
--     through signals, which are transmitted by wires ;
--     thus, there will be a signal for every wire in the circuit

module Sim where

-- the signal that is always on "High"
high::[Bool]
high = True:high

-- the signal that is always on "Low"
low::[Bool]
low = False:low

-- create a limited list, of length "n", filled with logical value "fill"
-- useful to create signals by appending finite sequences
set :: Integer -> Bool -> [Bool]
set 0 _ = []
set n fill | n>0 = fill:(set (n-1) fill)

-- delay a signal by "n" clock cycles
--   prepends "n" instances of "fill" to the signal
delay :: Integer -> Bool -> [Bool] -> [Bool]
delay n fill s = (set n fill) ++ s

-- the inverter gate inverts every level for the entire signal,
-- and also delays the signal by one clock cycle
not_gate :: [Bool] -> [Bool]
not_gate s = delay 1 True (map not s)

-- a clock can be obtained by connecting an inverter's output to its input
--         |\       Clock
--      +--| >o----+--
--      |  |/      |
--      |          |
--      +----------+
clock :: [Bool]
clock = not_gate clock

-- and gate delays its output by 2 clock cycles
and_gate :: [Bool] -> [Bool] -> [Bool]
and_gate i1 i2 = delay 2 True (zipWith (&&) i1 i2)
                               -- "zipWith" is similar to map
							   -- but it takes a binary operator
							   -- and two lists, and produces
							   -- a list of results of the binary
							   -- operator being applied to corresponding
							   -- elements in the argument list
							   -- (&&) is the conjunction operator

--  A /SR latch (http://en.wikipedia.org/wiki/Latch_%28electronics%29)
--     is a device made up of 4 gates and 4 wires. Notice how the 
--     signals on the wires are defined in terms of outputs of the
--     corresponding gates.
--             ____
--     /S ----|    \   w1 |\       Q
--            |     |-----| >o----+--
--         +--|____/      |/      |
--         |                      |
--         +--------------------+ |
--                              | |
--         +-------------------- -+
--         |   ____             |
--         +--|    \   w2 |\    |  /Q
--            |     |-----| >o--+----
--     /R ----|____/      |/
--

srneg_latch :: [Bool]->[Bool]->[Bool]
srneg_latch s r = 
  let (q,qbar,w1,w2) = (not_gate w1,not_gate w2,and_gate s qbar,and_gate r q) in q

-- Consider now the following signals: (each dash represents 1 clock cycle)
--
--                +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
--  /S      6     |
--     -+-+-+-+-+-+  
--
--     -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+           +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-
--  /R            15                |     6     |
--                                  +-+-+-+-+-+-+
--
--  The following expression, when evaluated at the top level (you will have to cut and paste), 
--  will return the the Q signal of the latch for the first 40 clock cycles
--
--  take 40 (srneg_latch ((set 6 False)++high) ((set 15 True)++(set 6 False)++high))

--JK Flip Flop
--  Various Modes for JK Flip Flop
--    J  |  K  | Q
--  ---------------------------
--    0  |  0  | No Change to Q
--    0  |  1  | Set Q=0
--    1  |  0  | Set Q=1
--    1  |  1  | Toggle Q

--I have created 2 versions:
--	1. display1()
--		I created the JK-Gate myself, and implemented the truth table with it. Feels like I'm cheating because a JK Flip-flop is actually made up of 4 NAND gates
--	2. display()
--		Built using NAND gates, but cannot get a JK-gate output in the manner [... True, True, False, False, True, True, False, False] when supplied with J=K=True

--3-input AND gate
and_gate3 :: [Bool] -> [Bool] -> [Bool] -> [Bool]
and_gate3 i1 i2 i3 = delay 2 True (zipWith (&&) i1 (zipWith (&&) i2 i3))

--NAND gate with a 2-input AND
nand_gate :: [Bool] -> [Bool] -> [Bool]
nand_gate i1 i2 = not_gate (and_gate i1 i2)

--NAND gate with a 3-input AND
nand_gate3 :: [Bool] -> [Bool] -> [Bool] -> [Bool]
--nand_gate3 i1 i2 i3 = not_gate (and_gate3 i1 i2 i3)
nand_gate3 i1 i2 i3 = not_gate (and_gate (and_gate i1 i2) i3)

jk_gate2 :: [Bool] -> [Bool] -> [Bool] -> [Bool]
jk_gate2 j c k =
	let (q,qbar,w1,w2) =
		(nand_gate w1 qbar,
		nand_gate q w2,
		nand_gate3 qbar j c,
		nand_gate3 c k q
		) in q

and_gate1 :: [Bool] -> [Bool] -> [Bool]
and_gate1 i1 i2 = (zipWith (&&) i1 i2)

--jkflipper j c k q
jkflipper :: Bool -> Bool -> Bool -> Bool -> Bool
jkflipper False True False q = q
jkflipper False False False q = q
jkflipper False True True q = False
jkflipper False False True q = q
jkflipper True True False q = True
jkflipper True False False q = q
jkflipper True True True q = not q
jkflipper True False True q = q

jk_gatehelper :: [Bool] -> [Bool] -> [Bool] -> Bool -> [Bool]
jk_gatehelper (j:js) (c:cs) (k:ks) q = q:(jk_gatehelper js cs ks (jkflipper j c k q))

jk_gate :: [Bool] -> [Bool] -> [Bool] -> [Bool]
jk_gate j c k = delay 0 True (delay 1 False (jk_gatehelper j c k False))

jk_gate1 :: [Bool] -> [Bool] -> [Bool] -> [Bool]
jk_gate1 j c k = delay 0 True (delay 1 False (jk_gatehelper j c k False))

jk_zipper :: (a -> b -> c -> d -> e) -> [a] -> [b] -> [c] -> [d] -> [e]
jk_zipper z (a:as) (b:bs) (c:cs) (d:ds) = z a b c d : jk_zipper z as bs cs ds

jkflipflop :: [Bool] -> [Bool] -> [Bool] -> [(Bool,Bool,Bool,Bool)]
jkflipflop j c k = let (q1,q2,q3,q4) = (delay 10 True (jk_gate j c k), delay 8 True (jk_gate q1 c q1), delay 6 True (jk_gate (and_gate q1 q2) c (and_gate q1 q2)), delay 4 True (jk_gate (and_gate (and_gate q1 q2) q3) c (and_gate (and_gate q1 q2) q3))) in jk_zipper (\p q r s -> (s,r,q,p)) q1 q2 q3 q4

jkflipflop1 :: [Bool] -> [Bool] -> [Bool] -> [(Bool,Bool,Bool,Bool)]
jkflipflop1 j c k = let (q1,q2,q3,q4) = (jk_gate1 j c k, jk_gate1 q1 c q1, jk_gate1 (and_gate1 q1 q2) c (and_gate1 q1 q2), jk_gate1 (and_gate1 (and_gate1 q1 q2) q3) c (and_gate1 (and_gate1 q1 q2) q3)) in jk_zipper (\p q r s -> (s,r,q,p)) q1 q2 q3 q4

booleanconvert a = if a==True then 1 else 0

tupleconvert (a,b,c,d) = (booleanconvert a, booleanconvert b, booleanconvert c, booleanconvert d)

display j c k = foldl (\x (p,q,r,s) -> x++[tupleconvert (p,q,r,s)]) [] (take 100 (jkflipflop j c k))

display1 j c k = foldl (\x (p,q,r,s) -> x++[tupleconvert (p,q,r,s)]) [] (take 100 (jkflipflop1 j c k))