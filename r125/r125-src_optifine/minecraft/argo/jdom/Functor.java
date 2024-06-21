package argo.jdom;

interface Functor {
	boolean matchesNode(Object object1);

	Object applyTo(Object object1);

	String shortForm();
}
