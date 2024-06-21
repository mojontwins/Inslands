package argo.jdom;

import java.util.Map;

final class JsonNodeSelectors_Object extends LeafFunctor {
	public boolean func_27070_a(JsonNode jsonNode1) {
		return JsonNodeType.OBJECT == jsonNode1.getType();
	}

	public String shortForm() {
		return "A short form object";
	}

	public Map func_27071_b(JsonNode jsonNode1) {
		return jsonNode1.getFields();
	}

	public String toString() {
		return "an object";
	}

	public Object typeSafeApplyTo(Object object1) {
		return this.func_27071_b((JsonNode)object1);
	}

	public boolean matchesNode(Object object1) {
		return this.func_27070_a((JsonNode)object1);
	}
}
