package com.mojang.minecraft.structure;

public class MapGenMineshaft extends MapGenStructure {
	protected boolean canSpawnStructureAtCoords(int i1, int i2) {
		return this.rand.nextInt(100) == 0 && this.rand.nextInt(80) < Math.max(Math.abs(i1), Math.abs(i2));
	}

	protected StructureStart getStructureStart(int i1, int i2) {
		return new StructureMineshaftStart(this.world, this.rand, i1, i2);
	}
}
