package argo.jdom;

class JsonListenerToJdomAdapter_Array implements JsonListenerToJdomAdapter_NodeContainer {
	final JsonArrayNodeBuilder nodeBuilder;
	final JsonListenerToJdomAdapter listenerToJdomAdapter;

	JsonListenerToJdomAdapter_Array(JsonListenerToJdomAdapter jsonListenerToJdomAdapter1, JsonArrayNodeBuilder jsonArrayNodeBuilder2) {
		this.listenerToJdomAdapter = jsonListenerToJdomAdapter1;
		this.nodeBuilder = jsonArrayNodeBuilder2;
	}

	public void addNode(JsonNodeBuilder jsonNodeBuilder1) {
		this.nodeBuilder.withElement(jsonNodeBuilder1);
	}

	public void addField(JsonFieldBuilder jsonFieldBuilder1) {
		throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to an array.");
	}
}
