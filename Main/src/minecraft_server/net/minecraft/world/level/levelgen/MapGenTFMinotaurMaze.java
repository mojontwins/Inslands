package net.minecraft.world.level.levelgen;

import net.minecraft.src.World;
import net.minecraft.src.WorldSize;
import net.minecraft.src.WorldType;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.minotaurmaze.StructureTFMinotaurMazeStart;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;
import net.minecraft.world.level.theme.LevelThemeSettings;

public class MapGenTFMinotaurMaze extends MapGenStructure {
	public MapGenTFMinotaurMaze(World world) {
		this.world = world;
	}
		
	@Override
	protected boolean canSpawnStructureAtCoords(World world, int chunkX, int chunkZ) {
		if(chunkX < 0 || chunkX >= WorldSize.xChunks || chunkZ < 0 || chunkZ >= WorldSize.zChunks) return false;
		
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
			res = WorldSize.xChunks <= 16 || rand.nextInt(3) == 0;
		}
		
		return res;
	}

	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new StructureTFMinotaurMazeStart(this.world, this.rand, chunkX, chunkZ);
	}

}
