package imPL;

public class Throw implements Expression {

    public Expression expression;

    public Throw(Expression e) {
      expression = e;
   }

    // //////////////////////
    // Denotational Semantics
    // //////////////////////

    public StoreAndValue eval(Store s, Environment e) throws ExceptionValue {
	    throw new ExceptionValue(expression.eval(s, e).value);
    }

    // //////////////////////
    // Support Functions
    // //////////////////////

   public String toString() {
       return "throw " + expression + " end";
   }
}
