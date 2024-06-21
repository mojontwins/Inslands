package argo.jdom;

final class JsonNodeSelectors_String extends LeafFunctor {
	public boolean func_27072_a(JsonNode jsonNode1) {
		return JsonNodeType.STRING == jsonNode1.getType();
	}

	public String shortForm() {
		return "A short form string";
	}

	public String func_27073_b(JsonNode jsonNode1) {
		return jsonNode1.getText();
	}

	public String toString() {
		return "a value that is a string";
	}

	public Object typeSafeApplyTo(Object object1) {
		return this.func_27073_b((JsonNode)object1);
	}

	public boolean matchesNode(Object object1) {
		return this.func_27072_a((JsonNode)object1);
	}
}
