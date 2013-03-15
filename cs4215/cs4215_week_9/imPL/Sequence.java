package imPL;

public class Sequence implements Expression {
	public Expression firstPart, secondPart;

	public Sequence(Expression f, Expression s) {
		firstPart = f;
		secondPart = s;
	}

	// //////////////////////
	// Denotational Semantics
	// //////////////////////

	// stub to be replaced by proper implementation

	public StoreAndValue eval(Store s, Environment e) {
		StoreAndValue first = firstPart.eval(s, e);
		StoreAndValue second = secondPart.eval(first.store, e);
		return new StoreAndValue(second.store, second.value);
	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public String toString() {
		return "(" + firstPart + " ; " + secondPart + ")";
	}
}
