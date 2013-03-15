/*
Heng Low Wee
U096901R

Problem Set 3 Problem 3
*/
tile(0, In, Out) :-
	scale(In, 100, 100, Out),
	!.
	
tile(N, In, Out) :-
	N1 is N-1,
	atom_concat(Out, 't1', Out1),
	tile(N1, In, Out1),
	s(Out1, In, Out),
	!.
	
% In and Add assumed same size
% Will put them together and when done, reduce size to In.
s(In, Add, Out) :-
	atom_concat(Out, 's1', Out1),
	atom_concat(Out, 's2', Out2),
	atom_concat(Out, 's3', Out3),
	r(In, Out1),
	r(Out1, Out2),
	r(Out2, Out3),
	atom_concat(Out, 's4', Out4),
	atom_concat(Out, 's5', Out5),
	atom_concat(Out, 's6', Out6),
	atom_concat(Out, 's7', Out7),
	atom_concat(Out, 's8', Out8),
	r(Add, Out4),
	r(Out4, Out5),
	r(Out5, Out6),
	b(Out3, Out5, Out7),
	b(Add, Out4, Out8),
	atom_concat(Out, 's9', Out9),
	atom_concat(Out, 's10', Out10),
	atom_concat(Out, 's11', Out11),
	r(Out7, Out9),
	r(Out8, Out10),
	b(Out9, Out10, Out11),
	scale(Out11, 50, 50, Out),
	!.
	
b(Arg1, Arg2, Out) :-
	atom(Arg1), atom(Arg2),
	write('convert +append '),
	write(Arg1), write('.jpg '),
	write(Arg2), write('.jpg '),
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
