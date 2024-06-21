package argo.jdom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class JsonObject extends JsonRootNode {
	private final Map fields;

	JsonObject(Map map1) {
		this.fields = new HashMap(map1);
	}

	public Map getFields() {
		return new HashMap(this.fields);
	}

	public JsonNodeType getType() {
		return JsonNodeType.OBJECT;
	}

	public String getText() {
		throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
	}

	public List getElements() {
		throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
	}

	public boolean equals(Object object1) {
		if(this == object1) {
			return true;
		} else if(object1 != null && this.getClass() == object1.getClass()) {
			JsonObject jsonObject2 = (JsonObject)object1;
			return this.fields.equals(jsonObject2.fields);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return this.fields.hashCode();
	}

	public String toString() {
		return "JsonObject fields:[" + this.fields + "]";
	}
}
