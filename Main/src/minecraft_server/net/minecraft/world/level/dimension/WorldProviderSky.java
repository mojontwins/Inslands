package net.minecraft.world.level.dimension;

import net.minecraft.src.Block;
import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.levelgen.ChunkProviderSky;

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

	public boolean canCoordinateBeSpawn(int i1, int i2) {
		int i3 = this.worldObj.getFirstUncoveredBlock(i1, i2);
		return i3 == 0 ? false : Block.blocksList[i3].blockMaterial.getIsSolid();
	}
}
