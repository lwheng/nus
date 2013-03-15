package oPL;

import java.util.*;

public class Record implements Expression {

    public Vector<Association> associations;

    public Record(Vector<Association> as) {
	associations = as;
   }

    // //////////////////////
    // Denotational Semantics
    // //////////////////////

	public Value eval(Environment e) {
		RecordValue rv = new RecordValue();
		for (Association association : associations) {
			Value val = association.expression.eval(e);
			Integer location = Store.theStore.newLocation();
			rv.put(association.property, location);
			Store.theStore.extend(location,val);
		}
		return rv;
	}

    // //////////////////////
    // Support Functions
    // //////////////////////

   public String toString() {
       Enumeration en = associations.elements();
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
