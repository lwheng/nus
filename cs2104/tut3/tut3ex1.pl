% HENG LOW WEE
% U096901R
% Tut 3 Ex 1

rev([],[]).
rev([H|Tail], Result) :- rev(Tail, TailRev), append(TailRev, [H], Result).