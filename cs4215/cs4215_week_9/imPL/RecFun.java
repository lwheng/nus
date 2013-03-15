package imPL;

import java.util.*;

public class RecFun extends Fun {

	public String funVar;

	public RecFun(String f, Vector<String> xs, Expression b) {
		super(xs, b);
		funVar = f;
	}

	// //////////////////////
	// Denotational Semantics
	// //////////////////////

	// stub to be replaced by proper implementation

	public StoreAndValue eval(Store s, Environment e) {
		StoreAndValue sv = super.eval(s, e);
		int newLocation = sv.store.newLocation();
		sv.store = sv.store.extend(newLocation, sv.value);
		((FunValue) sv.value).environment = ((FunValue) sv.value).environment
				.extend(funVar, newLocation);
		return new StoreAndValue(sv.store, new FunValue(
				((FunValue) sv.value).environment, super.formals, super.body));
	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public String toString() {
		String s = "";
		for (String f : formals)
			s = s + " " + f;
		return "recfun " + funVar + s + " -> " + body + " end";
	}
}
