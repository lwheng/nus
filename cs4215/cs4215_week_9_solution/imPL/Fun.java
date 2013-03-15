package imPL;

import java.util.*;

public class Fun implements Expression {

    public Vector<String> formals;
    public Expression body;

    public Fun(Vector<String> xs, Expression b) {
       formals = xs;
       body = b;
    }

    // //////////////////////
    // Denotational Semantics
    // //////////////////////

	public StoreAndValue eval(Store s, Environment e) {
		return new StoreAndValue(s,new FunValue(e,formals,body));
	}

    // //////////////////////
    // Support Functions
    // //////////////////////

   public String toString() {
      String s = "";
      for (String f : formals)
	  s = s + " " + f;
      return "fun" + 
	  s + " -> " + body + " end";
   }
}
