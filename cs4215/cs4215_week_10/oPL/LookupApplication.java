package oPL;

import java.util.*;

public class LookupApplication extends Application {

	public Expression operator;

	public Vector<Expression> operands;

	RecordValue theClass;
	FunValue theMethod;

	public LookupApplication(Expression rator, Vector<Expression> rands) {
		super(rator, rands);
		theClass = null;
		theMethod = null;
	}

	public Value eval(Environment e) {
		// Take out the obj from Store
		RecordValue obj = (RecordValue) Store.theStore.get(e.access("obj"));
		// Find out the Class of obj
		Integer objClassID = obj.get("Class");
		RecordValue objClass = (RecordValue) Store.theStore.get(objClassID);

		if (theClass == null || objClass != theClass) {
			theMethod = (FunValue) super.eval(e);
			theClass = objClass;
		}
		return theMethod;

	}

	// //////////////////////
	// Support Functions
	// //////////////////////

	public String toString() {
		String s = "";
		for (Expression operand : operands)
			s = s + " " + operand;
		return "(" + operator + " " + s + ")";
	}
}
