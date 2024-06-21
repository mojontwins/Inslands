package net.minecraft.src;

class StructureNetherBridgePieceWeight {
	public Class field_40699_a;
	public final int field_40697_b;
	public int field_40698_c;
	public int field_40695_d;
	public boolean field_40696_e;

	public StructureNetherBridgePieceWeight(Class class1, int i2, int i3, boolean z4) {
		this.field_40699_a = class1;
		this.field_40697_b = i2;
		this.field_40695_d = i3;
		this.field_40696_e = z4;
	}

	public StructureNetherBridgePieceWeight(Class class1, int i2, int i3) {
		this(class1, i2, i3, false);
	}

	public boolean func_40693_a(int i1) {
		return this.field_40695_d == 0 || this.field_40698_c < this.field_40695_d;
	}

	public boolean func_40694_a() {
		return this.field_40695_d == 0 || this.field_40698_c < this.field_40695_d;
	}
}
