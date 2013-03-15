package imPL;

public class Try implements Expression {
	public String exceptionVar;
	public Expression tryExpression;
	public Expression withExpression;
	public Try(Expression t, String ev, Expression w) {
		tryExpression = t;
		exceptionVar = ev;
		withExpression = w;
	}

	// //////////////////////
	// Denotational Semantics
	// //////////////////////

	public StoreAndValue eval(Store s, Environment e) throws ExceptionValue {
		try {
			return tryExpression.eval(s, e);
		} catch (ExceptionValue x) {
			int newLoc = s.newLocation();
			Store newStore = s.extend(newLoc, x);
			Environment newEnv = e.extend(exceptionVar, newLoc);
			return withExpression.eval(newStore, newEnv);
		}
	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public String toString() {
		return "try " + tryExpression 
				+ " catch " + exceptionVar 
				+ " with "  + withExpression + " end";
	}
}
