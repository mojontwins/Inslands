package net.minecraft.world.level.levelgen;

import net.minecraft.src.Block;
import net.minecraft.src.LevelThemeGlobalSettings;
import net.minecraft.src.World;
import net.minecraft.src.WorldSize;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.levelgen.synth.NoiseGeneratorOctavesIndev;

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
			double dx = Math.abs(((double)x / (double)(WorldSize.width - 1) - 0.5D) * 2.0D);
			for(int z = z0; z < z0 + 16; z ++) {
				double dz = Math.abs(((double)z / (double)(WorldSize.length - 1) - 0.5D) * 2.0D);
				
				BiomeGenBase biome = this.biomesForGeneration[(x & 0xf0) | (z & 0xf)];
				
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
				
				// Get a weighted 2D distance to the center of sorts. This is a cone centered on the whole map area
				double d = Math.sqrt(dx * dx + dz * dz) * 1.2D;
				
				// Get noise
				double noise = this.noiseIslandGen.generateNoise(x * 0.05D, z * 0.05D) / 4.0D + 1.0D;
				
				// Weird a bit with those values (noise and d) to get a nice fried agg shape
				double factor = Math.max(Math.min(d, noise), Math.min(dx, dz));
				
				if(factor > 1.0D) factor = 1.0D;
				if(factor < 0.0D) factor = 0.0D;
				
				// Curve a bit
				factor *= factor;
				
				// Modify by cone
				// Adjust by factor, centered at 64
				double normalizedHeight = (double)height - 64.0D;
				normalizedHeight = normalizedHeight * (1.0D - factor) - factor * 10.0D + 5.0D;
				
				// Deepen oceans
				if(normalizedHeight < 0.0D) {
					normalizedHeight -= normalizedHeight * normalizedHeight * 0.2D;
				}
				
				height = 64 + (int)normalizedHeight;
				
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
						blockID = biome.mainLiquid;
					} else {
						blockID = 0;
					}
					
					blocks[idx ++] = (byte) blockID;
					
					if(y == seaLevel - 1) this.isOcean &= blockID == Block.waterStill.blockID;
				}
			}
		}
	}
	
	@Override
	public void terraform(int chunkX, int chunkZ, Chunk chunk, BiomeGenBase[] biomes) {
		// Already done while world gen
	}
	
}
