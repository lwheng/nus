package cVML;

public class AND extends INSTRUCTION {
	public AND() {
		OPCODE = OPCODES.AND;
	}

	public String toString() {
		return "AND";
	}

	public String toXML() {
		return "<rvm:AND/>";
	}
}
