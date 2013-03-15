package simPL;

import java.util.*;

public class Application implements Expression {

	public Expression operator;

	public Vector<Expression> operands;

	public Application(Expression rator, Vector<Expression> rands) {
		operator = rator;
		operands = rands;
	}

	public Expression eliminateLet() {
		Vector<Expression> newoperands = new Vector<Expression>();
		for (Expression operand : operands)
			newoperands.add(operand.eliminateLet());
		return new Application(operator.eliminateLet(), newoperands);
	}

	// to be implemented by student

	public Type check(TypeEnvironment G) throws TypeError {
		FunType result1 = (FunType) operator.check(G);
		Vector<Type> operandsResults = new Vector<Type>();
		for (Expression o : operands) {
			operandsResults.add(o.check(G));
		}
		if (result1.argumentTypes.size() == operandsResults.size()) {
			for (int i = 0; i < operandsResults.size(); i++) {
				if (!EqualType.equalType(result1.argumentTypes.get(i),
						operandsResults.get(i))) {
					throw new TypeError(
							"Application arguments don't match function "
									+ this);
				}
			}
			return result1.returnType;
		} else
			throw new TypeError("ill-typed Fun application " + this);
	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public String toString() {
		String s = "";
		for (Expression operand : operands)
			s = s + " " + operand;
		return "(" + operator + " " + s + ")";
	}
}
