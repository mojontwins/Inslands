package net.minecraft.src;

public class J_JsonNumberNodeSelector extends J_LeafFunctor {

	public J_JsonNumberNodeSelector() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean checkType(Object object1) {
		return this.checkNumber((J_JsonNode)object1);
	}

	@Override
	public String description() {
		return "A number";
	}

	@Override
	protected Object func_27063_c(Object object1) {
		return this.func_27073_b((J_JsonNode)object1);
	}

	public boolean checkNumber(J_JsonNode j_JsonNode1) {
		return EnumJsonNodeType.NUMBER == j_JsonNode1.getType();
	}
	
	public String func_27073_b(J_JsonNode j_JsonNode1) {
		return j_JsonNode1.getText();
	}
}
