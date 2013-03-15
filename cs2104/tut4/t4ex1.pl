/*
HENG LOW WEE
U096901R
Tut 4 Ex 1

<S> ::=  ‘(‘ <A> ‘)’
<A> ::=  ‘[‘ <A> ‘]’
		| ‘{‘ <A> ‘}’ <S>
		| a | ... | z

*/

s(S) :- append(["(", S1, ")"], S), a(S1), !.
a(S) :- append(["[", S1, "]"], S), a(S1), !.
a(S) :- append(["{", S1, "}", S2], S), a(S1), s(S2), !.
a([S]) :- (97 =< S, S =< 122), !.