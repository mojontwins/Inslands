package net.minecraft.client.json;

import java.util.Map;

final class J_JsonObjectNodeSelector extends J_LeafFunctor {
	public boolean func_27070_a(J_JsonNode j_JsonNode1) {
		return EnumJsonNodeType.OBJECT == j_JsonNode1.getType();
	}

	public String description() {
		return "A short form object";
	}

	public Map<J_JsonStringNode,J_JsonNode> getFields(J_JsonNode j_JsonNode1) {
		return j_JsonNode1.getFields();
	}

	public String toString() {
		return "an object";
	}

	public Object func_27063_c(Object object1) {
		return this.getFields((J_JsonNode)object1);
	}

	public boolean checkType(Object object1) {
		return this.func_27070_a((J_JsonNode)object1);
	}
}
