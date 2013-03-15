package imPL;

import java.util.*;

public class Let implements Expression {

	public Vector<LetDefinition> definitions;

	public Expression body;

	public Let(Vector<LetDefinition> ds, Expression b) {
		definitions = ds;
		body = b;
	}

	// //////////////////////
	// Denotational Semantics
	// //////////////////////

	// stub to be replaced by proper implementation

	public StoreAndValue eval(Store s, Environment e) {
		StoreAndValue rhs;
		int newLocation;
		for (LetDefinition ld : definitions) {
			rhs = ld.rightHandExpression.eval(s, e);
			newLocation = rhs.store.newLocation();
			s = rhs.store.extend(newLocation, rhs.value);
			e = e.extend(ld.variable, newLocation);
		}
		return this.body.eval(s, e);
	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public String toString() {
		String s = "";
		for (LetDefinition d : definitions)
			s = s + " " + d;
		return "let " + s + " in " + body + " end";
	}
}
