package argo.jdom;

import java.util.ArrayList;
import java.util.Iterator;

final class JsonArray_NodeList extends ArrayList {
	final Iterable field_27405_a;

	JsonArray_NodeList(Iterable iterable1) {
		this.field_27405_a = iterable1;
		Iterator iterator2 = this.field_27405_a.iterator();

		while(iterator2.hasNext()) {
			JsonNode jsonNode3 = (JsonNode)iterator2.next();
			this.add(jsonNode3);
		}

	}
}
