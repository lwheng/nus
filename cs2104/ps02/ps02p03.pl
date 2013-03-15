/* Heng Low Wee, U096901R
Problem Set 2, Problem 3

Write a Prolog program that performs arithmetic expression simplification. 
Your program should at least eliminate multiplications by 0 and 1, and 
additions with 0. Ideally, it should also convert any subexpression 
containing only constants into the value of that expression, and convert 
multiplications with -1 into the negative of the multiplicand. 
For instance, the expression

(1+0)*(a-x)+(x+2)*(0-1)

could be simplified into:

a-x-(x+2)

Further simplifications may include distribution of high-precedence 
operators, and grouping of terms that contain the same variable. 
The expression above could be further simplified into:

a-2*x-2


READ HERE:
Sample Run...

simplify((1+0)*(a-x)+(x+2)*(0-1), C).

*/

simplify(X, Result) :- number(X), Result is X, !.
simplify(X, Result) :- atom(X), Result = X.

simplify(Expr, Result) :- 
	(Expr =.. [Operator, Left, Right]
		->	(simplify(Left, R1),
			simplify(Right, R2),
			d1(Operator, R1, R2, Result))
		;	(Expr =.. [Operator, Right],
			simplify(Right, R3),
			d2(Operator, R3, Result))
	),
	!.
	
% Additions with numbers
d1(+, DL, DR, Result1) :- number(DL), number(DR), Result1 is DL+DR, !.
% Subtraction with numbers
d1(-, DL, DR, Result1) :- number(DL), number(DR), Result1 is DL-DR, !.
% Multiplication with numbers
d1(*, DL, DR, Result1) :- number(DL), number(DR), Result1 is DL*DR, !.
% Division with numbers
d1(/, DL, DR, Result1) :- number(DL), number(DR), Result1 is DL/DR, !.

% Multiplication/Division by 0
d1(*, 0, _, Result1) :- Result1 is 0, !.
d1(*, _, 0, Result1) :- Result1 is 0, !.
d1(/, 0, _, Result1) :- Result1 is 0, !.
d1(/, _, 0, Result1) :- Result1 = infinity, !.

% Multiplication/Division by 1
d1(*, 1, DR, Result1) :- Result1 = DR, !.
d1(*, DL, 1, Result1) :- Result1 = DL, !.
d1(/, 1, DR, Result1) :- (number(DR) -> Result1 is 1/DR ; Result1 = 1/DR), !.
d1(/, DL, 1, Result1) :- Result1 = DL, !.

% Multiplication/Division by -1
d1(*, -1, DR, Result1) :- Result1 = -DR, !.
d1(*, DL, -1, Result1) :- Result1 = -DL, !.
d1(/, -1, DR, Result1) :- (number(DR) -> Result1 is -1/DR; Result1 = -1/DR), !.
d1(/, DL, -1, Result1) :- Result1 = -DL, !.

% Additions with 0
d1(+, 0, DR, Result1) :- Result1 = DR, !.
d1(+, DL, 0, Result1) :- Result1 = DL, !.
% Subtraction with 0
d1(-, 0, DR, Result1) :- Result1 = -DR, !.
d1(-, DL, 0, Result1) :- Result1 = DL, !.

% Additions with variables
d1(+, DL, DR, Result1) :- DR =.. [-, Tail], Result1 = DL-Tail, !.
d1(+, DL, DR, Result1) :- DR =.. [_, _, _], Result1 = DL+DR, !.
d1(+, DL, DR, Result1) :- DL=DR, Result1 = 2*DL, !.
d1(+, DL, DR, Result1) :- Result1 = DL+DR, !.
	
% Subtraction with variables
d1(-, DL, DR, Result1) :- DR =.. [-, Tail], Result1 = DL+Tail, !.
d1(-, DL, DR, Result1) :- DR =.. [_, _, _], Result1 = DL-DR, !.
d1(-, DL, DR, Result1) :- DL=DR, Result1 is 0, !.
d1(-, DL, DR, Result1) :- Result1 = DL-DR, !.
	
% Multiplication with variables
d1(*, DL, DR, Result1) :- ((number(DL),atom(DR)) -> Result1 = DL*DR ; Result1 = DR*DL), !.

% Division with variables
d1(/, DL, DR, Result2) :- atom(DL), number(DR), Result2 = Result1 + DL, Result1 is 1/DR, !.
d1(/, DL, DR, Result1) :- (DL=DR -> Result1 is 1 ; Result1 = DL/DR), !.

d2(-, Term, Result1) :- number(Term), Result1 is -Term, !.
d2(-, Term, Result1) :- Result1 = -Term, !.