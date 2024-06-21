package argo.jdom;

import java.util.List;

final class JsonNodeSelectors_Element extends LeafFunctor {
	final int index;

	JsonNodeSelectors_Element(int i1) {
		this.index = i1;
	}

	public boolean matchesNode_(List list1) {
		return list1.size() > this.index;
	}

	public String shortForm() {
		return Integer.toString(this.index);
	}

	public JsonNode typeSafeApplyTo_(List list1) {
		return (JsonNode)list1.get(this.index);
	}

	public String toString() {
		return "an element at index [" + this.index + "]";
	}

	public Object typeSafeApplyTo(Object object1) {
		return this.typeSafeApplyTo_((List)object1);
	}

	public boolean matchesNode(Object object1) {
		return this.matchesNode_((List)object1);
	}
}
