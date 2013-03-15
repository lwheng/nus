package imPL;

import java.util.*;

public class Let implements Expression {

    public Vector<LetDefinition> definitions;

    public Expression body;

    public Let(Vector<LetDefinition> ds, Expression b) {
	definitions = ds;
	body = b;
    }

    // //////////////////////
    // Denotational Semantics
    // //////////////////////

    // Solution by Bi Ran

	public StoreAndValue eval(Store s, Environment e) throws ExceptionValue {
		Vector<String> vars=new Vector<String> ();
		Vector<Integer> locs=new Vector<Integer> ();
		
		for (LetDefinition l : definitions) {
			StoreAndValue sav = l.rightHandExpression.eval(s, e);
			int newLoc = sav.store.newLocation();
			s = sav.store.extend(newLoc, sav.value);
			vars.add(l.variable);
			locs.add(newLoc);
		}
		
		return body.eval(s, e.extend(vars, locs));
	}

    // //////////////////////
    // Support Functions
    // //////////////////////

   public String toString() {
       String s = "";
       for (LetDefinition d : definitions)
	   s = s + " " + d;
       return "let " + s + " in " + body + " end";
   }
}
