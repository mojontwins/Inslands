package net.minecraft.world.level.dimension;

import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.levelgen.ChunkProviderSky;
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
}
