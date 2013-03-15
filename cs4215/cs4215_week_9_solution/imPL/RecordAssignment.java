package imPL;

public class RecordAssignment implements Expression {
    public Expression recordExpression;
    public PropertyConstant property;
    public Expression rightHandSide;
    public RecordAssignment(Expression rec, PropertyConstant p, Expression rh) {
	recordExpression = rec;
	property = p;
	rightHandSide = rh;
   }

    // //////////////////////
    // Denotational Semantics
    // //////////////////////

    public StoreAndValue eval(Store s, Environment e) throws ExceptionValue {
    	   StoreAndValue s_v_1 = recordExpression.eval(s, e);
    	   StoreAndValue s_v_2 = rightHandSide.eval(s_v_1.store, e);
    	   Environment recordValue = (Environment) s_v_1.value;
    	   int l = recordValue.get(property);
    	   s_v_2.store.setElementAt(s_v_2.value, l);
	   return new StoreAndValue(s_v_2.store,s_v_2.value);
    }

    // //////////////////////
    // Support Functions
    // //////////////////////

   public String toString() {
       return "((" + recordExpression + ")."  + property 
	   + " := " + rightHandSide + ")";
   }
}
