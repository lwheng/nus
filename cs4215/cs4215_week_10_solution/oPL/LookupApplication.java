// submitted by JULIANA UNG BEE CHIN

package oPL;

import java.util.*;

public class LookupApplication extends Application {
    
    private FunValue cachedMethod;
    private RecordValue cachedClass;

    public LookupApplication(Expression rator,Vector<Expression> rands) {
		super(rator,rands);
		cachedMethod = null;
		cachedClass = null;
    }
    
    // JU
    // 0. Definition of lookup function: fun object methodname -> ... end
    // 1. The lookup function always returns a FunValue.
    // 2. The lookup function returns the cached FunValue if
    //    the class of the first argument (param object) is the same
    //    as cached class, otherwise
    // 3. it looks up class hierarchy to determine the correct
    //    FunValue to return
	public Value eval(Environment e) {
		// Get the class of the object
		Expression obj = operands.elementAt(0);
		RecordValue r = (RecordValue) obj.eval(e);
		// Get class from the Store
		RecordValue classRecord = (RecordValue) Store.theStore.get(r.get("Class"));
		
		if ( classRecord == cachedClass ) {
			// if class is already cached, return the method
			return cachedMethod;
		} else {
			// class is not cached, so cache the class, and look up the method
			cachedClass = classRecord;
			Expression method = operands.elementAt(1);
			PropertyValue p = (PropertyValue) method.eval(e);
			return lookupInClass( classRecord, p.value );
		}
	}
	
	private Value lookupInClass( RecordValue classRecord, String methodName ) {
		Integer methodLocation = classRecord.get(methodName);
		
		// Case: method found in this class
		if ( methodLocation != null ) {
			cachedMethod = (FunValue) Store.theStore.get(methodLocation);
			return cachedMethod;
		}
		
		// Case: method not found in this class. 
		// Keep looking up hierarchy
		return lookupInClass( (RecordValue) Store.theStore.get(classRecord.get("Parent")), methodName );
	}

    // //////////////////////
    // Support Functions
    // //////////////////////

    public String toString() {
	String s = "";
	for  (Expression operand : operands) 
	    s = s + " " + operand;
	return "("+operator+" "+s+")";
    }
}

