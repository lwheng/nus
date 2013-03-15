rename(V,S,R,Out) :-
	atom(V),
	(V==S
	-> Out = R
	; Out = V
	).

rename(V,_,_,Out) :-
	integer(V),
	Out = V.

rename(Expr,S,R,Out) :-
	Expr =.. [Opr, Left, Right],
	rename(Left,S,R,Out1),
	rename(Right,S,R,Out2),
	concat(Out1,Opr,OutAux),
	concat(OutAux,Out2,Out), !.

 last([Elem], Out) :- Out = Elem, !.
 last([_|Tail], Elem) :- last(Tail, Elem), !.

is_prime(2).
is_prime(3).
is_prime(P) :- integer(P), P > 3, P mod 2 =\= 0, \+ has_factor(P,3).

has_factor(N,L) :- N mod L =:= 0.
has_factor(N,L) :- L * L < N, L2 is L + 2, has_factor(N,L2).

% X is the current element we are visiting.
primeList(Primes, _, _, 0):- writeln(Primes).

primeList(In, Out, X, N) :-
	(is_prime(X) -> (append(In, [X], Out), N1 is N-1) ; (Out = In, N1 is N)),
	X1 is X + 1,
	primeList(Out, _, X1, N1 ).

primes(N) :- primeList([], _, 2, N).


reverse([],[]).
reverse([Head|Tail], Reversed) :-
	reverse(Tail, RevRest),
	append(RevRest,[Head],Reversed).

prefix_sum([],X) :- X is 0.
prefix_sum(ListIn) :-
	prefix_helper(ListIn, 0, []).
	
prefix_helper([],_,Aux) :- writeln(Aux).
prefix_helper(ListIn,Acc,ListOut) :-
	ListIn = [Head|Tail],
	Sum is Head + Acc,
	append(ListOut,[Sum],Aux),
	prefix_helper(Tail,Sum,Aux),!.

list_con([]).
list_con([_]).
list_con([H1|T1]) :-
	T1 = [H2|_],
	Val is H2-H1,
	(Val = 1
	-> list_con(T1)
	; false),!.

