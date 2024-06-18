package argo.jdom;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class JsonArrayNodeBuilder implements JsonNodeBuilder {
	private final List elementBuilders = new LinkedList();

	public JsonArrayNodeBuilder withElement(JsonNodeBuilder jsonNodeBuilder1) {
		this.elementBuilders.add(jsonNodeBuilder1);
		return this;
	}

	public JsonRootNode build() {
		LinkedList linkedList1 = new LinkedList();
		Iterator iterator2 = this.elementBuilders.iterator();

		while(iterator2.hasNext()) {
			JsonNodeBuilder jsonNodeBuilder3 = (JsonNodeBuilder)iterator2.next();
			linkedList1.add(jsonNodeBuilder3.buildNode());
		}

		return JsonNodeFactories.aJsonArray((Iterable)linkedList1);
	}

	public JsonNode buildNode() {
		return this.build();
	}
}
