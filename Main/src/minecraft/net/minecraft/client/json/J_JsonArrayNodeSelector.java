package net.minecraft.client.json;

import java.util.List;

final class J_JsonArrayNodeSelector extends J_LeafFunctor {
	public boolean func_27074_a(J_JsonNode j_JsonNode1) {
		return EnumJsonNodeType.ARRAY == j_JsonNode1.getType();
	}

	public String description() {
		return "A short form array";
	}

	public List<J_JsonNode> func_27075_b(J_JsonNode j_JsonNode1) {
		return j_JsonNode1.getElements();
	}

	public String toString() {
		return "an array";
	}

	public Object func_27063_c(Object object1) {
		return this.func_27075_b((J_JsonNode)object1);
	}

	public boolean checkType(Object object1) {
		return this.func_27074_a((J_JsonNode)object1);
	}
}
