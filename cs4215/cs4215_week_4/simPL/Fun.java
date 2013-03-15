package simPL;

import java.util.*;

public class Fun implements Expression {

	public Type funType;
	public Vector<String> formals;
	public Expression body;

	public Fun(Type t, Vector<String> xs, Expression b) {
		funType = t;
		formals = xs;
		body = b;
	}

	public Expression eliminateLet() {
		return new Fun(funType, formals, body.eliminateLet());
	}

	// to be implemented by student

	public Type check(TypeEnvironment G) throws TypeError {
		if (funType instanceof FunType) {
			if (((FunType) funType).argumentTypes.size() == formals.size()) {
				G = G.extend(formals, ((FunType) funType).argumentTypes);
				Type result1 = body.check(G);
				if (EqualType
						.equalType(((FunType) funType).returnType, result1)) {
					return funType;
				} else
					throw new TypeError("Fun return type != fun body type, "
							+ this);
			} else
				throw new TypeError("# arguments != # formals, " + this);
		} else
			throw new TypeError("Error in Fun definition, " + this);
	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public String toString() {
		String s = "";
		for (String f : formals)
			s = s + " " + f;
		return "fun {" + funType + "}" + s + " -> " + body + " end";
	}
}
