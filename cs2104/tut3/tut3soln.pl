% Question 1
rev(In,Out) :- reverseHelp(In,Out,[]).

reverseHelp([],Out,Out).
reverseHelp([H|T],Out,Help):- Temp = [H|Help], reverseHelp(T,Out,Temp).

% Question 2
quicksort([],[]):-!.
quicksort([Pivot|T],Out):- split(T,Pivot,LowList,HighList), quicksort(LowList,LowSorted),quicksort(HighList,HighSorted),append(LowSorted,[Pivot],Temp),append(Temp,HighSorted,Out).


split([],_,[],[]).
split([H|T],Pivot,LowList,HighList):- split(T,Pivot,AuxLow,AuxHigh), (H >= Pivot -> LowList = AuxLow , HighList = [H|AuxHigh] ; LowList = [H|AuxLow] , HighList = AuxHigh).


% Question 3
% used texec as exec was defined.
:- op(100 ,fx, push).
texec(Command,Result):-texec(Command,[],[Result|_]).

% texec(Command , CurrentStack , NewStack).
texec(X;Y , CurrentStack, ResultStack):- texec(X,CurrentStack,TempStack),texec(Y,TempStack,ResultStack).
texec(push X, CurrentStack , [X|CurrentStack]):- !, integer(X).
texec(add , [O1,O2|T] , [Result|T]):- !, Result is O2 + O1.
texec(mul , [O1,O2|T] , [Result|T]):- !, Result is O2 * O1.
texec(sub , [O1,O2|T] , [Result|T]):- !, Result is O2 - O1.
texec(div , [O1,O2|T] , [Result|T]):- !, Result is O2 / O1.

%Question 4
% used tcompile as compile was defined.
tcompile(Expression , Out):-compileHelp(Expression,List),convert(List,Out).

compileHelp(X , [push X]) :- integer(X),!.
compileHelp(X+Y , List):- !,compileHelp(X,Xg),compileHelp(Y,Yg),append(Xg,Yg,Temp),append(Temp,[add],List).
compileHelp(X-Y , List):- !,compileHelp(X,Xg),compileHelp(Y,Yg),append(Xg,Yg,Temp),append(Temp,[sub],List).
compileHelp(X/Y , List):- !,compileHelp(X,Xg),compileHelp(Y,Yg),append(Xg,Yg,Temp),append(Temp,[div],List).
compileHelp(X*Y , List):- !,compileHelp(X,Xg),compileHelp(Y,Yg),append(Xg,Yg,Temp),append(Temp,[mul],List).

convert([Item],Item).
convert([H|T],H;Tail):-convert(T,Tail).

/*
Instead of starting with ';' , i use a list to store the ops and convert them afterward.
*/


