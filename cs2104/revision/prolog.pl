:- op(950,fy,++#).
:- op(949,yf,#++).
:- op(949,yf,;).

% translate(Expr,FinalOut) :-
% 	helper(Expr,[],PreOut,[],PostOut,FinalIn,FinalOut).

translate(V:=E,PreIn,PreOut,PostIn,PostOut,FinalIn,FinalOut) :-
	append(FinalIn,[V],FinalAux),
	sub(E,FinalAux,FinalAux1),
	helper(E,PreIn,PreOut,PostIn,PostOut,FinalAux1,FinalAux2),
	append([PreOut,FinalAux2,PostOut],FinalOut).

sub(E1*E2,In,Out) :-
	sub(E1,In,Aux),
	append(Aux,[*],Aux1),
	sub(E2,Aux1,Out),!.
	
sub(E,In,Out) :-
	E=.. [_,V],
	append(In,[V],Out).
	
helper(E1*E2,PreIn,PreOut,PostIn,PostOut,FinalIn,FinalOut) :-
	helper(E1,PreIn,PreAux,PostIn,PostAux,FinalIn,FinalAux),
	helper(E2,PreAux,PreOut,PostAux,PostOut,FinalAux,FinalOut),!.

helper(E,PreIn,PreOut,PostIn,PostOut,FinalIn,FinalOut) :-
	E =.. [Op, V],
	(Op = ++#
	-> append(PreIn,[V:=V+1;],PreOut), PostOut=PostIn, FinalOut=FinalIn
	; (Op = #++
		-> append(PostIn,[V:=V+1;],PostOut), PreOut=PreIn, FinalOut=FinalIn
		)
	).