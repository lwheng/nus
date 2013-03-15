package ePLdynamic;

import ePL.*;

class Evaluator {

    // evaluate repeatedly implements the evaluation relation
    // by repeatedly calling oneStep until the expression has
    // no redex any longer
    static public Expression evaluate(Expression exp)  {
	return reducible(exp) ? 
	    evaluate(oneStep(exp))
	    : exp;
    }

    // in ePL, a redex is a PrimitiveApplication that has
    // a redex in one of its arguments, or that has constants
    // of the right type as arguments

    static private boolean reducible(Expression exp) {
	if (exp instanceof UnaryPrimitiveApplication) 
	    return
	    // the operator must be \, that's the only unary operator in this language
	    reducible(((UnaryPrimitiveApplication)exp).argument)
	    ||
		((UnaryPrimitiveApplication)exp).argument instanceof BoolConstant;

	else if (exp instanceof BinaryPrimitiveApplication) 
	    return
		reducible(((BinaryPrimitiveApplication)exp).argument1)
		||
		reducible(((BinaryPrimitiveApplication)exp).argument2)
		||
		// integer ops without division
		(
		 ((BinaryPrimitiveApplication)exp).operator.equals("+") 
		 ||
		 ((BinaryPrimitiveApplication)exp).operator.equals("*") 
		 ||
		 ((BinaryPrimitiveApplication)exp).operator.equals("-") 
		 ||
		 ((BinaryPrimitiveApplication)exp).operator.equals("=") 
		 ||
		 ((BinaryPrimitiveApplication)exp).operator.equals("<") 
		 ||
		 ((BinaryPrimitiveApplication)exp).operator.equals(">") 
		 )
		&&
		((BinaryPrimitiveApplication)exp).argument1 instanceof IntConstant
		&&
		((BinaryPrimitiveApplication)exp).argument2 instanceof IntConstant
		||
		// division (exclude second argument 0)
		((BinaryPrimitiveApplication)exp).operator.equals("/") 
		&&
		((BinaryPrimitiveApplication)exp).argument1 instanceof IntConstant
		&&
		((BinaryPrimitiveApplication)exp).argument2 instanceof IntConstant
		&&
		! ((IntConstant)(((BinaryPrimitiveApplication)exp).argument2))
		  .value.equals("0")
		||
		// binary bool ops
		(
		 ((BinaryPrimitiveApplication)exp).operator.equals("|") 
		 ||
		 ((BinaryPrimitiveApplication)exp).operator.equals("&") 
		 )
		&&
		((BinaryPrimitiveApplication)exp).argument1 instanceof BoolConstant
		&&
		((BinaryPrimitiveApplication)exp).argument2 instanceof BoolConstant
		;
	else
	    return false;
    }

    static private Expression oneStep(Expression exp) {
    	if (exp instanceof UnaryPrimitiveApplication) {
    		String operator = ((UnaryPrimitiveApplication)exp).operator;
    		Expression argument = ((UnaryPrimitiveApplication)exp).argument;
    		if (reducible(argument)) {
    			return new
    					UnaryPrimitiveApplication(operator,oneStep(argument));
    		}
    			else {
    				return new BoolConstant(Boolean.toString(
    					! (((BoolConstant)argument).value.equals("true"))));
    			}
    	}
    	else {
	String operator = ((BinaryPrimitiveApplication)exp).operator;
	Expression firstArg = ((BinaryPrimitiveApplication)exp).argument1;
	Expression secondArg = ((BinaryPrimitiveApplication)exp).argument2;
	if (reducible(firstArg)) {
	    return new
	    BinaryPrimitiveApplication(operator,oneStep(firstArg),secondArg);
	} else if (reducible(secondArg)) {
	    return new 
	    BinaryPrimitiveApplication(operator,firstArg,oneStep(secondArg));
	} else if (operator.equals("+")) {
	    return new 
		IntConstant(Integer.toString(Integer
					     .parseInt(((IntConstant)firstArg)
						       .value)
					     +
					     Integer
					     .parseInt(((IntConstant)secondArg)
						       .value)));
	} else if (operator.equals("-")) {
	    return new 
		IntConstant(Integer.toString(Integer
					     .parseInt(((IntConstant)firstArg)
						       .value)
					     -
					     Integer
					     .parseInt(((IntConstant)secondArg)
						       .value)));
	} else if (operator.equals("*")) {
	    return new 
		IntConstant(Integer.toString(Integer
					     .parseInt(((IntConstant)firstArg)
						       .value)
					     *
					     Integer
					     .parseInt(((IntConstant)secondArg)
						       .value)));
	} else if (operator.equals("/")) {
	    return new 
		IntConstant(Integer.toString(Integer
					     .parseInt(((IntConstant)firstArg)
						       .value)
					     /
					     Integer
					     .parseInt(((IntConstant)secondArg)
						       .value)));
	} else if (operator.equals("=")) {
	    return new 
		BoolConstant(Boolean.toString(Integer
					      .parseInt(((IntConstant)firstArg)
							.value)
					      ==
					      Integer
					      .parseInt(((IntConstant)secondArg)
							.value)));
	} else if (operator.equals("<")) {
	    return new 
		BoolConstant(Boolean.toString(Integer
					     .parseInt(((IntConstant)firstArg)
						       .value)
					     <
					     Integer
					     .parseInt(((IntConstant)secondArg)
						       .value)));
	} else if (operator.equals(">")) {
	    return new 
		BoolConstant(Boolean.toString(Integer
					     .parseInt(((IntConstant)firstArg)
						       .value)
					     >
					     Integer
					     .parseInt(((IntConstant)secondArg)
						       .value)));
	} else if (operator.equals("&")) {
	    return new 
		BoolConstant(Boolean.toString(((BoolConstant)firstArg)
					      .value.equals("true")
					      &&
					      ((BoolConstant)secondArg)
					      .value.equals("true")));
	} else // (operator.equals("|")) {
	    return new 
		BoolConstant(Boolean.toString(((BoolConstant)firstArg)
					      .value.equals("true")
					      ||
					      ((BoolConstant)secondArg)
					      .value.equals("true")));
	}
    }
}

