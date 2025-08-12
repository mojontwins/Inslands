package net.minecraft.world.level.levelgen.structure.stronghold;

final class StructureStrongholdPieceWeight2 extends StructureStrongholdPieceWeight {
	StructureStrongholdPieceWeight2(Class<?> pieceClass, int pieceWeight, int instancesLimit) {
		super(pieceClass, pieceWeight, instancesLimit);
	}

	public boolean canSpawnMoreStructuresOfType(int type) {
		return super.canSpawnMoreStructuresOfType(type) && type > 4;
	}
}
