package simPLvm;

import sVML.*;

public class VM extends FixedSizeVM {

	// the runtime stack is implemented outside
	// of the heap. Its elements are addresses of
	// heap nodes, each of which represents
	// a runtime stack frame. The implementation
	// represents the runtime stack by an array.

	private static final int[] RUNTIMESTACK = new int[RUNTIMESTACK_SIZE];
	private static int TopOfRuntimeStack = -1;

	private static void pushOnRuntimeStack(int i) {
		RUNTIMESTACK[++TopOfRuntimeStack] = i;
	}

	private static int popFromRuntimeStack() {
		return RUNTIMESTACK[TopOfRuntimeStack--];
	}

	// registers are static now, so that
	// the garbage collector can find its roots

	private static int os, e, pc;

	// ##################
	// ##### CHENEY #####
	// ##################

	// We introduce temp_root to handle instructions that
	// allocate nodes on the heap and then may flip.
	// Those nodes are assigned to temp_root, to make
	// sure they are copied

	private static int temp_root;

	// tags used for identifying nodes in the heap

	private static final class TAGS {
		public static final int INT = -100, BOOL = -101, ENVIRONMENT = -102,
				CLOSURE = -103, STACKFRAME = -104, OPERANDSTACK = -105,
				ERROR = -106;
	}

	// setting up the heap

	private static final int HEAPBOTTOM = 0; // smallest heap address
	private static final int[] HEAP = new int[HEAP_SIZE];

	// for debugging: peek at section of heap starting
	// at position "address" and size "size"

	private static void peek(int address, int size) {
		System.out.println("peek");
		for (int i = address; i <= address + size; i++)
			System.out.println("at " + i + ": " + HEAP[i]);
	}

	// General node layout:

	// 0: tag = a number between -100 and -110
	// 1: size = the number of slots occupied by the node

	private static final int TAG_SLOT = 0;
	private static final int SIZE_SLOT = 1;
	private static final int FIRST_CHILD_SLOT = 2;
	private static final int LAST_CHILD_SLOT = 3;

	// Layout of nodes with children nodes:

	// 0: tag
	// 1: size
	// 2: offset of first child from the tag
	// 3: offset of last child from the tag
	// ... further information and/or children

	// integer nodes layout
	//
	// 0: tag = -100
	// 1: size = 5
	// 2: offset of first child from the tag: 6 (no children)
	// 3: offset of last child from the tag: 5 (must be less than first)
	// 4: value

	private static final int INT_VALUE_SLOT = 4;

	private static int newInt(int i) {
		int node_address = New(TAGS.INT, 5);
		HEAP[node_address + FIRST_CHILD_SLOT] = 6;
		HEAP[node_address + LAST_CHILD_SLOT] = 5;
		HEAP[node_address + INT_VALUE_SLOT] = i;
		return node_address;
	}

	// boolean nodes layout
	//
	// 0: tag = -101
	// 1: size = 5
	// 2: offset of first child from the tag: 6 (no children)
	// 3: offset of last child from the tag: 5 (must be less than first)
	// 4: value

	private static final int BOOL_VALUE_SLOT = 4;

	private static int newBool(int i) {
		int node_address = New(TAGS.BOOL, 5);
		HEAP[node_address + FIRST_CHILD_SLOT] = 6;
		HEAP[node_address + LAST_CHILD_SLOT] = 5;
		HEAP[node_address + BOOL_VALUE_SLOT] = i;
		return node_address;
	}

	// boolean values are represented by 0 and 1
	private static final int TRUE = 1;
	private static final int FALSE = 0;

	// error nodes layout
	//
	// 0: tag = -106
	// 1: size = 4
	// 2: offset of first child from the tag: 5 (no children)
	// 3: offset of last child from the tag: 4 (must be less than first)

	private static int newError() {
		int node_address = New(TAGS.ERROR, 4);
		HEAP[node_address + FIRST_CHILD_SLOT] = 4;
		HEAP[node_address + LAST_CHILD_SLOT] = 3;
		return node_address;
	}

	// closure nodes layout
	//
	// 0: tag = -103
	// 1: size = 7
	// 2: offset of first child from the tag: 6 (only environment)
	// 3: offset of last child from the tag: 6
	// 4: address = address of function
	// 5: stack size = max stack size needed for executing function body
	// 6: environment

	private static final int CLOSURE_ADDRESS_SLOT = 4;
	private static final int CLOSURE_STACKSIZE_SLOT = 5;
	private static final int CLOSURE_ENVIRONMENT_SLOT = 6;

	private static int newClosure(int a, int s) {
		int node_address = New(TAGS.CLOSURE, 7);
		HEAP[node_address + FIRST_CHILD_SLOT] = CLOSURE_ENVIRONMENT_SLOT;
		HEAP[node_address + LAST_CHILD_SLOT] = CLOSURE_ENVIRONMENT_SLOT;
		HEAP[node_address + CLOSURE_ADDRESS_SLOT] = a;
		HEAP[node_address + CLOSURE_STACKSIZE_SLOT] = s;
		HEAP[node_address + CLOSURE_ENVIRONMENT_SLOT] = e;
		return node_address;
	}

	private static void setClosureEnvironment(int cl, int e) {
		HEAP[cl + CLOSURE_ENVIRONMENT_SLOT] = e;
	}

	// stackframe nodes layout
	//
	// 0: tag = -104
	// 1: size = 7
	// 2: offset of first child from the tag: 5 (environment)
	// 3: offset of last child from the tag: 6 (operand stack)
	// 4: program counter = return address
	// 5: environment
	// 6: operand stack

	private static final int STACKFRAME_PROGRAMCOUNTER_SLOT = 4;
	private static final int STACKFRAME_ENVIRONMENT_SLOT = 5;
	private static final int STACKFRAME_OPERANDSTACK_SLOT = 6;

	private static int newStackFrame(int pc) {
		int node_address = New(TAGS.STACKFRAME, 7);
		HEAP[node_address + FIRST_CHILD_SLOT] = STACKFRAME_ENVIRONMENT_SLOT;
		HEAP[node_address + LAST_CHILD_SLOT] = STACKFRAME_OPERANDSTACK_SLOT;
		HEAP[node_address + STACKFRAME_PROGRAMCOUNTER_SLOT] = pc;
		HEAP[node_address + STACKFRAME_ENVIRONMENT_SLOT] = e;
		HEAP[node_address + STACKFRAME_OPERANDSTACK_SLOT] = os;
		return node_address;
	}

	// operandstack nodes layout
	//
	// 0: tag = -105
	// 1: size = maximal number of entries + 4
	// 2: first child slot = 4
	// 3: last child slot = current top of stack; initially 3 (empty stack)
	// 4: first entry
	// 5: second entry
	// ...

	private static int newOperandStack(int size) {
		int node_address = New(TAGS.OPERANDSTACK, 4 + size);
		// the operand stack is initially empty. Therefore, the
		// initial "last child" pointer is pointing at the place
		// one below the first reserved slot for the stack.
		HEAP[node_address + FIRST_CHILD_SLOT] = 4;
		HEAP[node_address + LAST_CHILD_SLOT] = 3;
		return node_address;
	}

	// push on the operand stack
	private static void pushOnOperandStack(int x) {
		HEAP[os + LAST_CHILD_SLOT] = HEAP[os + LAST_CHILD_SLOT] + 1;
		HEAP[os + HEAP[os + LAST_CHILD_SLOT]] = x;
	}

	// pop from operand stack
	private static int popFromOperandStack() {
		int value = HEAP[os + HEAP[os + LAST_CHILD_SLOT]];
		HEAP[os + LAST_CHILD_SLOT] = HEAP[os + LAST_CHILD_SLOT] - 1;
		return value;
	}

	// environment nodes layout
	//
	// 0: tag = -102
	// 1: size = number of entries + 4
	// 2: first child = 4
	// 3: last child
	// 4: first entry
	// 5: second entry
	// ...

	private static int newEnvironment(int size) {
		int node_address = New(TAGS.ENVIRONMENT, 4 + size);
		HEAP[node_address + FIRST_CHILD_SLOT] = 4;
		HEAP[node_address + LAST_CHILD_SLOT] = 3 + size;
		return node_address;
	}

	private static int extend(int env, int byHowMany) {
		int node_address = newEnvironment(HEAP[env + SIZE_SLOT] - 4 + byHowMany);

		// env may have changed due to garbage collection in newEnvironment.
		// Therefore refresh it.
		if (already_copied(env))
			env = HEAP[env + FORWARDINGADDRESS];

		for (int i = HEAP[env + FIRST_CHILD_SLOT]; i <= HEAP[env
				+ LAST_CHILD_SLOT]; i++)
			HEAP[node_address + i] = HEAP[env + i];
		return node_address;
	}

	// for printing out the final result

	private static String heapAddressToString(int address) {
		switch (HEAP[address]) {

		case TAGS.INT:
			return Integer.toString(HEAP[address + INT_VALUE_SLOT]);

		case TAGS.BOOL:
			return (HEAP[address + BOOL_VALUE_SLOT] == 1) ? "true" : "false";

		case TAGS.ENVIRONMENT:
			return "env";

		case TAGS.CLOSURE:
			return "fun..->...end";

		case TAGS.STACKFRAME:
			return "stackframe";

		case TAGS.OPERANDSTACK:
			return "operandstack";

		case TAGS.ERROR:
			return "error";

		default:
			return "TAG not known";
		}
	}

	// Cheney's Copying Garbage Collection

	// hp is the current "top of heap" pointer, initially 0
	private static int hp = 0;

	// initialize spaces for Cheney's algorithm

	private static final int SPACESIZE = HEAP_SIZE / 2;
	private static int Tospace = HEAPBOTTOM;
	private static int Fromspace = Tospace + SPACESIZE;
	private static int Topofspace = Tospace + SPACESIZE - 1;
	private static int Free = Tospace;

	// New creates new node with given tag and size
	private static int New(int tag, int size) {
		if (Free + size >= Topofspace)
			flip();
		if (Free + size >= Topofspace)
			System.out.println("Memory Exhausted");
		int newnode = Free;
		HEAP[newnode + TAG_SLOT] = tag;
		HEAP[newnode + SIZE_SLOT] = size;
		Free = Free + size;
		return newnode;
	}

	private static void flip() {
		System.out.println("flip");
		int temp = Fromspace;
		Fromspace = Tospace;
		Tospace = temp;
		Topofspace = Tospace + SPACESIZE - 1;
		int scan = Tospace;
		Free = Tospace;

		// copy register roots: os, e
		os = copy(os);
		e = copy(e);

		// copy temp_root if it is >=0; it is set to -1 when not needed
		if (temp_root >= 0)
			temp_root = copy(temp_root);

		// copy all entries of the runtime stack
		for (int i = 0; i <= TopOfRuntimeStack; i++)
			RUNTIMESTACK[i] = copy(RUNTIMESTACK[i]);
		// run garbage collection until scan reaches Free
		while (scan < Free) {
			for (int c = HEAP[scan + FIRST_CHILD_SLOT]; c <= HEAP[scan
					+ LAST_CHILD_SLOT]; c++) {
				HEAP[scan + c] = copy(HEAP[scan + c]);

			}
			scan = scan + HEAP[scan + SIZE_SLOT];
		}
	}

	// use tag slot as forwarding address;
	// the trick: since tags are negative, they
	// can never be confused with heap addresses
	private static final int FORWARDINGADDRESS = 0;

	private static int copy(int v) {
		if (already_copied(v))
			return HEAP[v + FORWARDINGADDRESS];
		else {
			int addr = Free;
			move(v, Free);
			Free = Free + HEAP[v + SIZE_SLOT];
			HEAP[v + FORWARDINGADDRESS] = addr;
			return addr;
		}
	}

	private static boolean already_copied(int v) {
		return HEAP[v + FORWARDINGADDRESS] >= Tospace
				&& HEAP[v + FORWARDINGADDRESS] <= Free;
	}

	private static void move(int source, int destination) {
		for (int i = 0; i < HEAP[source + SIZE_SLOT]; i++)
			HEAP[destination + i] = HEAP[source + i];
	}

	// run executes the instructions in the given instruction array

	public static String run(INSTRUCTION[] INSTRUCTIONARRAY) {

		// declare and initialize registers
		os = 0;
		// address of current operand stack on HEAP,
		// initialized by START instruction
		e = newEnvironment(0);
		// address of current environment on HEAP
		// initially an empty environment
		pc = 0;
		// program counter initially 0

		temp_root = -1;
		// temp_root is a pointer to a temporary
		// root used in in CALL and LDRF
		// -1 indicates that it is currently
		// not needed

		// machine instruction loop

		loop: while (true) {

			INSTRUCTION i = INSTRUCTIONARRAY[pc];

			// System.out.println("instr: "+ i);

			switch (i.OPCODE) {

			// start execution: initialize operand stack

			case OPCODES.START: {
				os = newOperandStack(((START) i).MAXSTACKSIZE);
				pc++;
				break;
			}

			// pushing constants

			case OPCODES.LDCI: {
				pushOnOperandStack(newInt(((LDCI) i).VALUE));
				pc++;
				break;
			}

			case OPCODES.LDCB: {
				pushOnOperandStack(newBool(((LDCB) i).VALUE ? TRUE : FALSE));
				pc++;
				break;
			}

			// primitive operations:
			// watch out, the non-commutative operations have to consider
			// that the arguments appear on the stack in reverse order!

			case OPCODES.PLUS: {
				pushOnOperandStack(newInt(HEAP[popFromOperandStack()
						+ INT_VALUE_SLOT]
						+ HEAP[popFromOperandStack() + INT_VALUE_SLOT]));
				pc++;
				break;
			}

			case OPCODES.TIMES: {
				pushOnOperandStack(newInt(HEAP[popFromOperandStack()
						+ INT_VALUE_SLOT]
						* HEAP[popFromOperandStack() + INT_VALUE_SLOT]));
				pc++;
				break;
			}

			case OPCODES.MINUS: {
				pushOnOperandStack(newInt(-HEAP[popFromOperandStack()
						+ INT_VALUE_SLOT]
						+ HEAP[popFromOperandStack() + INT_VALUE_SLOT]));
				pc++;
				break;
			}

			case OPCODES.DIV: {
				int divisor = HEAP[popFromOperandStack() + INT_VALUE_SLOT];
				int dividend = HEAP[popFromOperandStack() + INT_VALUE_SLOT];
				if (divisor == 0) {
					pushOnOperandStack(newError());
					break loop;
				} else {
					pushOnOperandStack(newInt(dividend / divisor));
					pc++;
					break;
				}
			}

			case OPCODES.OR: {
				int i1 = HEAP[popFromOperandStack() + BOOL_VALUE_SLOT];
				int i2 = HEAP[popFromOperandStack() + BOOL_VALUE_SLOT];
				pushOnOperandStack(newBool((i1 == TRUE) ? TRUE : i2));
				pc++;
				break;
			}

			case OPCODES.AND: {
				int i1 = HEAP[popFromOperandStack() + BOOL_VALUE_SLOT];
				int i2 = HEAP[popFromOperandStack() + BOOL_VALUE_SLOT];
				pushOnOperandStack(newBool((i1 == FALSE) ? FALSE : i2));
				pc++;
				break;
			}

			case OPCODES.NOT: {
				int i1 = HEAP[popFromOperandStack() + BOOL_VALUE_SLOT];
				pushOnOperandStack(newBool((i1 == TRUE) ? FALSE : TRUE));
				pc++;
				break;
			}

			case OPCODES.GREATER: {
				pushOnOperandStack(newBool((HEAP[popFromOperandStack()
						+ INT_VALUE_SLOT] < HEAP[popFromOperandStack()
						+ INT_VALUE_SLOT]) ? TRUE : FALSE));
				pc++;
				break;
			}

			case OPCODES.LESS: {
				pushOnOperandStack(newBool((HEAP[popFromOperandStack()
						+ INT_VALUE_SLOT] > HEAP[popFromOperandStack()
						+ INT_VALUE_SLOT]) ? TRUE : FALSE));
				pc++;
				break;
			}

			case OPCODES.EQUAL: {
				pushOnOperandStack(newBool((HEAP[popFromOperandStack()
						+ INT_VALUE_SLOT] == HEAP[popFromOperandStack()
						+ INT_VALUE_SLOT]) ? TRUE : FALSE));
				pc++;
				break;
			}

			// jump by setting pc.

			case OPCODES.GOTO: {
				pc = ((GOTO) i).ADDRESS;
				break;
			}

			case OPCODES.JOF: {
				pc = (HEAP[popFromOperandStack() + BOOL_VALUE_SLOT] == TRUE ? pc + 1
						: ((JOF) i).ADDRESS);
				break;
			}

			// load function by pushing closure.

			case OPCODES.LDF: {
				pushOnOperandStack(newClosure(((LDF) i).ADDRESS,
						((LDF) i).MAXSTACKSIZE));
				pc++;
				break;
			}

			// load recursive function by pushing closure.

			case OPCODES.LDRF: {
				// make temp_root point to a new closure.
				// Note that temp_root will be updated in case
				// garbage collection happens in "extend" below.
				temp_root = newClosure(((LDRF) i).ADDRESS,
						((LDRF) i).MAXSTACKSIZE);
				pushOnOperandStack(temp_root);
				// extend the current environment by one slot
				int e_closure = extend(e, 1);
				// plug the extended environment into the new closure
				setClosureEnvironment(temp_root, e_closure);
				// put a reference to the new closure into the slot
				HEAP[e_closure + HEAP[e_closure + SIZE_SLOT] - 1] = temp_root;
				// now temp_root is not needed any longer;
				// setting it to -1 makes the garbage collector ignore it
				temp_root = -1;
				pc++;
				break;
			}

			// CALL finds a closure on top of the stack.
			// the closure environment is filled up with
			// actual parameters that are popped from the stack.
			// the registers are saved in stack frame and
			// the stack frame pushed on runtime stack.
			// new registers are initialized in the obvious way.

			case OPCODES.CALL: {
				int n = ((CALL) i).NUMBEROFARGUMENTS;
				int closure = HEAP[os + HEAP[os + LAST_CHILD_SLOT] - n];
				int closureAddress = HEAP[closure + CLOSURE_ADDRESS_SLOT];
				int closureStacksize = HEAP[closure + CLOSURE_STACKSIZE_SLOT];
				// extend the closure environment by slots for parameters
				temp_root = extend(HEAP[closure + CLOSURE_ENVIRONMENT_SLOT], n);
				// Note that temp_root is copied in "flip". This is important,
				// because garbage collection may happen
				// in newStackFrame or newOperandStack below
				int s = HEAP[temp_root + SIZE_SLOT]
						- HEAP[temp_root + FIRST_CHILD_SLOT];
				for (int j = s - 1; j >= s - n; --j)
					HEAP[temp_root + HEAP[temp_root + FIRST_CHILD_SLOT] + j] = popFromOperandStack();
				popFromOperandStack();
				pushOnRuntimeStack(newStackFrame(pc + 1));
				pc = closureAddress;
				os = newOperandStack(closureStacksize);
				e = temp_root;
				temp_root = -1;
				break;
			}

			// address for LD is calculated by compiler and
			// put in the INDEX field of the instruction.
			// LD only needs to push the environment value under INDEX

			case OPCODES.LD: {
				pushOnOperandStack(HEAP[e + HEAP[e + FIRST_CHILD_SLOT]
						+ (((LD) i).INDEX)]);
				pc++;
				break;
			}

			// RTN reinstalls the top frame of the runtime stack,
			// and pushes the return value on the operand stack.

			case OPCODES.RTN: {
				int f = popFromRuntimeStack();
				pc = HEAP[f + STACKFRAME_PROGRAMCOUNTER_SLOT];
				e = HEAP[f + STACKFRAME_ENVIRONMENT_SLOT];
				int returnValue = popFromOperandStack();
				os = HEAP[f + STACKFRAME_OPERANDSTACK_SLOT];
				pushOnOperandStack(returnValue);
				break;
			}

			case OPCODES.DONE:
				break loop;

			default:
				System.out.println(" unknown opcode: " + i.OPCODE);
				pc++;
			}
		}
		return heapAddressToString(popFromOperandStack());
	}
}
