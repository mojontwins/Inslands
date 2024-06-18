package net.minecraft.src;

public class StructureVillagePieceWeight {
	public Class villagePieceClass;
	public final int villagePieceWeight;
	public int villagePiecesSpawned;
	public int villagePiecesLimit;

	public StructureVillagePieceWeight(Class class1, int i2, int i3) {
		this.villagePieceClass = class1;
		this.villagePieceWeight = i2;
		this.villagePiecesLimit = i3;
	}

	public boolean canSpawnMoreVillagePiecesOfType(int i1) {
		return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
	}

	public boolean canSpawnMoreVillagePieces() {
		return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
	}
}
