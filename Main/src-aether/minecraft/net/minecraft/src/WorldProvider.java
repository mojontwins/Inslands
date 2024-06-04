package net.minecraft.src;

public abstract class WorldProvider {
	public World worldObj;
	public WorldChunkManager worldChunkMgr;
	public boolean isNether = false;
	public boolean isHellWorld = false;
	public boolean hasNoSky = false;
	public float[] lightBrightnessTable = new float[16];
	public int worldType = 0;
	private float[] colorsSunriseSunset = new float[4];

	public final void registerWorld(World paramfd) {
		this.worldObj = paramfd;
		this.registerWorldChunkManager();
		this.generateLightBrightnessTable();
	}

	protected void generateLightBrightnessTable() {
		float f1 = 0.05F;

		for(int i = 0; i <= 15; ++i) {
			float f2 = 1.0F - (float)i / 15.0F;
			this.lightBrightnessTable[i] = (1.0F - f2) / (f2 * 3.0F + 1.0F) * (1.0F - f1) + f1;
		}

	}

	protected void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManager(this.worldObj);
	}

	public IChunkProvider getChunkProvider() {
		return new ChunkProviderGenerate(this.worldObj, this.worldObj.getRandomSeed());
	}

	public boolean canCoordinateBeSpawn(int paramInt1, int paramInt2) {
		int i = this.worldObj.getFirstUncoveredBlock(paramInt1, paramInt2);
		return i == Block.sand.blockID;
	}

	public float calculateCelestialAngle(long paramLong, float paramFloat) {
		int i = (int)(paramLong % 24000L);
		float f1 = ((float)i + paramFloat) / 24000.0F - 0.25F;
		if(f1 < 0.0F) {
			++f1;
		}

		if(f1 > 1.0F) {
			--f1;
		}

		float f2 = f1;
		f1 = 1.0F - (float)((Math.cos((double)f1 * Math.PI) + 1.0D) / 2.0D);
		f1 = f2 + (f1 - f2) / 3.0F;
		return f1;
	}

	public float[] calcSunriseSunsetColors(float paramFloat1, float paramFloat2) {
		float f1 = 0.4F;
		float f2 = MathHelper.cos(paramFloat1 * 3.141593F * 2.0F) - 0.0F;
		float f3 = -0.0F;
		if(f2 >= f3 - f1 && f2 <= f3 + f1) {
			float f4 = (f2 - f3) / f1 * 0.5F + 0.5F;
			float f5 = 1.0F - (1.0F - MathHelper.sin(f4 * 3.141593F)) * 0.99F;
			f5 *= f5;
			this.colorsSunriseSunset[0] = f4 * 0.3F + 0.7F;
			this.colorsSunriseSunset[1] = f4 * f4 * 0.7F + 0.2F;
			this.colorsSunriseSunset[2] = f4 * f4 * 0.0F + 0.2F;
			this.colorsSunriseSunset[3] = f5;
			return this.colorsSunriseSunset;
		} else {
			return null;
		}
	}

	public Vec3D func_4096_a(float paramFloat1, float paramFloat2) {
		float f1 = MathHelper.cos(paramFloat1 * 3.141593F * 2.0F) * 2.0F + 0.5F;
		if(f1 < 0.0F) {
			f1 = 0.0F;
		}

		if(f1 > 1.0F) {
			f1 = 1.0F;
		}

		float f2 = 0.7529412F;
		float f3 = 0.8470588F;
		float f4 = 1.0F;
		f2 *= f1 * 0.94F + 0.06F;
		f3 *= f1 * 0.94F + 0.06F;
		f4 *= f1 * 0.91F + 0.09F;
		return Vec3D.createVector((double)f2, (double)f3, (double)f4);
	}

	public boolean canRespawnHere() {
		return true;
	}

	public static WorldProvider getProviderForDimension(int paramInt) {
		DimensionBase localDimensionBase = DimensionBase.getDimByNumber(paramInt);
		return localDimensionBase != null ? localDimensionBase.getWorldProvider() : null;
	}

	public float getCloudHeight() {
		return 108.0F;
	}

	public boolean func_28112_c() {
		return true;
	}
}
