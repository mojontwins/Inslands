package com.benimatic.twilightforest;

import java.util.Random;

import com.mojang.minecraft.structure.StructureStart;

import net.minecraft.src.GlobalVars;
import net.minecraft.src.World;

public class StructureTFMinotaurMazeStart extends StructureStart {
	public StructureTFMinotaurMazeStart(World world, Random rand, int chunkX, int chunkZ) {
		int x = (chunkX << 4) + rand.nextInt(16) - 8;
		int z = (chunkZ << 4) + rand.nextInt(16) - 8;
		int y = world.getLandSurfaceHeightValue(x, z);
		
		// Condition so the majority of this maze spawns under the sea level
		if(y > world.getWorldInfo().getTerrainType().getSeaLevel(world) + 14) {
			// Don't generate shit
			return; 
		}
		
		System.out.println ("Minotaur maze" + x + " " + y + " " + z);
		GlobalVars.hasCorrectMinoshroomMaze = true;
		
		ComponentTFMazeRuins lair = new ComponentTFMazeRuins(world, rand, 0, x, y, z);
		this.components.add(lair);
		lair.buildComponent(lair, this.components, rand);
		this.updateBoundingBox();
	}
}
