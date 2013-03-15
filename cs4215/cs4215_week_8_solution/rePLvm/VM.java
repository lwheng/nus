
package rePLvm; 

import rVML.*;
import rePLcompiler.*;
import java.util.*;

public class VM {

	public static class CatchFrame extends StackFrame {
		public CatchFrame(int p, Environment e, Stack<Value> os) {
			super(p, e, os);
		}
	}

	// run:
	// instructionArray is the usual array of instructions
	// p is the table that the compiler constructs for
	// accessing arrays.
	// p[i][j] is the index for accessing the property p with 
	// idp(p) = j in a record r with idr(r) = i

	public static Value run(INSTRUCTION[] instructionArray,int[][] p, Idp idp, 
			int divisionByZeroAddress, int invalidRecordAccessAddress) {
		// initialize registers
		int pc = 0;
		Stack<Value> os = new Stack<Value>();
		Stack<StackFrame> rts = new Stack<StackFrame>();
		Environment e = new Environment();

		// loop

		loop:
			while (true) {
				INSTRUCTION i = instructionArray[pc];

				System.out.println("pc: "+pc+"; instruction: "+i);

				switch (i.OPCODE) {

				// pushing constants

				case OPCODES.LDCI:  {
					os.push(new IntValue(((LDCI)i).VALUE));
					pc++;
					break;
				}

				case OPCODES.LDCB:  
				{
					os.push(new BoolValue(((LDCB)i).VALUE));

					pc++;
					break;
				}

				// primitive operations:
				// watch out, the non-commutative operations have to consider
				// that the arguments appear on the stack in reverse order!

				case OPCODES.PLUS: 
				{
					os.push(new IntValue(
							((IntValue) os.pop()).value
							+ 
							((IntValue) os.pop()).value));
					pc++;
					break;
				}
				case OPCODES.TIMES:  {
					os.push(new IntValue(

							((IntValue) os.pop()).value
							* 
							((IntValue) os.pop()).value));
					pc++;
					break;
				}
				case OPCODES.MINUS: {
					os.push(new IntValue(

							- ((IntValue) os.pop()).value
							+
							((IntValue) os.pop()).value));
					pc++;
					break;
				}

				case OPCODES.DIV: {
					int divisor = ((IntValue) os.pop()).value;
					if (divisor == 0) {
						pc = divisionByZeroAddress; 
					} else {
						os.push(new 
								IntValue(((IntValue) os.pop()).value
										/
										divisor));
						pc++;
					}
					break;
				}
				case OPCODES.OR:    { 
					boolean b1, b2;
					b2 = ((BoolValue)os.pop()).value;
					b1 = ((BoolValue)os.pop()).value;
					os.push(new BoolValue(b1 || b2));
					pc++;
					break;
				}

				case OPCODES.AND:   { 
					boolean b1, b2;
					b2 = ((BoolValue)os.pop()).value;
					b1 = ((BoolValue)os.pop()).value;
					os.push(new BoolValue(b1 && b2));
					pc++;
					break;
				}

				case OPCODES.NOT: {
					os.push(new BoolValue(
							! ((BoolValue) os.pop()).value));
					pc++;
					break;
				}

				case OPCODES.GREATER: {
					os.push(new BoolValue(

							((IntValue) os.pop()).value
							<
							((IntValue) os.pop()).value));
					pc++;
					break;
				}

				case OPCODES.LESS: {
					os.push(new BoolValue(

							((IntValue) os.pop()).value
							>
							((IntValue) os.pop()).value));
					pc++;
					break;
				}

				case OPCODES.EQUAL: {
					os.push(new BoolValue(

							((IntValue) os.pop()).value
							==
							((IntValue) os.pop()).value));
					pc++;
					break;
				}

				// jump by setting pc.

				case OPCODES.GOTO: {
					pc = ((GOTO)i).ADDRESS;
					break;
				}

				case OPCODES.JOF: {
					pc = (((BoolValue) os.pop()).value) 
							? pc+1
									: ((JOF)i).ADDRESS;
					break;
				}

				// load function by pushing closure.
				// environment in closure has space for arguments.
				// CALL actually fills these new slots.

				case OPCODES.LDF: {
					Environment env = e;

					os.push(new Closure(env,((LDF)i).ADDRESS));
					pc++;
					break;
				}

				// recursive closures have an extra slot for the function variable.
				// this slot is filled by the function definition. Note again
				// the circularity via environment addressing.

				case OPCODES.LDRF: {
					Environment envr = e.extend(1);

					Value fv = new 
							Closure(envr,((LDRF)i).ADDRESS);
					envr.setElementAt(fv,e.size());
					os.push(fv);
					pc++;
					break;
				}

				// CALL finds a closure on top of the stack.
				// the closure environment is filled up with
				// actual parameters that are popped from the stack.
				// the registers are saved in stack frame and
				// the stack frame pushed on runtime stack.
				// new registers are initialized in the obvious way.

				case OPCODES.CALL:  { 
					int n = ((CALL)i).NUMBEROFARGUMENTS;
					Closure closure 
					= (Closure) os.elementAt(os.size()-n-1);
					Environment newEnv 
					= closure.environment.extend(n);
					int s = newEnv.size();
					int k = s - n;
					int j = s - 1;
					while (j >= k)
						newEnv.setElementAt(os.pop(),j--);
					os.pop();
					rts.push(new StackFrame(pc+1,e,os));
					pc = closure.ADDRESS;
					e = newEnv;
					os = new Stack<Value>();
					break;
				}


				// address for LD is calculated by compiler and
				// put in the INDEX field of the instruction.
				// LD only needs to push the environment value under INDEX

				case OPCODES.LD: {
					os.push(e.elementAt(((LD)i).INDEX));
					pc++;
					break;
				}

				// RTN reinstalls the top frame of the runtime stack,
				// and pushes the return value on the operand stack.

				case OPCODES.RTN: {
					if (rts.empty()) break loop;
					StackFrame f = rts.pop();
					pc = f.pc;
					e = f.environment;
					Value returnValue = os.pop();
					os = f.operandStack;
					os.push(returnValue);
					break;
				}

				// note that the RecordValue constructor
				// takes three parameters:
				// * the record identifier
				// * number of arguments (fields)
				// * the idp object (in order to print properties)
				case OPCODES.RCD: {
					int size = ((RCD)i).NUMBEROFARGUMENTS;
					int idr = ((RCD)i).IDR;
					RecordValue rv = new RecordValue(idr, 
							size,
							idp);
					rv.i_to_idp = new int[((RCD)i).NUMBEROFARGUMENTS];
					for (int j=size-1; j>=0; j--) {
						Value this_v = os.pop();
						int this_idp = ((IntValue) os.pop()).value;
						// to support the display of records
						rv.i_to_idp[j] = this_idp;
						rv.entries[p[idr][this_idp]] = this_v;
					}
					os.push(rv);
					pc++;
					break;
				}

				case OPCODES.DOT:{
					int this_idp = ((IntValue) os.pop()).value;
					RecordValue rv = ((RecordValue) os.pop());
					if (p[rv.idr][this_idp] == -1) {
						pc = invalidRecordAccessAddress;
					} else {
						os.push(rv.entries[p[rv.idr][this_idp]]);
						pc++;
					}
					break;
				}

				case OPCODES.HASP: {
					int this_idp = ((IntValue) os.pop()).value;
					RecordValue rv = ((RecordValue) os.pop());	    	
					if (p[rv.idr][this_idp] == -1)
						os.push(new BoolValue(false));
					else
						os.push(new BoolValue(true));
					pc++;
					break;
				}

				case OPCODES.EMPTY: {
					Value v = os.pop();
					System.out.println(v);
					RecordValue rv = ((RecordValue) v);	
					System.out.println(rv); System.out.println(rv.entries);
					if (rv.entries == null) 
						os.push(new BoolValue(true));
					else
						os.push(new BoolValue(false));
					pc++;
					break;
				}

				case OPCODES.THROW: {
					if (rts.isEmpty()) {
						System.out.println("Uncaught exception: ");
						break loop;
					} else {
						StackFrame sf = rts.pop();
						if (sf instanceof CatchFrame) {
							pc = ((CatchFrame) sf).pc;
							e = ((CatchFrame) sf).environment.extend(1);
							e.setElementAt(os.pop(), e.size()-1);
							os = ((CatchFrame) sf).operandStack;
							break;
						} else {
							// do not pc++. this implements a loop 
							// for popping stack frames until a CatchFrame
							// is found
							break;
						}
					}
				}

				case OPCODES.TRY: {
					rts.push(new CatchFrame( ((TRY) i).ADDRESS, e, os));
					pc++;
					break;
				}

				case OPCODES.ENDTRY: {
					rts.pop();
					pc++;
					break;
				}

				// DONE simply breaks the loop. The result is now
				// on top of the operand stack

				case OPCODES.DONE:    break loop;

				default:              System.out.println(" unknown opcode: " 
						+ i.OPCODE);
				pc++;
				}
			}
		return os.pop();
	}
}
