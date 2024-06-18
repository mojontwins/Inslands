package net.minecraft.src;

public class WorldProviderHell extends WorldProvider {
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 1.0F, 0.0F);
		this.isHellWorld = true;
		this.hasNoSky = true;
		this.worldType = -1;
	}

	public Vec3D getFogColor(float f1, float f2) {
		return Vec3D.createVector((double)0.2F, (double)0.03F, (double)0.03F);
	}

	protected void generateLightBrightnessTable() {
		float f1 = 0.1F;

		for(int i2 = 0; i2 <= 15; ++i2) {
			float f3 = 1.0F - (float)i2 / 15.0F;
			this.lightBrightnessTable[i2] = (1.0F - f3) / (f3 * 3.0F + 1.0F) * (1.0F - f1) + f1;
		}

	}

	public IChunkProvider getChunkProvider() {
		return new ChunkProviderHell(this.worldObj, this.worldObj.getSeed());
	}

	public boolean func_48217_e() {
		return false;
	}

	public boolean canCoordinateBeSpawn(int i1, int i2) {
		return false;
	}

	public float calculateCelestialAngle(long j1, float f3) {
		return 0.5F;
	}

	public boolean canRespawnHere() {
		return false;
	}

	public boolean func_48218_b(int i1, int i2) {
		return true;
	}
}
