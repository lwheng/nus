package simPL;

import java.util.*;

public class RecFun extends Fun {

    public String funVar;

    public RecFun(String f, Type t, Vector<String> xs, Expression b) {
	super(t,xs,b);
	funVar = f;
    }

    public Expression eliminateLet() {
	return new RecFun(funVar,funType,formals,body.eliminateLet());
    }

    // to be implemented by student
    
    public Type check(TypeEnvironment G) throws TypeError {
       	if (formals.contains(funVar)) {
    	    throw new TypeError("function variable "+funVar+" also appears as parameter");
    	}
    	return super.check(G.extend(funVar,funType));
    }

    // //////////////////////
    // Support Functions
    // //////////////////////

   public String toString() {
      String s = "";
      for (String f : formals)
	  s = s + " " + f;
      return "recfun " + funVar + " {" + funType + "} " + 
	  s + " -> " + body + " end";
   }
}
