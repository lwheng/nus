package simPL;

import java.util.*;

public class Application implements Expression {

	public Expression operator;

	public Vector<Expression> operands;

	public Application(Expression rator,Vector<Expression> rands) {
		operator = rator; operands = rands;
	}

	public Expression eliminateLet() {
		Vector<Expression> newoperands = new Vector<Expression>();
		for (Expression operand : operands) 
			newoperands.add(operand.eliminateLet());
		return new Application(operator.eliminateLet(), newoperands);
	}

	// //////////////////////
	// Dynamic Semantics
	// //////////////////////

	public StringSet freeVars() {
		StringSet x = operator.freeVars();
		// Union all the freevars from all operands
		for (Expression operand : operands)
			x.union(operand.freeVars());
		return x;
	}

	public Expression substitute(String var, Expression replacement) {
		Vector<Expression> newoperands = new Vector<Expression>();
		for (Expression operand : operands) 
			newoperands.add(operand.substitute(var,replacement));
		return new Application(operator.substitute(var,replacement), 
				newoperands);
	}

	public boolean reducible() {
		// when the operator is not a value, the application is reducible
		// if and only if the operator is reducible
		if (!IsValue.isValue(operator)) return operator.reducible();
		// for any operand, if it is not a value, then the application is 
		// reducible if and only if the operand is reducible
		for (Expression operand : operands) 
			if (!IsValue.isValue(operand)) return operand.reducible();
		// now all components are values. The application is reducible
		// if the operator is a Fun that matches the number of arguments
		// (note that RecFun is a subclass of Fun)
		return (operator instanceof Fun &&
				((Fun)operator).funType instanceof FunType &&
				((FunType)((Fun)operator).funType).argumentTypes.size() == operands.size() &&
				((Fun)operator).formals.size() == operands.size());
	}

	// Note that oneStep is only called on reducible Expressions
	public Expression oneStep() {
		// if operator is not a value, it must be reducible (considering
		// that the whole expression is reducible)
		if (!IsValue.isValue(operator)) {
			assert operator.reducible() : "operator "+operator+" must be reducible";
			// non-destructive programming: create a new Application
			return new Application(operator.oneStep(),operands);
		}
		// otherwise, reduce first operand that is not a value
		for (int i = 0; i < operands.size(); i++) {
			Expression e = operands.get(i);
			if (!IsValue.isValue(e)) {
				assert e.reducible() : "non-value operand must be reducible";
				// non-destructive programming: make a copy of the operands vector
				// and replace the expression in the copy
				Vector<Expression> newoperands = new Vector<Expression>(operands);
				newoperands.set(i,e.oneStep());
				return new Application(operator,newoperands);
			}
		}
		// We get here if neither the operator nor any of the operands are reducible.
		// For the whole expression to be reducible, the operator must be a Fun, whose 
		// number of parameters matches the number of operands, and all operands must be 
		// values
		assert operator instanceof Fun : "operator must be function";
		Fun funOperator = (Fun)operator;
		Type funType = funOperator.funType;
		Expression funBody = funOperator.body;
		assert funType instanceof FunType : "function must have function type";
		FunType theFunType = (FunType)funType;
		Vector<Type> theArgumentTypes = theFunType.argumentTypes;
		Type theReturnType = theFunType.returnType;
		assert theFunType.argumentTypes.size() == funOperator.formals.size() 
		: "number of function argument types must coincide with number of formal parameters";
		assert ((Fun)operator).formals.size() == operands.size() : 
			"number of formal parameters must be the same as number of arguments";
		Expression newBody = funBody;
		if (operator instanceof RecFun)
			newBody = funBody.substitute(((RecFun)operator).funVar,operator);
		if (operands.size() == 1) {
			return newBody.substitute(((Fun)operator).formals.elementAt(0), 
					operands.elementAt(0));
		} else {
			return transformToSingleArgumentApplication(
					transformToSingleArgumentFunction(newBody,0,operands.size()-1,
							theArgumentTypes,funOperator.formals, theReturnType),
							operands,
							operands.size()-1);
		}
	}

	Expression transformToSingleArgumentApplication(Expression operator, 
			Vector<Expression> operands, int n) {
		if (n==0) {
			Vector<Expression> arguments = new Vector<Expression>();
			arguments.add(operands.elementAt(0));
			return new Application(operator,arguments);
		} else {
			Vector<Expression> arguments = new Vector<Expression>();
			arguments.add(operands.elementAt(n));
			return new Application(transformToSingleArgumentApplication(operator,operands,n-1),
					arguments);
		}
	}		

	Fun transformToSingleArgumentFunction(Expression body, int from, int to, 
			Vector<Type> argTypes, Vector<String> formals, Type returnType) {
		Vector<Type> newArgTypes = new Vector<Type>();
		newArgTypes.add(argTypes.elementAt(from));
		Vector<String> newFormals = new Vector<String>();
		newFormals.add(formals.elementAt(from));
		if (from==to) {
			return new Fun(new FunType(newArgTypes,returnType),
					newFormals,
					body);
		}
		else {
			Fun newFun = transformToSingleArgumentFunction(body, from+1,to,argTypes,formals,returnType);
			return new Fun(new FunType(newArgTypes,newFun.funType),newFormals,newFun);
		}
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

	public String toXML() {
		String s = "";
		for (Expression operand : operands)
			s = s + operand.toXML();
		return "<simpl:application>\n"
		+"<simpl:operator>\n"
		+operator.toXML()
		+"</simpl:operator>\n"
		+"<simpl:arguments>\n"
		+s
		+"</simpl:arguments>\n"
		+"</simpl:application>\n";
	}
}
