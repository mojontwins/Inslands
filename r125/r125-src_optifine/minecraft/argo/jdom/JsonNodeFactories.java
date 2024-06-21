package argo.jdom;

import java.util.Arrays;
import java.util.Map;

public final class JsonNodeFactories {
	public static JsonNode aJsonNull() {
		return JsonConstants.NULL;
	}

	public static JsonNode aJsonTrue() {
		return JsonConstants.TRUE;
	}

	public static JsonNode aJsonFalse() {
		return JsonConstants.FALSE;
	}

	public static JsonStringNode aJsonString(String string0) {
		return new JsonStringNode(string0);
	}

	public static JsonNode aJsonNumber(String string0) {
		return new JsonNumberNode(string0);
	}

	public static JsonRootNode aJsonArray(Iterable iterable0) {
		return new JsonArray(iterable0);
	}

	public static JsonRootNode aJsonArray(JsonNode... jsonNode0) {
		return aJsonArray((Iterable)Arrays.asList(jsonNode0));
	}

	public static JsonRootNode aJsonObject(Map map0) {
		return new JsonObject(map0);
	}
}
