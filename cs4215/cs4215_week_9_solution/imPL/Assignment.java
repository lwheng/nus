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

    public StoreAndValue eval(Store s, Environment e) throws ExceptionValue {
    	    StoreAndValue s_and_v = rightHandSide.eval(s, e);
    	    s_and_v.store.setElementAt(s_and_v.value,
                                   e.access(leftHandSide));
	    return new StoreAndValue(s_and_v.store,
	    						    s_and_v.value);
    }

    // //////////////////////
    // Support Functions
    // //////////////////////

   public String toString() {
       return "(" + leftHandSide + " := " + rightHandSide + ")";
   }
}
