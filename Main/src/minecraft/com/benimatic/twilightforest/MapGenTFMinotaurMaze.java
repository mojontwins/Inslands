package com.benimatic.twilightforest;

import com.mojang.minecraft.structure.MapGenStructure;
import com.mojang.minecraft.structure.StructureStart;

import net.minecraft.src.World;

public class MapGenTFMinotaurMaze extends MapGenStructure {

	@Override
	protected boolean canSpawnStructureAtCoords(World world, int chunkX, int chunkZ) {
		return false;
	}

	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new StructureTFMinotaurMazeStart(this.world, this.rand, chunkX, chunkZ);
	}

}
