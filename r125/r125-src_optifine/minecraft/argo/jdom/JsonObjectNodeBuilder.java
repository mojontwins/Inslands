package argo.jdom;

import java.util.LinkedList;
import java.util.List;

public final class JsonObjectNodeBuilder implements JsonNodeBuilder {
	private final List fieldBuilders = new LinkedList();

	public JsonObjectNodeBuilder withFieldBuilder(JsonFieldBuilder jsonFieldBuilder1) {
		this.fieldBuilders.add(jsonFieldBuilder1);
		return this;
	}

	public JsonRootNode func_27235_a() {
		return JsonNodeFactories.aJsonObject(new JsonObjectNodeBuilder_List(this));
	}

	public JsonNode buildNode() {
		return this.func_27235_a();
	}

	static List func_27236_a(JsonObjectNodeBuilder jsonObjectNodeBuilder0) {
		return jsonObjectNodeBuilder0.fieldBuilders;
	}
}
