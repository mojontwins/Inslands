package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class ComponentVillageStartPiece extends ComponentVillageWell {
	public WorldChunkManager worldChunkMngr;
	public int terrainType;
	public StructureVillagePieceWeight structVillagePieceWeight;
	public ArrayList structureVillageWeightedPieceList;
	public ArrayList field_35389_e = new ArrayList();
	public ArrayList field_35387_f = new ArrayList();

	public ComponentVillageStartPiece(WorldChunkManager worldChunkManager1, int i2, Random random3, int i4, int i5, ArrayList arrayList6, int i7) {
		super(0, random3, i4, i5);
		this.worldChunkMngr = worldChunkManager1;
		this.structureVillageWeightedPieceList = arrayList6;
		this.terrainType = i7;
	}

	public WorldChunkManager getWorldChunkManager() {
		return this.worldChunkMngr;
	}
}
