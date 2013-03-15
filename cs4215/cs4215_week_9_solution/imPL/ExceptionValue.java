package imPL;

public class ExceptionValue extends Exception implements Value {
	private static final long serialVersionUID = -1007511413400896624L;
	public Value value;
	public ExceptionValue(Value v) {
		value = v;
	}
	public String toString() {
		return value.toString();
	}
}

