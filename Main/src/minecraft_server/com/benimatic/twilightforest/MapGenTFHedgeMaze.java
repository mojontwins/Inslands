package com.benimatic.twilightforest;

import java.util.Random;

import com.mojang.minecraft.structure.MapGenStructure;
import com.mojang.minecraft.structure.StructureStart;

import net.minecraft.src.LevelThemeGlobalSettings;
import net.minecraft.src.LevelThemeSettings;
import net.minecraft.src.World;
import net.minecraft.src.WorldSize;
import net.minecraft.src.WorldType;

public class MapGenTFHedgeMaze extends MapGenStructure {
	
	public MapGenTFHedgeMaze(World world) {
		this.world = world;
	}
	
	protected boolean canSpawnStructureAtCoords(World world, int chunkX, int chunkZ) {
		if(chunkX < 0 || chunkX >= WorldSize.xChunks || chunkZ < 0 || chunkZ >= WorldSize.zChunks) return false;
		
		if(LevelThemeGlobalSettings.themeID != LevelThemeSettings.forest.id) {
			return false;
		}

		if(world.getWorldInfo().getTerrainType() == WorldType.SKY) {
			return false;
		}
		
		// Seed must be consistent
		long seed = world.getRandomSeed() + chunkX * 25117 + chunkZ * 151121;
		final Random rand = new Random(seed);
		
		boolean res = false;
		if((chunkX & 0x0f) == 8 && (chunkZ & 0x0f) == 8) {
			res = rand.nextBoolean();
		}
		
		return res;
	}

	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new StructureTFHedgeMazeStart(this.world, this.rand, chunkX, chunkZ);
	}


}
