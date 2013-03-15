/* Heng Low Wee U096901R
Problem Set 2, Problem 1

Write a Prolog predicate definition that counts the number of occurrences of 
an operator inside an expression. For instance, the query:
?- count(a+b*c-(2+3*4)/(5*(2+a)+(b+c)^f((d-e)*(x-y))), *, C).
would count the number of occurrences of operator * in the expression 
given as the first argument. The return value should be bound to the 
variable passed as the third argument. For the query given above, the 
answer should be C=4.

*/

count(Expr, Op, C) :-
	(Expr =.. [Operator, LeftOperand, RightOperand]
		->	(count(LeftOperand, Op, C1),
			count(RightOperand, Op, C2),
			(Operator = Op 
				-> C is C1+C2+1
				; C is C1+C2
				),
			!)
		;	(Expr =.. [Operator, LeftOperand],
			count(LeftOperand, Op,C1),
			(Operator = Op 
				-> C is C1+1
				; C is C1
				),
			!)
	).
	
count(Expr, _, 0) :- atomic(Expr) ; number(Expr).
