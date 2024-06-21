package argo.jdom;

class JsonListenerToJdomAdapter_Object implements JsonListenerToJdomAdapter_NodeContainer {
	final JsonObjectNodeBuilder nodeBuilder;
	final JsonListenerToJdomAdapter listenerToJdomAdapter;

	JsonListenerToJdomAdapter_Object(JsonListenerToJdomAdapter jsonListenerToJdomAdapter1, JsonObjectNodeBuilder jsonObjectNodeBuilder2) {
		this.listenerToJdomAdapter = jsonListenerToJdomAdapter1;
		this.nodeBuilder = jsonObjectNodeBuilder2;
	}

	public void addNode(JsonNodeBuilder jsonNodeBuilder1) {
		throw new RuntimeException("Coding failure in Argo:  Attempt to add a node to an object.");
	}

	public void addField(JsonFieldBuilder jsonFieldBuilder1) {
		this.nodeBuilder.withFieldBuilder(jsonFieldBuilder1);
	}
}
