package com.benimatic.twilightforest;

import java.util.Random;

import com.mojang.minecraft.structure.StructureStart;

import net.minecraft.src.World;

public class StructureTFHedgeMazeStart extends StructureStart {
	public StructureTFHedgeMazeStart(World world, Random rand, int chunkX, int chunkZ) {
		int x = (chunkX << 4) + 8;
		int z = (chunkZ << 4) + 8;
		int y = world.getWorldInfo().getTerrainType().getSeaLevel(world);
		ComponentTFHedgeMaze hedgeMaze = new ComponentTFHedgeMaze(world, rand, 0, x, y, z);
		this.components.add(hedgeMaze);
		hedgeMaze.buildComponent(hedgeMaze, this.components, rand);
		this.updateBoundingBox();
		
		System.out.println ("Hedge maze " + x + " " + y + " " + z);
	}
}
