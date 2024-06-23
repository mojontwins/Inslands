package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class J_JsonArray extends J_JsonRootNode {
	private final List<J_JsonNode> elements;

	public J_JsonArray(Iterable<?> iterable1) {
		this.elements = asList(iterable1);
	}

	public EnumJsonNodeType getType() {
		return EnumJsonNodeType.ARRAY;
	}

	public List<J_JsonNode> getElements() {
		return new ArrayList<J_JsonNode>(this.elements);
	}

	public String getText() {
		throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
	}

	public Map<J_JsonStringNode,J_JsonNode> getFields() {
		throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
	}

	public boolean equals(Object object1) {
		if(this == object1) {
			return true;
		} else if(object1 != null && this.getClass() == object1.getClass()) {
			J_JsonArray j_JsonArray2 = (J_JsonArray)object1;
			return this.elements.equals(j_JsonArray2.elements);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return this.elements.hashCode();
	}

	public String toString() {
		return "JsonArray elements:[" + this.elements + "]";
	}

	private static List<J_JsonNode> asList(Iterable<?> iterable0) {
		return new J_JsonNodeList(iterable0);
	}
}
