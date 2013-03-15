/*
Heng Low Wee
U096901R

Problem Set 3 Problem 2
*/

ma(Expr) :-
	Expr =.. [;, Left, Right],
	Left =.. [=, X, Cmd],
	montage(Cmd, X),
	ma(Right),
	!.
	
ma(Expr) :-
	Expr =.. [=, X, Cmd],
	montage(Cmd, X),
	!.

montage(Expr, Out) :-
	atom(Expr),
	scale(Expr, 100, 100, Out),
	!.
	
montage(Expr, Out) :-
	Expr =.. [rotate, Arg1],
	atom(Arg1),
	r(Arg1, Out),
	!.
	
montage(Expr, Out) :-
	Expr =.. [rotate, Arg1],
	atom_concat(Out, '3', Out1),
	montage(Arg1, Out1),
	r(Out1, Out),
	!.
	
montage(Expr, Out) :-
	Expr =.. [beside, Arg1, Arg2],
	atom(Arg1), atom(Arg2),
	b(Arg1, Arg2, Out),
	!.
	
montage(Expr, Out) :-
	Expr =.. [beside, Arg1, Arg2],
	atom_concat(Out, '4', Out1),
	atom_concat(Out, '5', Out2),
	montage(Arg1, Out1),
	montage(Arg2, Out2),
	b(Out1, Out2, Out),
	!.
	
b(Arg1, Arg2, Out) :-
	atom(Arg1), atom(Arg2),
	atom_concat(Out, '1', Out1),
	atom_concat(Out, '2', Out2),
	scale(Arg1, 50, 100, Out1),
	scale(Arg2, 50, 100, Out2),
	write('convert +append '),
	write(Out1), write('.jpg '),
	write(Out2), write('.jpg '),
	write(Out), write('.jpg'),
	writeln(''),
	!.
	
r(Arg1, Out) :-
	atom(Arg1),
	write('convert -rotate 90 '),
	write(Arg1), write('.jpg '),
	write(Out), write('.jpg '),
	writeln(''),
	!.
	
scale(Arg1, Width, Height, Out) :-
	atom(Arg1),
	write('convert -scale '),
	write(Width), write('%%x'),
	write(Height), write('%% '),
	write(Arg1), write('.jpg '),
	write(Out), write('.jpg'),
	writeln(''),
	!.
