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
		if(LevelThemeGlobalSettings.themeID != LevelThemeSettings.forest.id) return false;
		if(world.getWorldInfo().getTerrainType() == WorldType.SKY) return false;
		
		// TODO: Check if this maze breaks small level and don't use it
		if(WorldSize.xChunks < 16) return chunkX == WorldSize.xChunks / 2 && chunkZ == WorldSize.zChunks / 2;
		//
		
		return chunkX == (WorldSize.xChunks & 0xf0 | 8) && chunkZ == (WorldSize.zChunks & 0xf0 | 8) &&
				rand.nextBoolean();
	}

	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new StructureTFMinotaurMazeStart(this.world, this.rand, chunkX, chunkZ);
	}

}
