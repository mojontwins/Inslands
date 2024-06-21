package argo.jdom;

final class ChainedFunctor implements Functor {
	private final JsonNodeSelector parentJsonNodeSelector;
	private final JsonNodeSelector childJsonNodeSelector;

	ChainedFunctor(JsonNodeSelector jsonNodeSelector1, JsonNodeSelector jsonNodeSelector2) {
		this.parentJsonNodeSelector = jsonNodeSelector1;
		this.childJsonNodeSelector = jsonNodeSelector2;
	}

	public boolean matchesNode(Object object1) {
		return this.parentJsonNodeSelector.matches(object1) && this.childJsonNodeSelector.matches(this.parentJsonNodeSelector.getValue(object1));
	}

	public Object applyTo(Object object1) {
		Object object2;
		try {
			object2 = this.parentJsonNodeSelector.getValue(object1);
		} catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException jsonNodeDoesNotMatchChainedJsonNodeSelectorException6) {
			throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_27321_b(jsonNodeDoesNotMatchChainedJsonNodeSelectorException6, this.parentJsonNodeSelector);
		}

		try {
			Object object3 = this.childJsonNodeSelector.getValue(object2);
			return object3;
		} catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException jsonNodeDoesNotMatchChainedJsonNodeSelectorException5) {
			throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_27323_a(jsonNodeDoesNotMatchChainedJsonNodeSelectorException5, this.parentJsonNodeSelector);
		}
	}

	public String shortForm() {
		return this.childJsonNodeSelector.shortForm();
	}

	public String toString() {
		return this.parentJsonNodeSelector.toString() + ", with " + this.childJsonNodeSelector.toString();
	}
}
