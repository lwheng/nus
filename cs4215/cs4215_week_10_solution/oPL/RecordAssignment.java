package oPL;

public class RecordAssignment implements Expression {
	public Expression recordExpression;
	public Expression propertyExpression;
	public Expression rightHandSide;
	public RecordAssignment(Expression rec, Expression p, Expression rh) {
		recordExpression = rec;
		propertyExpression = p;
		rightHandSide = rh;
	}

	// //////////////////////
	// Denotational Semantics
	// //////////////////////

	public Value eval(Environment e) {
		RecordValue sv1 = (RecordValue) recordExpression.eval(e);
		PropertyValue sv2 = (PropertyValue) propertyExpression.eval(e);
		Value sv3 = rightHandSide.eval(e);
		if (sv1.containsKey(sv2.value)) { 
			Store.theStore.set(sv1.get(sv2.value),sv3);
		} else {
			Integer location = Store.theStore.newLocation();
			sv1.put(sv2.value,location);
			Store.theStore.extend(location,sv3);
		}
		return sv3;
	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public String toString() {
		return "((" + recordExpression + ").("  + propertyExpression
		+ ") := " + rightHandSide + ")";
	}
}
