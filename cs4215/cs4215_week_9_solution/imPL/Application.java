package imPL;

import java.util.*;

public class Application implements Expression {

	public Expression operator;

	public Vector<Expression> operands;

	public Application(Expression rator,Vector<Expression> rands) {
		operator = rator; operands = rands;
	}

	// //////////////////////
	// Denotational Semantics
	// //////////////////////


	public StoreAndValue eval(Store s, Environment e) throws ExceptionValue {
		StoreAndValue sav = operator.eval(s, e);
		FunValue fv = (FunValue) sav.value;
		Store newSt = sav.store;
		Vector<Integer> locs = new Vector<Integer>();
		for (Expression exp : operands) {
			StoreAndValue t = exp.eval(newSt, e);
			int newLoc = t.store.newLocation();
			newSt = t.store.extend(newLoc, t.value);
			locs.add(newLoc);
		}
		return fv.body.eval(newSt, fv.environment.extend(fv.formals, locs));
	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public String toString() {
		String s = "";
		for  (Expression operand : operands) 
			s = s + " " + operand;
		return "("+operator+" "+s+")";
	}
}
