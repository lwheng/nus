package simPL;

import java.util.*;

public class Application implements Expression {

	public Expression operator;

	public Vector<Expression> operands;

	public Application(Expression rator, Vector<Expression> rands) {
		operator = rator;
		operands = rands;
	}

	// stub; to be replaced by student
	public Expression eliminateLet() {
		Vector<Expression> newOperands = new Vector<Expression>();
		for (Expression o : operands) {
			newOperands.add(o.eliminateLet());
		}
		return new Application(operator.eliminateLet(), newOperands);
	}

	// //////////////////////
	// Dynamic Semantics
	// //////////////////////

	// stub; to be replaced by student
	public StringSet freeVars() {
		StringSet x = operator.freeVars();
		for (Expression o : operands) {
			x.union(o.freeVars());
		}
		return x;
	}

	// stub; to be replaced by student
	public Expression substitute(String var, Expression replacement) {
		Vector<Expression> newOperands = new Vector<Expression>();
		for (Expression o : operands) {
			newOperands.add(o.substitute(var, replacement));
		}
		return new Application(operator.substitute(var, replacement),
				newOperands);
	}

	// stub; to be replaced by student
	public boolean reducible() {
		// return operator.reducible() || operandsReducible()
		// || ((IsValue.isValue(operator)) && !operandsReducible());
		return operator.reducible()
				|| (IsValue.isValue(operator) && operandsReducible())
				|| (IsValue.isValue(operator) && operandsAreValues());
	}

	// stub; to be replaced by student
	public Expression oneStep() {
		if (operator.reducible()) {
			return new Application(operator.oneStep(), operands);
		} else if (operandsReducible()) {
			return new Application(operator, reduceOperandsByOneStep());
		} else if (operandsAreValues()) {
			if (operator instanceof RecFun) {
				Expression newBody = ((RecFun) operator).body;
				newBody = newBody.substitute(((RecFun) operator).funVar,
						operator);

				for (int i = 0; i < ((RecFun) operator).formals.size(); i++) {
					newBody = newBody.substitute(((RecFun) operator).formals
							.get(i).toString(), operands.get(i));
				}
				return newBody;

			} else {
				Expression newBody = ((Fun) operator).body;
				for (int i = 0; i < ((Fun) operator).formals.size(); i++) {
					newBody = newBody.substitute(((Fun) operator).formals
							.get(i).toString(), operands.get(i));
				}
				return newBody;
			}
		} else {
			return this;
		}
	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public boolean operandsReducible() {
		for (Expression o : operands) {
			if (o.reducible()) {
				return true;
			} else if (!IsValue.isValue(o)) {
				return false;
			}
		}
		return false;
	}

	public boolean operandsAreValues() {
		for (Expression o : operands) {
			if (!IsValue.isValue(o)) {
				return false;
			}
		}
		return true;
	}

	public Vector<Expression> reduceOperandsByOneStep() {
		Vector<Expression> newOperands = operands;
		for (int i = 0; i < newOperands.size(); i++) {
			if (newOperands.get(i).reducible()) {
				newOperands.set(i, newOperands.get(i).oneStep());
			} else if (!IsValue.isValue(newOperands.get(i))) {
				return newOperands;
			}
		}
		return newOperands;
	}

	public String toString() {
		String s = "";
		for (Expression operand : operands)
			s = s + " " + operand;
		return "(" + operator + " " + s + ")";
	}

	public String toXML() {
		String s = "";
		for (Expression operand : operands)
			s = s + operand.toXML();
		return "<simpl:application>\n" + "<simpl:operator>\n"
				+ operator.toXML() + "</simpl:operator>\n"
				+ "<simpl:arguments>\n" + s + "</simpl:arguments>\n"
				+ "</simpl:application>\n";
	}
}
