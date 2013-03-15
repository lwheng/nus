package imPL;

import java.util.*;

public class Application implements Expression {

	public Expression operator;

	public Vector<Expression> operands;

	public Application(Expression rator, Vector<Expression> rands) {
		operator = rator;
		operands = rands;
	}

	// //////////////////////
	// Denotational Semantics
	// //////////////////////

	// stub to be replaced by proper implementation

	public StoreAndValue eval(Store s, Environment e) {
		StoreAndValue functionSV;
		FunValue fv;
		Enumeration<String> eKeys;
		String key;
		if (operator instanceof Fun) {
			functionSV = ((Fun) operator).eval(s, e);
			fv = (FunValue) functionSV.value;
			eKeys = e.keys();
			while (eKeys.hasMoreElements()) {
				key = eKeys.nextElement();
				if (!fv.environment.containsKey(key)) {
					fv.environment = fv.environment.extend(key, e.access(key));
				}
			}
			for (int i = 0; i < operands.size(); i++) {
				functionSV.store.setElementAt(operands.get(i).eval(s, e).value,
						fv.environment.access(fv.formals.get(i).toString()));
			}
			return fv.body.eval(functionSV.store, fv.environment);
		} else {
			fv = (FunValue) s.get(e.access(operator.toString()));
			return new Application(new Fun(fv.formals, fv.body), operands)
					.eval(s, e);
		}
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
