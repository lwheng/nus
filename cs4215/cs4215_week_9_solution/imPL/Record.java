package imPL;

import java.util.*;

public class Record implements Expression {

    public Vector<Association> associations;

    public Record(Vector<Association> as) {
	associations = as;
   }

    // //////////////////////
    // Denotational Semantics
    // //////////////////////

    public StoreAndValue eval(Store s, Environment e) throws ExceptionValue {
    	    Environment recordValue = new Environment();
    	    for (Association a : associations) {
    	    	   StoreAndValue s_v = a.expression.eval(s, e);
    	    	   s = s_v.store;
    	    	   int l = s.newLocation();
    	    	   s = s.extend(l,s_v.value);
    	    	   recordValue.extend(a.property,l);
    	    }
	    return new StoreAndValue(s,recordValue);
    }

    // //////////////////////
    // Support Functions
    // //////////////////////

   public String toString() {
       Enumeration<Association> en = associations.elements();
       String content = "";
       if (en.hasMoreElements()) {
	   Association a = (Association) en.nextElement();
	   content = content + a.property + " : " + a.expression;
	   if (en.hasMoreElements()) 
	       while (en.hasMoreElements()) 
		   content = content + " , " 
		       + ((Association) en.nextElement());
       }
       return "[ " + content + " ]";
   }
}
