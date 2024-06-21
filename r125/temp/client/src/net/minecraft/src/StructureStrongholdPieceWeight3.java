package net.minecraft.src;

final class StructureStrongholdPieceWeight3 extends StructureStrongholdPieceWeight {
	StructureStrongholdPieceWeight3(Class class1, int i2, int i3) {
		super(class1, i2, i3);
	}

	public boolean canSpawnMoreStructuresOfType(int i1) {
		return super.canSpawnMoreStructuresOfType(i1) && i1 > 5;
	}
}
