writer('+') :- !,
	writeln('add ;').
writer('-') :- !,
	writeln('sub ;').
writer('*') :- !,
	writeln('mul ;').
writer('/') :- !,
	writeln('div ;').

compile(Expr) :- integer(Expr),
	write('push '), write(Expr), writeln(' ;'), !.

compile(Expr) :-
	Expr =.. [Op, Left, Right],
	integer(Left), integer(Right),
	write('push '), write(Left), writeln(' ;'),
	write('push '), write(Right), writeln(' ;'),
	writer(Op), !.

compile(Expr) :-
	Expr =.. [Op, Left, Right],
	compile(Left),
	compile(Right),
	writer(Op).