package net.minecraft.src;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.EntityPlayer;

public class WorldProviderHell extends WorldProvider {
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 1.0D, 0.0D);
		this.isNether = true;
		this.isHellWorld = true;
		this.hasNoSky = true;
		this.worldType = -1;
	}

	public Vec3D getFogColor(float f1, float f2, Entity entity1, boolean blevis) {
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
		return new ChunkProviderHell(this.worldObj, this.worldObj.getRandomSeed());
	}

	public boolean canCoordinateBeSpawn(int i1, int i2) {
		int i3 = this.worldObj.getFirstUncoveredBlock(i1, i2);
		return i3 == Block.bedrock.blockID ? false : (i3 == 0 ? false : Block.opaqueCubeLookup[i3]);
	}

	public float calculateCelestialAngle(long j1, float f3) {
		return 0.5F;
	}

	public boolean canRespawnHere() {
		return false;
	}
	
	@Override
	public int[] updateLightmap(EntityPlayer thePlayer, float gammaSetting) {
		if(LevelThemeGlobalSettings.themeID == LevelThemeSettings.paradise.id) return super.updateLightmap(thePlayer, gammaSetting);
		
		int[] lightmapColors = new int[256];
		
		World world1 = this.worldObj;
		float sb = world1.getSunBrightness(1.0F);
		
		// Reddish & lighter
		for(int i2 = 0; i2 < 256; ++i2) {
			float f3 = sb * 0.79F + 0.2F;
			float f4 = world1.worldProvider.lightBrightnessTable[i2 / 16] * f3;
			float f5 = world1.worldProvider.lightBrightnessTable[i2 % 16];
			if(world1.lightningFlash > 0) {
				f4 = world1.worldProvider.lightBrightnessTable[i2 / 16];
			}

			float f6 = f4 * (sb * 0.55F + 0.45F);
			float rg = f4 + f5;
			float b = f6 + f5;
			
			rg = rg * 0.96F + 0.03F;
			b = b * 0.9F + 0.1F;
			b = b * 1.2F;

			if(world1.worldProvider.worldType == 1) {
				b = rg = 0.22F + f5 * 0.75F;
			}

			// Set up night vision with code somewhat stolen from r1.5.2!
			if(thePlayer.divingHelmetOn() && thePlayer.isInsideOfMaterial(Material.water)) {
				float nVB = 0.5F;					
				float fNV = 1.0F / rg;					
				b = rg = rg * (1.0F - nVB) + rg * fNV * nVB;
			}
			
			if(rg > 1.0F) {
				rg = 1.0F;
			}
			if (b > 1.0F) {
				b = 1.0F;
			}
	
			float f15 = gammaSetting - 0.2F;
			float f16 = 1.0F - rg;
	
			f16 = 1.0F - f16 * f16 * f16 * f16;				
			rg = rg * (1.0F - f15) + f16 * f15;
			rg = rg * 0.96F + 0.03F;
			
			b = b * (1.0F - f15) + f16 * f15;
			b = b * 0.9F + 0.1F;

			if(rg > 1.0F) {
				rg = 1.0F;
			}

			if(rg < 0.0F) {
				rg = 0.0F;
			}
			
			if(b > 1.0F) {
				b = 1.0F;
			}
			
			if (b < 0.0F) {
				b = 0.0F;
			}

			short s19 = 255;
			int i20 = (int)(rg * 255.0F);
			int i21 = (int)(b * 255.0F);
			lightmapColors[i2] = s19 << 24 | i21 << 16 | i20 << 8 | i20;
		}
		
		return lightmapColors;
	}
}
