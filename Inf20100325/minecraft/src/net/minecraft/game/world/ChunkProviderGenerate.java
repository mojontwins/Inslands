package net.minecraft.game.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import net.minecraft.game.world.block.Block;
import net.minecraft.game.world.terrain.noise.NoiseGeneratorOctaves;

public class ChunkProviderGenerate {
	private Random rand = new Random();
	private NoiseGeneratorOctaves noiseGen1 = new NoiseGeneratorOctaves(16);
	private NoiseGeneratorOctaves noiseGen2 = new NoiseGeneratorOctaves(16);
	private NoiseGeneratorOctaves noiseGen3 = new NoiseGeneratorOctaves(8);
	private NoiseGeneratorOctaves noiseGen4 = new NoiseGeneratorOctaves(4);
	private NoiseGeneratorOctaves noiseGen5 = new NoiseGeneratorOctaves(4);
	private NoiseGeneratorOctaves noiseGen6 = new NoiseGeneratorOctaves(5);
	private Map chunks;

	public int getBlockAt(int var1, int var2, int var3) {
		return var2 < 0 ? Block.lavaStill.blockID : (var2 >= 128 ? 0 : this.getBlockAtXZ(var1 >>> 4, var3 >>> 4).getBlockId(var1 & 15, var2, var3 & 15));
	}

	public boolean setBlockAt(int var1, int var2, int var3, int var4) {
		if(var2 < 0) {
			return false;
		} else if(var2 >= 128) {
			return false;
		} else {
			Chunk var5 = this.getBlockAtXZ(var1 >>> 4, var3 >>> 4);
			var1 &= 15;
			var3 &= 15;
			if((var5.getBlockId(var1, var2, var3) & 255) == var4) {
				return false;
			} else {
				var5.setBlockId(var1, var2, var3, var4);
				return true;
			}
		}
	}

	public ChunkProviderGenerate() {
		new NoiseGeneratorOctaves(3);
		new NoiseGeneratorOctaves(3);
		new NoiseGeneratorOctaves(3);
		this.chunks = new HashMap();
	}

	private Chunk getBlockAtXZ(int var1, int var2) {
		ChunkPosition var3 = new ChunkPosition(this, var1, var2);
		Chunk var4 = (Chunk)this.chunks.get(var3);
		if(var4 == null) {
			var4 = new Chunk(this, this.provideChunk(var3));
			this.chunks.put(var3, var4);
		}

		return var4;
	}

	public byte[] provideChunk(ChunkPosition position) {
		byte[] blocks = new byte[-Short.MIN_VALUE];
		int x0 = position.xPosition << 4;
		int z0 = position.zPosition << 4;
		int idx = 0;

		for(int x = x0; x < x0 + 16; ++x) {
			for(int z = z0; z < z0 + 16; ++z) {
				int xRegion = x / 1024;
				int zRegion = z / 1024;

				// Gen noise to build surface height

				float noiseBase = (float)(this.noiseGen1.generateNoise((double)((float)x / 0.03125F), 0.0D, (double)((float)z / 0.03125F)) - this.noiseGen2.generateNoise((double)((float)x / 0.015625F), 0.0D, (double)((float)z / 0.015625F))) / 512.0F / 4.0F;
				float noise = (float)this.noiseGen5.generateNoise((double)((float)x / 4.0F), (double)((float)z / 4.0F));
				float noiseHi = (float)this.noiseGen6.generateNoise((double)((float)x / 8.0F), (double)((float)z / 8.0F)) / 8.0F;
				
				noise = noise > 0.0F ? 
						(float)(this.noiseGen3.generateNoise((double)((float)x * 0.25714284F * 2.0F), (double)((float)z * 0.25714284F * 2.0F)) * (double)noiseHi / 4.0D) 
					: 
						(float)(this.noiseGen4.generateNoise((double)((float)x * 0.25714284F), (double)((float)z * 0.25714284F)) * (double)noiseHi);

				int surfaceHeight = (int)(noiseBase + 64.0F + noise);
				
				// Indev "erode / raise"
				if((float)this.noiseGen5.generateNoise((double)x, (double)z) < 0.0F) {
					surfaceHeight = surfaceHeight / 2 << 1;
					if((float)this.noiseGen5.generateNoise((double)(x / 5), (double)(z / 5)) < 0.0F) {
						++surfaceHeight;
					}
				}

				// Fill with blocks

				for(int y = 0; y < 128; ++y) {

					int blockID = 0;
					if((x == 0 || z == 0) && y <= surfaceHeight + 2) {
						blockID = Block.obsidian.blockID;
					} else if(y == surfaceHeight + 1 && surfaceHeight >= 64 && Math.random() < 0.02D) {
						blockID = Block.plantYellow.blockID;
					} else if(y == surfaceHeight && surfaceHeight >= 64) {
						blockID = Block.grass.blockID;
					} else if(y <= surfaceHeight - 2) {
						blockID = Block.stone.blockID;
					} else if(y <= surfaceHeight) {
						blockID = Block.dirt.blockID;
					} else if(y <= 64) {
						blockID = Block.waterStill.blockID;
					}

					// Make pyramids

					this.rand.setSeed((long)(xRegion + zRegion * 13871));
					int var12 = (xRegion << 10) + 128 + this.rand.nextInt(512);
					int var13 = (zRegion << 10) + 128 + this.rand.nextInt(512);
					var12 = x - var12;
					var13 = z - var13;
					if(var12 < 0) {
						var12 = -var12;
					}

					if(var13 < 0) {
						var13 = -var13;
					}

					if(var13 > var12) {
						var12 = var13;
					}

					var12 = 127 - var12;
					if(var12 == 255) {
						var12 = 1;
					}

					if(var12 < surfaceHeight) {
						var12 = surfaceHeight;
					}

					if(y <= var12 && (blockID == 0 || blockID == Block.waterStill.blockID)) {
						blockID = Block.brick.blockID;
					}

					if(blockID < 0) {
						blockID = 0;
					}

					blocks[idx++] = (byte)blockID;
				}
			}
		}

		return blocks;
	}
}
