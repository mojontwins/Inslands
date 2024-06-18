package argo.jdom;

final class JsonFieldBuilder {
	private JsonNodeBuilder key;
	private JsonNodeBuilder valueBuilder;

	static JsonFieldBuilder aJsonFieldBuilder() {
		return new JsonFieldBuilder();
	}

	JsonFieldBuilder withKey(JsonNodeBuilder jsonNodeBuilder1) {
		this.key = jsonNodeBuilder1;
		return this;
	}

	JsonFieldBuilder withValue(JsonNodeBuilder jsonNodeBuilder1) {
		this.valueBuilder = jsonNodeBuilder1;
		return this;
	}

	JsonStringNode func_27303_b() {
		return (JsonStringNode)this.key.buildNode();
	}

	JsonNode buildValue() {
		return this.valueBuilder.buildNode();
	}
}
