/* Heng Low Wee U096901R
Problem Set 2, Problem 2

Consider the language of arithmetic expressions with 
the operators +, -, *, and /, where the operands are either 
numeric constants, or Prolog atoms denoting mathematical variables. 
Thus, the Prolog term:

(x+2)*(a-x)

stands for the mathematical function f(x)=(x+2)(a-x), 
where a would be symbolic constant. Write a Prolog program that 
computes the derivative of such an expression. For instance, the
query:

?- derive((x+2)*(a-x),x,D).

should derive the expression given as the first argument with 
respect to the variable given as the second argument. The result 
should be placed in the variable given as the third argument. 
For the query above, the answer should be

C = (1+0)*(a-x)+(x+2)*(0-1)

Do not perform any arithmetic simplification in your solution 
to this problem. Simplification is the topic of the next problem.

*/

derive(Expr, Wrt, Result) :-
	Expr =.. [Operator, Left, Right],
	derive(Left, Wrt, R1),
	derive(Right, Wrt, R2),
	d(Operator, Left, Right, R1, R2, Result), !.
	
% Base cases
derive(Expr, _, 0) :- number(Expr), !.
derive(Expr*Wrt, Wrt, Expr) :- number(Expr), !.
derive(Wrt*Expr, Wrt, Expr) :- number(Expr), !.
derive(Expr, Wrt, 0) :- Expr \= Wrt, !.
derive(Expr, Wrt, 1) :- Expr = Wrt, !.

d(+, _, _, DL, DR, Result1) :- Result1 = DL + DR, !.
d(-, _, _, DL, DR, Result1) :- Result1 = DL - DR, !.
d(*, L, R, DL, DR, Result1) :- Result1 = DL * R + L * DR, !.
d(/, L, R, DL, DR, Result1) :- Result1 = (DL * R - L * DR)/(R*R), !.
