package argo.jdom;

public final class JsonNodeSelector {
	final Functor valueGetter;

	JsonNodeSelector(Functor functor1) {
		this.valueGetter = functor1;
	}

	public boolean matches(Object object1) {
		return this.valueGetter.matchesNode(object1);
	}

	public Object getValue(Object object1) {
		return this.valueGetter.applyTo(object1);
	}

	public JsonNodeSelector with(JsonNodeSelector jsonNodeSelector1) {
		return new JsonNodeSelector(new ChainedFunctor(this, jsonNodeSelector1));
	}

	String shortForm() {
		return this.valueGetter.shortForm();
	}

	public String toString() {
		return this.valueGetter.toString();
	}
}
