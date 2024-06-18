package argo.jdom;

import argo.format.CompactJsonFormatter;
import argo.format.JsonFormatter;

public final class JsonNodeDoesNotMatchPathElementsException extends JsonNodeDoesNotMatchJsonNodeSelectorException {
	private static final JsonFormatter JSON_FORMATTER = new CompactJsonFormatter();

	static JsonNodeDoesNotMatchPathElementsException jsonNodeDoesNotMatchPathElementsException(JsonNodeDoesNotMatchChainedJsonNodeSelectorException jsonNodeDoesNotMatchChainedJsonNodeSelectorException0, Object[] object1, JsonRootNode jsonRootNode2) {
		return new JsonNodeDoesNotMatchPathElementsException(jsonNodeDoesNotMatchChainedJsonNodeSelectorException0, object1, jsonRootNode2);
	}

	private JsonNodeDoesNotMatchPathElementsException(JsonNodeDoesNotMatchChainedJsonNodeSelectorException jsonNodeDoesNotMatchChainedJsonNodeSelectorException1, Object[] object2, JsonRootNode jsonRootNode3) {
		super(formatMessage(jsonNodeDoesNotMatchChainedJsonNodeSelectorException1, object2, jsonRootNode3));
	}

	private static String formatMessage(JsonNodeDoesNotMatchChainedJsonNodeSelectorException jsonNodeDoesNotMatchChainedJsonNodeSelectorException0, Object[] object1, JsonRootNode jsonRootNode2) {
		return "Failed to find " + jsonNodeDoesNotMatchChainedJsonNodeSelectorException0.failedNode.toString() + " at [" + JsonNodeDoesNotMatchChainedJsonNodeSelectorException.getShortFormFailPath(jsonNodeDoesNotMatchChainedJsonNodeSelectorException0.failPath) + "] while resolving [" + commaSeparate(object1) + "] in " + JSON_FORMATTER.format(jsonRootNode2) + ".";
	}

	private static String commaSeparate(Object[] object0) {
		StringBuilder stringBuilder1 = new StringBuilder();
		boolean z2 = true;
		Object[] object3 = object0;
		int i4 = object0.length;

		for(int i5 = 0; i5 < i4; ++i5) {
			Object object6 = object3[i5];
			if(!z2) {
				stringBuilder1.append(".");
			}

			z2 = false;
			if(object6 instanceof String) {
				stringBuilder1.append("\"").append(object6).append("\"");
			} else {
				stringBuilder1.append(object6);
			}
		}

		return stringBuilder1.toString();
	}
}
