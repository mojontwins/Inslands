package net.minecraft.src;

class StructureNetherBridgePieceWeight {
	public Class field_40655_a;
	public final int field_40653_b;
	public int field_40654_c;
	public int field_40651_d;
	public boolean field_40652_e;

	public StructureNetherBridgePieceWeight(Class class1, int i2, int i3, boolean z4) {
		this.field_40655_a = class1;
		this.field_40653_b = i2;
		this.field_40651_d = i3;
		this.field_40652_e = z4;
	}

	public StructureNetherBridgePieceWeight(Class class1, int i2, int i3) {
		this(class1, i2, i3, false);
	}

	public boolean func_40649_a(int i1) {
		return this.field_40651_d == 0 || this.field_40654_c < this.field_40651_d;
	}

	public boolean func_40650_a() {
		return this.field_40651_d == 0 || this.field_40654_c < this.field_40651_d;
	}
}
