package imPL;

import java.util.*;

public class Fun implements Expression {

	public Vector<String> formals;
	public Expression body;

	public Fun(Vector<String> xs, Expression b) {
		formals = xs;
		body = b;
	}

	// //////////////////////
	// Denotational Semantics
	// //////////////////////

	// stub to be replaced by proper implementation

	public StoreAndValue eval(Store s, Environment e) {
		int newLocation;
		Environment funE = new Environment();
		for (String v : formals) {
			newLocation = s.newLocation();
			s = s.extend(newLocation, null);
			funE = funE.extend(v, newLocation);
		}
		return new StoreAndValue(s, new FunValue(funE, formals, body));
	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public String toString() {
		String s = "";
		for (String f : formals)
			s = s + " " + f;
		return "fun" + s + " -> " + body + " end";
	}
}
