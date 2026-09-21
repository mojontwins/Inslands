package net.minecraft.src;

class WorldBlockPositionType {
	int posX;
	int posY;
	int posZ;
	int field_1206_d;
	int blockID;
	int metadata;
	final WorldClient field_1203_g;

	public WorldBlockPositionType(WorldClient worldClient1, int i2, int i3, int i4, int i5, int i6) {
		this.field_1203_g = worldClient1;
		this.posX = i2;
		this.posY = i3;
		this.posZ = i4;
		this.field_1206_d = 80;
		this.blockID = i5;
		this.metadata = i6;
	}
}
