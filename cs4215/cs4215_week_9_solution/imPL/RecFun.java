package imPL;

import java.util.*;

public class RecFun extends Fun {

	public String funVar;

	public RecFun(String f, Vector<String> xs, Expression b) {
		super(xs,b);
		funVar = f;
	}

	// //////////////////////
	// Denotational Semantics
	// //////////////////////

	// Solution by Bi Ran

	public StoreAndValue eval(Store s, Environment e) {
		int newLoc = s.newLocation();
		FunValue fv = new FunValue(e.extend(funVar, newLoc), formals, body);
		Store newStore = s.extend(newLoc, fv);
		return new StoreAndValue(newStore, fv);
	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public String toString() {
		String s = "";
		for (String f : formals)
			s = s + " " + f;
		return "recfun " + funVar + 
				s + " -> " + body + " end";
	}
}
