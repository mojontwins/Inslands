package argo.jdom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class JsonArray extends JsonRootNode {
	private final List elements;

	JsonArray(Iterable iterable1) {
		this.elements = asList(iterable1);
	}

	public JsonNodeType getType() {
		return JsonNodeType.ARRAY;
	}

	public List getElements() {
		return new ArrayList(this.elements);
	}

	public String getText() {
		throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
	}

	public Map getFields() {
		throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
	}

	public boolean equals(Object object1) {
		if(this == object1) {
			return true;
		} else if(object1 != null && this.getClass() == object1.getClass()) {
			JsonArray jsonArray2 = (JsonArray)object1;
			return this.elements.equals(jsonArray2.elements);
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

	private static List asList(Iterable iterable0) {
		return new JsonArray_NodeList(iterable0);
	}
}
