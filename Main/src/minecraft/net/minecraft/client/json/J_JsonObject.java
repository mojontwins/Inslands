package net.minecraft.client.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class J_JsonObject extends J_JsonRootNode {
	private final Map<J_JsonStringNode,J_JsonNode> fields;

	public J_JsonObject(Map<J_JsonStringNode,J_JsonNode> map1) {
		this.fields = new HashMap<J_JsonStringNode,J_JsonNode>(map1);
	}

	public Map<J_JsonStringNode,J_JsonNode> getFields() {
		return new HashMap<J_JsonStringNode,J_JsonNode>(this.fields);
	}

	public EnumJsonNodeType getType() {
		return EnumJsonNodeType.OBJECT;
	}

	public String getText() {
		throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
	}

	public List<J_JsonNode> getElements() {
		throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
	}

	public boolean equals(Object object1) {
		if(this == object1) {
			return true;
		} else if(object1 != null && this.getClass() == object1.getClass()) {
			J_JsonObject j_JsonObject2 = (J_JsonObject)object1;
			return this.fields.equals(j_JsonObject2.fields);
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
