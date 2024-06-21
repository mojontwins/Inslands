package argo.jdom;

import java.util.List;

final class JsonNodeSelectors_Array extends LeafFunctor {
	public boolean matchesNode_(JsonNode jsonNode1) {
		return JsonNodeType.ARRAY == jsonNode1.getType();
	}

	public String shortForm() {
		return "A short form array";
	}

	public List typeSafeApplyTo(JsonNode jsonNode1) {
		return jsonNode1.getElements();
	}

	public String toString() {
		return "an array";
	}

	public Object typeSafeApplyTo(Object object1) {
		return this.typeSafeApplyTo((JsonNode)object1);
	}

	public boolean matchesNode(Object object1) {
		return this.matchesNode_((JsonNode)object1);
	}
}
