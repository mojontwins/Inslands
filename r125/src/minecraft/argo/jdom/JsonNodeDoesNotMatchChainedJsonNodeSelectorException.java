package argo.jdom;

import java.util.LinkedList;
import java.util.List;

public final class JsonNodeDoesNotMatchChainedJsonNodeSelectorException extends JsonNodeDoesNotMatchJsonNodeSelectorException {
	final Functor failedNode;
	final List failPath;

	static JsonNodeDoesNotMatchJsonNodeSelectorException func_27322_a(Functor functor0) {
		return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(functor0, new LinkedList());
	}

	static JsonNodeDoesNotMatchJsonNodeSelectorException func_27323_a(JsonNodeDoesNotMatchChainedJsonNodeSelectorException jsonNodeDoesNotMatchChainedJsonNodeSelectorException0, JsonNodeSelector jsonNodeSelector1) {
		LinkedList linkedList2 = new LinkedList(jsonNodeDoesNotMatchChainedJsonNodeSelectorException0.failPath);
		linkedList2.add(jsonNodeSelector1);
		return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(jsonNodeDoesNotMatchChainedJsonNodeSelectorException0.failedNode, linkedList2);
	}

	static JsonNodeDoesNotMatchJsonNodeSelectorException func_27321_b(JsonNodeDoesNotMatchChainedJsonNodeSelectorException jsonNodeDoesNotMatchChainedJsonNodeSelectorException0, JsonNodeSelector jsonNodeSelector1) {
		LinkedList linkedList2 = new LinkedList();
		linkedList2.add(jsonNodeSelector1);
		return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(jsonNodeDoesNotMatchChainedJsonNodeSelectorException0.failedNode, linkedList2);
	}

	private JsonNodeDoesNotMatchChainedJsonNodeSelectorException(Functor functor1, List list2) {
		super("Failed to match any JSON node at [" + getShortFormFailPath(list2) + "]");
		this.failedNode = functor1;
		this.failPath = list2;
	}

	static String getShortFormFailPath(List list0) {
		StringBuilder stringBuilder1 = new StringBuilder();

		for(int i2 = list0.size() - 1; i2 >= 0; --i2) {
			stringBuilder1.append(((JsonNodeSelector)list0.get(i2)).shortForm());
			if(i2 != 0) {
				stringBuilder1.append(".");
			}
		}

		return stringBuilder1.toString();
	}

	public String toString() {
		return "JsonNodeDoesNotMatchJsonNodeSelectorException{failedNode=" + this.failedNode + ", failPath=" + this.failPath + '}';
	}
}
