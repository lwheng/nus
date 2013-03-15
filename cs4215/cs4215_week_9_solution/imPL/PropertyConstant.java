package imPL;

public class PropertyConstant implements Expression {
    public String value;
    public PropertyConstant(String s) {
	value = s;
    }

    // //////////////////////
    // Denotational Semantics
    // //////////////////////

    // no need to implement

    public StoreAndValue eval(Store s, Environment e) {
	return new StoreAndValue(s,new PropertyValue(value));
    }

    // //////////////////////
    // Support Functions
    // //////////////////////

    public String toString() {
	return value;
    }
}

