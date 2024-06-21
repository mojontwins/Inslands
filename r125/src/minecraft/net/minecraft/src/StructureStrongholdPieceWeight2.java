package net.minecraft.src;

final class StructureStrongholdPieceWeight2 extends StructureStrongholdPieceWeight {
	StructureStrongholdPieceWeight2(Class class1, int i2, int i3) {
		super(class1, i2, i3);
	}

	public boolean canSpawnMoreStructuresOfType(int i1) {
		return super.canSpawnMoreStructuresOfType(i1) && i1 > 4;
	}
}
