package simPL;

import java.util.*;

public class RecFun extends Fun {

	public String funVar;

	public RecFun(String f, Type t, Vector<String> xs, Expression b) {
		super(t, xs, b);
		funVar = f;
	}

	public Expression eliminateLet() {
		return new RecFun(funVar, funType, formals, body.eliminateLet());
	}

	// to be implemented by student

	public Type check(TypeEnvironment G) throws TypeError {
		if (funType instanceof FunType) {
			G = G.extend(funVar, funType);
			if (((FunType) funType).argumentTypes.size() == formals.size()) {
				G = G.extend(formals, ((FunType) funType).argumentTypes);
				Type result1 = body.check(G);
				if (EqualType
						.equalType(((FunType) funType).returnType, result1)) {
					return funType;
				} else
					throw new TypeError("RecFun return type != fun body type, "
							+ this);
			} else
				throw new TypeError("# arguments != # formals, " + this);
		} else
			throw new TypeError("Error in RecFun definition, " + this);
	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public String toString() {
		String s = "";
		for (String f : formals)
			s = s + " " + f;
		return "recfun " + funVar + " {" + funType + "} " + s + " -> " + body
				+ " end";
	}
}
