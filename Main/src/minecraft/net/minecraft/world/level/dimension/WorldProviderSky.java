package net.minecraft.world.level.dimension;

import java.util.List;

import net.minecraft.util.CoordXZ;
import net.minecraft.util.CoordXZUtils;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.levelgen.ChunkProviderSky;
import net.minecraft.world.level.levelgen.feature.WorldGenIndevHouse;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;
import net.minecraft.world.level.tile.Block;

public class WorldProviderSky extends WorldProvider {

	public IChunkProvider getChunkProvider() {
		return new ChunkProviderSky(this.worldObj, this.worldObj.getRandomSeed());
	}
 
	/*
	public float calculateCelestialAngle(long j1, float f3) {
		return 0.0F;
	}
	*/

	/*
	public float[] calcSunriseSunsetColors(float f1, float f2) {
		return null;
	}
	*/

	public boolean func_28112_c() {
		return false;
	}

	public float getCloudHeight() {
		return 8.0F;
	}

	public boolean canCoordinateBeSpawn(int x, int z) {
		int blockID = this.worldObj.getFirstUncoveredBlock(x, z, true);
		return blockID == 0 ? false : Block.blocksList[blockID].blockMaterial.getIsSolid();
	}
	
	public void getInitialSpawnLocation(World world) {
		world.findingSpawnPoint = LevelThemeGlobalSettings.getTheme().getInitialSpawnLocation(world);
		
		if (world.findingSpawnPoint) {
			// For floating islands we'll use a different approach:
			// We'll calculate a list of CoordXZ ordered by distance
			// to the center. Then we'll iterate it and pick the first
			// one which is valid (i.e. landSurfaceHeight > 8);
			
			List<CoordXZ> coords = CoordXZUtils.getCoordsOrderedFromCenter(WorldSize.width, WorldSize.length, WorldSize.xChunks / 4);
			for(CoordXZ coord : coords) {
				int y = world.getLandSurfaceHeightValue(coord.x, coord.z);

				if(y > 8 && y < 120) {
					// Make sure the indev house will spawn on ground
					
					boolean valid = true;
					for(int x = coord.x - 3; x <= coord.x + 3 && valid; x ++) {
						for(int z = coord.z - 6; z <= coord.z + 3 && valid; z ++) {
							if(world.getLandSurfaceHeightValue(x, z) < 8) valid = false;
						}
					}
					
					if(valid) {
						world.worldInfo.setSpawn(coord.x, y, coord.z);
						world.findingSpawnPoint = false;
						break;
					}
				}
			}
			
			if(!world.findingSpawnPoint) {
				int x = world.worldInfo.getSpawnX();
				int y = world.worldInfo.getSpawnY();
				int z = world.worldInfo.getSpawnZ();
				(new WorldGenIndevHouse(world.getBiomeGenAt(x, z).indevHouseWalls))
				.generate(world, world.rand, x, y + 1, z);
			}
		}
	}
}
