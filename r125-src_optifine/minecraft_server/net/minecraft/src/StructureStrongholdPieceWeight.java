package net.minecraft.src;

class StructureStrongholdPieceWeight {
	public Class pieceClass;
	public final int pieceWeight;
	public int instancesSpawned;
	public int instancesLimit;

	public StructureStrongholdPieceWeight(Class class1, int i2, int i3) {
		this.pieceClass = class1;
		this.pieceWeight = i2;
		this.instancesLimit = i3;
	}

	public boolean canSpawnMoreStructuresOfType(int i1) {
		return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
	}

	public boolean canSpawnMoreStructures() {
		return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
	}
}
