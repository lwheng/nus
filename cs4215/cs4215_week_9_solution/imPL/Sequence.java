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

    public StoreAndValue eval(Store s, Environment e) throws ExceptionValue {
    	    StoreAndValue s_and_v_1 = firstPart.eval(s, e);
    	    return secondPart.eval(s_and_v_1.store,e);
    }

    // //////////////////////
    // Support Functions
    // //////////////////////

   public String toString() {
       return "(" + firstPart + " ; " + secondPart + ")";
   }
}
