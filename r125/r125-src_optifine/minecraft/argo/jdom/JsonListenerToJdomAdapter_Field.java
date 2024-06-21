package argo.jdom;

class JsonListenerToJdomAdapter_Field implements JsonListenerToJdomAdapter_NodeContainer {
	final JsonFieldBuilder fieldBuilder;
	final JsonListenerToJdomAdapter listenerToJdomAdapter;

	JsonListenerToJdomAdapter_Field(JsonListenerToJdomAdapter jsonListenerToJdomAdapter1, JsonFieldBuilder jsonFieldBuilder2) {
		this.listenerToJdomAdapter = jsonListenerToJdomAdapter1;
		this.fieldBuilder = jsonFieldBuilder2;
	}

	public void addNode(JsonNodeBuilder jsonNodeBuilder1) {
		this.fieldBuilder.withValue(jsonNodeBuilder1);
	}

	public void addField(JsonFieldBuilder jsonFieldBuilder1) {
		throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to a field.");
	}
}
