package argo.jdom;

import java.util.List;
import java.util.Map;

public abstract class JsonNode {
	public abstract JsonNodeType getType();

	public abstract String getText();

	public abstract Map getFields();

	public abstract List getElements();

	public final String getStringValue(Object... object1) {
		return (String)this.wrapExceptionsFor(JsonNodeSelectors.func_27349_a(object1), this, object1);
	}

	public final List getArrayNode(Object... object1) {
		return (List)this.wrapExceptionsFor(JsonNodeSelectors.func_27346_b(object1), this, object1);
	}

	private Object wrapExceptionsFor(JsonNodeSelector jsonNodeSelector1, JsonNode jsonNode2, Object[] object3) {
		try {
			return jsonNodeSelector1.getValue(jsonNode2);
		} catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException jsonNodeDoesNotMatchChainedJsonNodeSelectorException5) {
			throw JsonNodeDoesNotMatchPathElementsException.jsonNodeDoesNotMatchPathElementsException(jsonNodeDoesNotMatchChainedJsonNodeSelectorException5, object3, JsonNodeFactories.aJsonArray(new JsonNode[]{jsonNode2}));
		}
	}
}
