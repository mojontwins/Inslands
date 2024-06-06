package com.benimatic.twilightforest;

import java.util.Random;

import com.mojang.minecraft.structure.StructureStart;

import net.minecraft.src.World;

public class StructureTFMinotaurMazeStart extends StructureStart {
	public StructureTFMinotaurMazeStart(World world, Random rand, int chunkX, int chunkZ) {
		int x = (chunkX << 4) + 8;
		int z = (chunkZ << 4) + 8;
		int y = world.getWorldInfo().getTerrainType().getSeaLevel(world) + 1;
		ComponentTFMazeRuins lair = new ComponentTFMazeRuins(world, rand, 0, x, y, z);
		this.components.add(lair);
		lair.buildComponent(lair, this.components, rand);
		this.updateBoundingBox();
	}
}
