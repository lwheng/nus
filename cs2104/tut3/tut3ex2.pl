qsort([], Results) :- Results = [].
qsort([X|Xs], Results) :-
	part(X, Xs, Left, Right),
	qsort(Left, R1), qsort(Right, R2),
	append(R1, [X|R2], Results).

part(_,[],[],[]) :- !.
part(P, [X|Xs], Left, [X|Right]) :- X>=P, !,part(P,Xs,Left,Right).
part(P, [X|Xs], [X|Left], Right) :- X<P, part(P,Xs,Left,Right).