package imPL;

public class BinaryPrimitiveApplication implements Expression {
	public String operator;
	public Expression argument1,argument2;

	public BinaryPrimitiveApplication(String op, Expression a1, Expression a2) {
		operator = op;
		argument1 = a1;
		argument2 = a2;
	}

	// //////////////////////
	// Denotational Semantics
	// //////////////////////

	public StoreAndValue eval(Store s, Environment e) throws ExceptionValue {
		StoreAndValue s_and_v_1 = argument1.eval(s, e);
		StoreAndValue s_and_v_2 = argument2.eval(s_and_v_1.store, e);
		
		if (operator.equals(".") || 
				operator.equals("hasproperty")) {
			   Environment recordValue = (Environment) s_and_v_1.value;
			   PropertyValue propValue = (PropertyValue) s_and_v_2.value;
			   return new StoreAndValue(
					s_and_v_2.store,
					(operator.equals(".")) ?
							s_and_v_2.store.get(recordValue.access(propValue.value))
							: new BoolValue(recordValue.containsKey(propValue.value)));
			  
			
		} else if (operator.equals("|") ||
				operator.equals("&")) {
			BoolValue v_1 = (BoolValue) s_and_v_1.value;
			BoolValue v_2 = (BoolValue) s_and_v_2.value;
			return new StoreAndValue(
					s_and_v_2.store,
					(operator.equals("|")) ?
							new BoolValue(v_1.value || v_2.value)
					:       new BoolValue(v_1.value && v_2.value)
					);
		} 
		else {
			IntValue v_1 = (IntValue) s_and_v_1.value;
			IntValue v_2 = (IntValue) s_and_v_2.value;
			return new StoreAndValue(
					s_and_v_2.store,
					(operator.equals("+")) 
					? new IntValue(v_1.value + v_2.value)
					: (operator.equals("-")) 
					? new IntValue(v_1.value - v_2.value)
					: (operator.equals("/")) // ignoring division by zero
					? new IntValue(v_1.value / v_2.value)
					: (operator.equals("*")) 
					? new IntValue(v_1.value * v_2.value)
					: (operator.equals("=")) 
					? new BoolValue(v_1.value == v_2.value)
					: (operator.equals(">")) 
					? new BoolValue(v_1.value > v_2.value)
					: new BoolValue(v_1.value < v_2.value) 
					// here: operator.equals("<")) 
					);

		}
	}


// //////////////////////
// Support Functions
// //////////////////////

public String toString() {
	return "(" + argument1 + " " + operator + " " + argument2 + ")";
}
}

