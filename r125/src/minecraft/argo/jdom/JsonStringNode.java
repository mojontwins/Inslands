package argo.jdom;

import java.util.List;
import java.util.Map;

public final class JsonStringNode extends JsonNode implements Comparable {
	private final String value;

	JsonStringNode(String string1) {
		if(string1 == null) {
			throw new NullPointerException("Attempt to construct a JsonString with a null value.");
		} else {
			this.value = string1;
		}
	}

	public JsonNodeType getType() {
		return JsonNodeType.STRING;
	}

	public String getText() {
		return this.value;
	}

	public Map getFields() {
		throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
	}

	public List getElements() {
		throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
	}

	public boolean equals(Object object1) {
		if(this == object1) {
			return true;
		} else if(object1 != null && this.getClass() == object1.getClass()) {
			JsonStringNode jsonStringNode2 = (JsonStringNode)object1;
			return this.value.equals(jsonStringNode2.value);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return this.value.hashCode();
	}

	public String toString() {
		return "JsonStringNode value:[" + this.value + "]";
	}

	public int func_27223_a(JsonStringNode jsonStringNode1) {
		return this.value.compareTo(jsonStringNode1.value);
	}

	public int compareTo(Object object1) {
		return this.func_27223_a((JsonStringNode)object1);
	}
}
