package net.minecraft.src;

import java.util.Map;

final class J_JsonFieldNodeSelector extends J_LeafFunctor {
	final J_JsonStringNode field_27066_a;

	J_JsonFieldNodeSelector(J_JsonStringNode j_JsonStringNode1) {
		this.field_27066_a = j_JsonStringNode1;
	}

	public boolean func_27065_a(Map<J_JsonStringNode,J_JsonNode> map1) {
		return map1.containsKey(this.field_27066_a);
	}

	public String description() {
		return "\"" + this.field_27066_a.getText() + "\"";
	}

	public J_JsonNode func_27064_b(Map<J_JsonStringNode,J_JsonNode> map1) {
		return (J_JsonNode)map1.get(this.field_27066_a);
	}

	public String toString() {
		return "a field called [\"" + this.field_27066_a.getText() + "\"]";
	}

	@SuppressWarnings("unchecked")
	public Object func_27063_c(Object object1) {
		return this.func_27064_b((Map<J_JsonStringNode,J_JsonNode>)object1);
	}

	@SuppressWarnings("unchecked")
	public boolean checkType(Object object1) {
		return this.func_27065_a((Map<J_JsonStringNode,J_JsonNode>)object1);
	}
}
