package argo.jdom;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

final class JsonNumberNode extends JsonNode {
	private static final Pattern PATTERN = Pattern.compile("(-?)(0|([1-9]([0-9]*)))(\\.[0-9]+)?((e|E)(\\+|-)?[0-9]+)?");
	private final String value;

	JsonNumberNode(String string1) {
		if(string1 == null) {
			throw new NullPointerException("Attempt to construct a JsonNumber with a null value.");
		} else if(!PATTERN.matcher(string1).matches()) {
			throw new IllegalArgumentException("Attempt to construct a JsonNumber with a String [" + string1 + "] that does not match the JSON number specification.");
		} else {
			this.value = string1;
		}
	}

	public JsonNodeType getType() {
		return JsonNodeType.NUMBER;
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
			JsonNumberNode jsonNumberNode2 = (JsonNumberNode)object1;
			return this.value.equals(jsonNumberNode2.value);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return this.value.hashCode();
	}

	public String toString() {
		return "JsonNumberNode value:[" + this.value + "]";
	}
}
