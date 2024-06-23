package net.minecraft.src;

import java.util.List;
import java.util.Map;

public abstract class J_JsonNode {
	public abstract EnumJsonNodeType getType();

	public abstract String getText();

	public abstract Map<J_JsonStringNode,J_JsonNode> getFields();

	public abstract List<J_JsonNode> getElements();

	public final String getStringValue(Object... object1) {
		return (String)this.wrapExceptionsFor(J_JsonNodeSelectors.aStringNode(object1), this, object1);
	}
	
	public final String getNumberValue(Object... object1) {
		return (String)this.wrapExceptionsFor(J_JsonNodeSelectors.aNumberNode(object1), this, object1);
	}

	@SuppressWarnings("unchecked")
	public final List<J_JsonNode> getArrayNode(Object... object1) {
		return (List<J_JsonNode>)this.wrapExceptionsFor(J_JsonNodeSelectors.anArrayNode(object1), this, object1);
	}
	
    public J_JsonObject getObjectNode(final Object... pathElements) {
        return (J_JsonObject) wrapExceptionsFor(J_JsonNodeSelectors.anObjectNode(pathElements), this, pathElements);
    }

	private Object wrapExceptionsFor(J_JsonNodeSelector j_JsonNodeSelector1, J_JsonNode j_JsonNode2, Object[] object3) {
		try {
			return j_JsonNodeSelector1.func_27357_b(j_JsonNode2);
		} catch (J_JsonNodeDoesNotMatchChainedJsonNodeSelectorException j_JsonNodeDoesNotMatchChainedJsonNodeSelectorException5) {
			throw J_JsonNodeDoesNotMatchPathElementsException.func_27319_a(j_JsonNodeDoesNotMatchChainedJsonNodeSelectorException5, object3, J_JsonNodeFactories.func_27315_a(new J_JsonNode[]{j_JsonNode2}));
		}
	}
}
