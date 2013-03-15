:-op(100,fx,push).
exec(Expr, Result) :- exec(Expr, [], [Result|_]).

exec(X;Y, CurrentStack, ResultStack) :-
	exec(X, CurrentStack, TempStack),
	exec(Y, TempStack, ResultStack).

exec(push X, CurrentStack, [X|CurrentStack]) :- !, integer(X).
exec(add, [Item1, Item2|Tail], [R|Tail]) :- !,R is Item1+Item2.
exec(sub, [Item1, Item2|Tail], [R|Tail]) :- !,R is Item1-Item2.
exec(mul, [Item1, Item2|Tail], [R|Tail]) :- !,R is Item1*Item2.
exec(div, [Item1, Item2|Tail], [R|Tail]) :- !,R is Item1/Item2.