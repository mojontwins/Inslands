package com.mojang.minecraft.indev;

import net.minecraft.src.Block;
import net.minecraft.src.ChunkProviderGenerate;
import net.minecraft.src.LevelThemeGlobalSettings;
import net.minecraft.src.World;

public class ChunkProviderIndev extends ChunkProviderGenerate {
	private NoiseGeneratorOctavesIndev noise1;
	private NoiseGeneratorOctavesIndev noise2;
	private NoiseGeneratorOctavesIndev noise3;
	private NoiseGeneratorOctavesIndev noise4;
	private NoiseGeneratorOctavesIndev noise5;
	private NoiseGeneratorOctavesIndev noise6;
	
	public ChunkProviderIndev(World world, long seed) {
		this(world, seed, true);
	}
	
	public ChunkProviderIndev(World world, long seed, boolean b) {
		super(world, seed, b);

		this.noise1 = new NoiseGeneratorOctavesIndev(this.rand, 16);
		this.noise2 = new NoiseGeneratorOctavesIndev(this.rand, 16);
		this.noise3 = new NoiseGeneratorOctavesIndev(this.rand, 8);
		this.noise4 = new NoiseGeneratorOctavesIndev(this.rand, 4);
		this.noise5 = new NoiseGeneratorOctavesIndev(this.rand, 4);
		this.noise6 = new NoiseGeneratorOctavesIndev(this.rand, 5);
	}

	@Override
	public void generateTerrain(int chunkX, int chunkZ, byte[] blocks) {
		final byte seaLevel = 64;
		this.isOcean = true;
		
		// Adapted & simplified from Inf227
		
		int x0 = chunkX << 4;
		int z0 = chunkZ << 4;
		
		// Index to the block array
		int idx = 0;
		
		for(int x = x0; x < x0 + 16; x ++) {
			for(int z = z0; z < z0 + 16; z ++) {
				
				float baseHeight = (float)(
						this.noise1.generateNoiseOctaves(x / 0.03125F, 0, z / 0.03125F) -
						this.noise2.generateNoiseOctaves(x / 0.015625F, 0, z / 0.015625F)
				) / 2048F;
						
				float bump = (float) this.noise4.generateNoise(x / 4F, z / 4F);
				float jitter = (float) this.noise6.generateNoise(x / 8F, z / 8F) / 8F;
				
				float rise = bump <= 0 ?
						(float) this.noise5.generateNoise(x * 0.2571428F, z * 0.2571428F) * jitter
					:
						(float) this.noise3.generateNoise(x * 0.2571428F * 2, z * 0.2571428F * 2) * jitter / 4F;
				
				int height = (int) (baseHeight + 64 + rise);
				
				// Modify per level theme
				
				height = LevelThemeGlobalSettings.getTheme().adjustPerlinHeight(height);
				
				// Erode - raise
				
				if(this.noise5.generateNoise(x, z) < 0) {
					height &= 0xFFFFFFFE;
					if(this.noise5.generateNoise(x / 5F, z / 5F) < 0) {
						height ++;
					}
				}
				
				int blockID;
				for(int y = 0; y < 128; y ++) {
					
					if(y < height) {
						blockID = Block.stone.blockID;
					} else if(y < 64) {
						blockID = Block.waterStill.blockID;
					} else {
						blockID = 0;
					}
					
					blocks[idx ++] = (byte) blockID;
					
					if(y == seaLevel - 1) this.isOcean &= blockID == Block.waterStill.blockID;
				}
			}
		}
	}
}
