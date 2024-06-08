package com.benimatic.twilightforest;

import com.mojang.minecraft.structure.MapGenStructure;
import com.mojang.minecraft.structure.StructureStart;

import net.minecraft.src.LevelThemeGlobalSettings;
import net.minecraft.src.LevelThemeSettings;
import net.minecraft.src.World;
import net.minecraft.src.WorldSize;

public class MapGenTFMinotaurMaze extends MapGenStructure {
	public MapGenTFMinotaurMaze(World world) {
		this.world = world;
	}
		
	@Override
	protected boolean canSpawnStructureAtCoords(World world, int chunkX, int chunkZ) {
		return LevelThemeGlobalSettings.themeID == LevelThemeSettings.forest.id && chunkX == WorldSize.xChunks / 2 && chunkZ == WorldSize.zChunks / 2;
	}

	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new StructureTFMinotaurMazeStart(this.world, this.rand, chunkX, chunkZ);
	}

}
