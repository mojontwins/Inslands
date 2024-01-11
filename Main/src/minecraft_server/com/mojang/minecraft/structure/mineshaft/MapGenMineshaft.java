package com.mojang.minecraft.structure.mineshaft;

import com.mojang.minecraft.structure.MapGenStructure;
import com.mojang.minecraft.structure.StructureStart;

import net.minecraft.src.World;

public class MapGenMineshaft extends MapGenStructure {
	public MapGenMineshaft(World world) {
		this.world = world;
	}
	
	protected boolean canSpawnStructureAtCoords(World world, int cX, int cZ) {
		return this.rand.nextInt(100) == 0 && this.rand.nextInt(80) < Math.max(Math.abs(cX), Math.abs(cZ));
	}

	protected StructureStart getStructureStart(int cX, int cZ) {
		return new StructureMineshaftStart(this.world, this.rand, cX, cZ);
	}
}
