package net.minecraft.src;

public class WorldProviderEnd extends WorldProvider {
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.5F, 0.0F);
		this.worldType = 1;
		this.hasNoSky = true;
	}

	public IChunkProvider getChunkProvider() {
		return new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
	}

	public float calculateCelestialAngle(long j1, float f3) {
		return 0.0F;
	}

	public float[] calcSunriseSunsetColors(float f1, float f2) {
		return null;
	}

	public Vec3D getFogColor(float f1, float f2) {
		int i3 = 8421536;
		float f4 = MathHelper.cos(f1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
		if(f4 < 0.0F) {
			f4 = 0.0F;
		}

		if(f4 > 1.0F) {
			f4 = 1.0F;
		}

		float f5 = (float)(i3 >> 16 & 255) / 255.0F;
		float f6 = (float)(i3 >> 8 & 255) / 255.0F;
		float f7 = (float)(i3 & 255) / 255.0F;
		f5 *= f4 * 0.0F + 0.15F;
		f6 *= f4 * 0.0F + 0.15F;
		f7 *= f4 * 0.0F + 0.15F;
		return Vec3D.createVector((double)f5, (double)f6, (double)f7);
	}

	public boolean isSkyColored() {
		return false;
	}

	public boolean canRespawnHere() {
		return false;
	}

	public boolean func_48217_e() {
		return false;
	}

	public float getCloudHeight() {
		return 8.0F;
	}

	public boolean canCoordinateBeSpawn(int i1, int i2) {
		int i3 = this.worldObj.getFirstUncoveredBlock(i1, i2);
		return i3 == 0 ? false : Block.blocksList[i3].blockMaterial.blocksMovement();
	}

	public ChunkCoordinates getEntrancePortalLocation() {
		return new ChunkCoordinates(100, 50, 0);
	}

	public int getAverageGroundLevel() {
		return 50;
	}

	public boolean func_48218_b(int i1, int i2) {
		return true;
	}
}
