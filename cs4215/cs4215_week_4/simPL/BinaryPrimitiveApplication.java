package simPL;

public class BinaryPrimitiveApplication implements Expression {
	public String operator;
	public Expression argument1, argument2;

	public BinaryPrimitiveApplication(String op, Expression a1, Expression a2) {
		operator = op;
		argument1 = a1;
		argument2 = a2;
	}

	// Eliminate let for the operands
	public Expression eliminateLet() {
		return new BinaryPrimitiveApplication(operator,
				argument1.eliminateLet(), argument2.eliminateLet());
	}

	// to be implemented by student

	public Type check(TypeEnvironment G) throws TypeError {
		Type result1 = argument1.check(G);
		Type result2 = argument2.check(G);
		if (result1 instanceof IntType && result2 instanceof IntType) {
			if (operator.equals("+") || operator.equals("-")
					|| operator.equals("*") || operator.equals("/")) {
				return new IntType();
			} else if (operator.equals("<") || operator.equals(">")
					|| operator.equals("=")) {
				return new BoolType();
			} else
				throw new TypeError("Bool ops on integers, " + this);
		} else if (result1 instanceof BoolType && result2 instanceof BoolType) {
			if (operator.equals("&") || operator.equals("|")) {
				return new BoolType();
			} else
				throw new TypeError("Arithmetic ops on booleans, " + this);
		} else
			throw new TypeError("ill-typed binary primitive application "
					+ this);
	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public String toString() {
		return "(" + argument1 + " " + operator + " " + argument2 + ")";
	}
}
