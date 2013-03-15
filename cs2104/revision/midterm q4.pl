% DefIn, DefOut = correctly defined variables
% IIn,IOut = incorrectly initialized variables
% UIn,UOut = incorrectly used variables
% Lev = scoping level, definitions allowed only at lev 0

:- op(950,fx,if).
:- op(949,xfx,then).
:- op(948,xfx,else).
:- op(947,fx,while).
:- op(946,xfx,do).

% checkExpr will handle either integer, a single atom or an expression 
checkExpr(N,_,InitIn,InitOut,UsedIn,UsedOut) :-
	integer(N),!.
checkExpr(V,DefIn,InitIn,InitOut,UsedIn,UsedOut) :-
	atom(V),!,
	(member(V,DefIn) % this checks whether the variable V is already defined
	-> UsedOut = UsedIn % yes, then we are done
	; member(V,UsedIn) -> UsedOut=UsedIn ; UsedOut=[V|UsedIn]
	).
checkExpr(E,DefIn,InitIn,InitOut,UsedIn,UsedOut) :-
	E =.. [Opr,Left,Right],
	member(Opr, [+,-,*,/,<,>,+=,-=,*=,/=,<=,>=,\=,==]),
	checkExpr(Left,DefIn,InitIn,InitAux,UsedIn,UsedAux),
	checkExpr(Right,DefIn,InitAux,InitOut,UsedIn,UsedOut).


check(V=E,DefIn,DefOut,InitIn,InitOut,UsedIn,UsedOut,Lev) :-
	checkExpr(E,DefIn,InitIn,InitAux,UsedIn,UsedOut),
	(Lev=0
    ->  (member(V,DefIn) -> DefIn = DefOut ; DefOut = [V|DefIn]), InitOut = InitAux
    ;   DefOut = DefIn, (member(V,InitAux) -> InitOut = InitAux ; InitOut = [V|InitAux])).

check((while E do {S1}),DefIn,DefOut,InitIn,InitOut,UsedIn,UsedOut,Lev) :-
	checkExpr(E,DefIn,InitIn,InitAux,UsedIn,UsedAux),
	L1 is Lev+1,
	check(S1,DefIn,DefOut,InitAux,InitOut,UsedAux,UsedOut,L1).

check(if E then S1 else S2,DefIn,DefOut,InitIn,InitOut,UsedIn,UsedOut,Lev) :-
	checkExpr(E,DefIn,InitIn,InitAux,UsedIn,UsedAux),
	L1 is Lev+1,
	check(S1,DefIn,DefAux,InitAux,InitAux1,UsedAux,UsedAux1,L1),
	check(S2,DefAux,DefOut,InitAux1,InitOut,UsedAux1,UsedOut,L1).

check(if E then S1,DefIn,DefOut,InitIn,InitOut,UsedIn,UsedOut,Lev) :-
	checkExpr(E,DefIn,InitIn,InitAux,UsedIn,UsedAux),
	L1 is Lev+1,
	check(S1,DefIn,DefOut,InitAux,InitOut,UsedAux,UsedOut,L1).

check((S1;S2),DefIn,DefOut,InitIn,InitOut,UsedIn,UsedOut,Lev) :-
	check(S1,DefIn,DefAux,InitIn,InitAux,UsedIn,UsedAux,Lev),
	check(S2,DefIn,DefOut,InitIn,InitOut,UsedIn,UsedOut,Lev).

check({S},DefIn,DefOut,InitIn,InitOut,UsedIn,UsedOut,Lev) :-
	L1 is Lev+1,
	check(S2,DefIn,DefOut,InitIn,InitOut,UsedIn,UsedOut,L1).

:- P = (
		x = 1
	).

initCheck(P, IncorrectInit, IncorrectUsed) :-
	check(P, [], DefOut, [], InitOut, [], UsedOut, Lev),
	writeln([d=DefOut,i=InitOut,u=UsedOut]).