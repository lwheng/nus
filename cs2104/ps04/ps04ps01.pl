/*

HENG LOW WEE
U096901R
Problem Set 4 Problem 1

NOTE:
To run the problem, the equation needs to be included in this file first
Go to ----->
Equation will be executed on 'consult'

I have set the size to be reduced according to the level
so the image wouldn't be too big to be viewed in the window

Level, K, is assumed to be >= 1

Test Case : fractal(g = (g;left(45);g;right(90);g;left(45);g),3,30).

*/

/*fractal(_=_, 0, L) :-
	write('forward('), write(L), writeln(')'),
	!.*/

fractal(X=(Expr),K,L) :-
	fractal(X, Expr, Expr, K, L),
	!.
	
fractal(_, _, _, 0, L) :-
	write('forward('), write(L), writeln(')'),
	!.
	
fractal(X, X, Expr, K, L) :-
	K1 is K-1,
	L1 is L*0.85^(K1),					% this line is reduce size of image
	fractal(X, Expr, Expr, K1, L1),		% use reduced size
	%fractal(X, Expr, Expr, K1, L),		% use actual size
	!.
	
fractal(_, left(Angle), _, _, _) :-
	write('left('), write(Angle), writeln(')'),
	!.

fractal(_, right(Angle), _, _, _) :-
	write('right('), write(Angle), writeln(')'),
	!.
	
fractal(X, Left;Right, Expr, K, L) :-
	fractal(X, Left, Expr, K, L),
	fractal(X, Right, Expr, K, L),
	!.
	
:- tell('output.py'),
	writeln('from turtle import *'),
	writeln('import time'),
	writeln('Screen().setworldcoordinates(-200,-500, 1500, 1000)'),
	writeln('delay(0)'),
	% -----> ENTER EQUATION HERE
 	fractal(g = (g;left(45);g;right(90);g;left(45);g),3,30),
	% After moving so much, the turtle is tired and has fallen asleep
	writeln('time.sleep(15)'),
	told,
	!.
