package sVML;

public class TAILCALL extends INSTRUCTION {
  public int NUMBEROFARGUMENTS;
  public TAILCALL(int noa) {
     OPCODE = OPCODES.TAILCALL;
     NUMBEROFARGUMENTS = noa;
  }
  public String toString() {
     return "TAILCALL" + " " + NUMBEROFARGUMENTS;
  }
  public String toXML() {
     return "<svm:TAILCALL>" + NUMBEROFARGUMENTS + "</svm:TAILCALL>";
  }
}
