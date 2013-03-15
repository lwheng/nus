package imPL;

public class Assignment implements Expression {
	public String leftHandSide;
	public Expression rightHandSide;

	public Assignment(String l, Expression r) {
		leftHandSide = l;
		rightHandSide = r;
	}

	// //////////////////////
	// Denotational Semantics
	// //////////////////////

	// stub to be replaced by proper implementation

	public StoreAndValue eval(Store s, Environment e) {
		StoreAndValue sv = rightHandSide.eval(s, e);
		sv.store.setElementAt(sv.value, e.access(this.leftHandSide));
		return sv;
	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public String toString() {
		return "(" + leftHandSide + " := " + rightHandSide + ")";
	}
}
