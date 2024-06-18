package argo.jdom;

import java.util.HashMap;
import java.util.Iterator;

class JsonObjectNodeBuilder_List extends HashMap {
	final JsonObjectNodeBuilder nodeBuilder;

	JsonObjectNodeBuilder_List(JsonObjectNodeBuilder jsonObjectNodeBuilder1) {
		this.nodeBuilder = jsonObjectNodeBuilder1;
		Iterator iterator2 = JsonObjectNodeBuilder.func_27236_a(this.nodeBuilder).iterator();

		while(iterator2.hasNext()) {
			JsonFieldBuilder jsonFieldBuilder3 = (JsonFieldBuilder)iterator2.next();
			this.put(jsonFieldBuilder3.func_27303_b(), jsonFieldBuilder3.buildValue());
		}

	}
}
