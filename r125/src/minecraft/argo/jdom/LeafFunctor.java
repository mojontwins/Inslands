package argo.jdom;

abstract class LeafFunctor implements Functor {
	public final Object applyTo(Object object1) {
		if(!this.matchesNode(object1)) {
			throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_27322_a(this);
		} else {
			return this.typeSafeApplyTo(object1);
		}
	}

	protected abstract Object typeSafeApplyTo(Object object1);
}
