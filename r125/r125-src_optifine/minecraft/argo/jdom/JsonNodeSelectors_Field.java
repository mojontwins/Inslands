package argo.jdom;

import java.util.Map;

final class JsonNodeSelectors_Field extends LeafFunctor {
	final JsonStringNode field_27066_a;

	JsonNodeSelectors_Field(JsonStringNode jsonStringNode1) {
		this.field_27066_a = jsonStringNode1;
	}

	public boolean func_27065_a(Map map1) {
		return map1.containsKey(this.field_27066_a);
	}

	public String shortForm() {
		return "\"" + this.field_27066_a.getText() + "\"";
	}

	public JsonNode func_27064_b(Map map1) {
		return (JsonNode)map1.get(this.field_27066_a);
	}

	public String toString() {
		return "a field called [\"" + this.field_27066_a.getText() + "\"]";
	}

	public Object typeSafeApplyTo(Object object1) {
		return this.func_27064_b((Map)object1);
	}

	public boolean matchesNode(Object object1) {
		return this.func_27065_a((Map)object1);
	}
}
