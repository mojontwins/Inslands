package com.benimatic.twilightforest;

import com.mojang.minecraft.structure.MapGenStructure;
import com.mojang.minecraft.structure.StructureStart;

import net.minecraft.src.LevelThemeGlobalSettings;
import net.minecraft.src.LevelThemeSettings;
import net.minecraft.src.World;
import net.minecraft.src.WorldSize;
import net.minecraft.src.WorldType;

public class MapGenTFMinotaurMaze extends MapGenStructure {
	public MapGenTFMinotaurMaze(World world) {
		this.world = world;
	}
		
	@Override
	protected boolean canSpawnStructureAtCoords(World world, int chunkX, int chunkZ) {
		if(LevelThemeGlobalSettings.themeID != LevelThemeSettings.forest.id) {
			return false;
		}

		if(world.getWorldInfo().getTerrainType() == WorldType.SKY) {
			return false;
		}
		
		// TODO: Check if this maze breaks small level and don't use it
		if(WorldSize.xChunks < 16) return chunkX == WorldSize.xChunks / 2 && chunkZ == WorldSize.zChunks / 2;
		//
		
		boolean res = false;
		if((chunkX & 0x0f) == 8 && (chunkZ & 0x0f) == 8) {
			res = rand.nextInt(3) == 0;
		}
		
		return res;
	}

	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new StructureTFMinotaurMazeStart(this.world, this.rand, chunkX, chunkZ);
	}

}
