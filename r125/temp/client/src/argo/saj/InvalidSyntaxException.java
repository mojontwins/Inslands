package argo.saj;

public final class InvalidSyntaxException extends Exception {
	private final int column;
	private final int row;

	InvalidSyntaxException(String string1, ThingWithPosition thingWithPosition2) {
		super("At line " + thingWithPosition2.getRow() + ", column " + thingWithPosition2.getColumn() + ":  " + string1);
		this.column = thingWithPosition2.getColumn();
		this.row = thingWithPosition2.getRow();
	}

	InvalidSyntaxException(String string1, Throwable throwable2, ThingWithPosition thingWithPosition3) {
		super("At line " + thingWithPosition3.getRow() + ", column " + thingWithPosition3.getColumn() + ":  " + string1, throwable2);
		this.column = thingWithPosition3.getColumn();
		this.row = thingWithPosition3.getRow();
	}
}
