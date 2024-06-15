package net.minecraft.src;

public class WorldProviderAether extends WorldProvider {
	private float[] colorsSunriseSunset = new float[4];

	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerAether(1.0D);
		this.worldType = 1;
	}

	public IChunkProvider getChunkProvider() {
		return new ChunkProviderAether(this.worldObj, this.worldObj.getRandomSeed());
	}

	public float calculateCelestialAngle(long l, float f) {
		boolean hasKilledGold = ModLoader.getMinecraftInstance().statFileWriter.hasAchievementUnlocked(AetherAchievements.defeatGold);
		if(hasKilledGold) {
			int timeTicks = (int)(l % 80000L);
			float timeFraction = ((float)timeTicks + f) / 120000.0F - 0.25F;
			if(timeTicks > 60000) {
				timeTicks -= 40000;
				timeFraction = ((float)timeTicks + f) / 20000.0F - 0.25F;
			}

			if(timeFraction < 0.0F) {
				++timeFraction;
			}

			if(timeFraction > 1.0F) {
				--timeFraction;
			}

			float f2 = timeFraction;
			timeFraction = 1.0F - (float)((Math.cos((double)timeFraction * Math.PI) + 1.0D) / 2.0D);
			timeFraction = f2 + (timeFraction - f2) / 3.0F;
			return timeFraction;
		} else {
			return 0.0F;
		}
	}

	public float[] calcSunriseSunsetColors(float f, float f1) {
		float f2 = 0.4F;
		float f3 = MathHelper.cos(f * 3.141593F * 2.0F) - 0.0F;
		float f4 = -0.0F;
		if(f3 >= f4 - f2 && f3 <= f4 + f2) {
			float f5 = (f3 - f4) / f2 * 0.5F + 0.5F;
			float f6 = 1.0F - (1.0F - MathHelper.sin(f5 * 3.141593F)) * 0.99F;
			f6 *= f6;
			this.colorsSunriseSunset[0] = f5 * 0.3F + 0.1F;
			this.colorsSunriseSunset[1] = f5 * f5 * 0.7F + 0.2F;
			this.colorsSunriseSunset[2] = f5 * f5 * 0.7F + 0.2F;
			this.colorsSunriseSunset[3] = f6;
			return this.colorsSunriseSunset;
		} else {
			return null;
		}
	}

	public Vec3D func_4096_a(float f, float f1) {
		int i = 8421536;
		float f2 = MathHelper.cos(f * 3.141593F * 2.0F) * 2.0F + 0.5F;
		if(f2 < 0.0F) {
			f2 = 0.0F;
		}

		if(f2 > 1.0F) {
			f2 = 1.0F;
		}

		float f3 = (float)(i >> 16 & 255) / 255.0F;
		float f4 = (float)(i >> 8 & 255) / 255.0F;
		float f5 = (float)(i & 255) / 255.0F;
		f3 *= f2 * 0.94F + 0.06F;
		f4 *= f2 * 0.94F + 0.06F;
		f5 *= f2 * 0.91F + 0.09F;
		return Vec3D.createVector((double)f3, (double)f4, (double)f5);
	}

	public boolean func_28112_c() {
		return false;
	}

	public float getCloudHeight() {
		return 8.0F;
	}

	public boolean canCoordinateBeSpawn(int i, int j) {
		int k = this.worldObj.getFirstUncoveredBlock(i, j);
		return k == 0 ? false : Block.blocksList[k].blockMaterial.getIsSolid();
	}

	public boolean canRespawnHere() {
		return false;
	}
}
