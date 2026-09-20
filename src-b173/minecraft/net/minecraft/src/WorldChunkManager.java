package net.minecraft.src;

import java.util.Random;

public class WorldChunkManager {
	private NoiseGeneratorOctaves2 ngo1;
	private NoiseGeneratorOctaves2 ngo2;
	private NoiseGeneratorOctaves2 ngo3;
	public double[] temperature;
	public double[] humidity;
	public double[] variation;
	public BiomeGenBase[] generatedBiomes;

	protected WorldChunkManager() {
	}

	public WorldChunkManager(World world1) {
		this.ngo1 = new NoiseGeneratorOctaves2(new Random(world1.getRandomSeed() * 9871L), 4);
		this.ngo2 = new NoiseGeneratorOctaves2(new Random(world1.getRandomSeed() * 39811L), 4);
		this.ngo3 = new NoiseGeneratorOctaves2(new Random(world1.getRandomSeed() * 543321L), 2);
	}

	public BiomeGenBase getBiomeGenAtChunkCoord(ChunkCoordIntPair chunkCoordIntPair1) {
		return this.getBiomeGenAt(chunkCoordIntPair1.chunkXPos << 4, chunkCoordIntPair1.chunkZPos << 4);
	}

	public BiomeGenBase getBiomeGenAt(int i1, int i2) {
		return this.getBiomesForGeneration(i1, i2, 1, 1)[0];
	}

	public double getTemperature(int i1, int i2) {
		this.temperature = this.ngo1.generateNoiseOctaves(this.temperature, (double)i1, (double)i2, 1, 1, 0.02500000037252903D, 0.02500000037252903D, 0.5D);
		return this.temperature[0];
	}

	public BiomeGenBase[] getBiomesForGeneration(int i1, int i2, int i3, int i4) {
		this.generatedBiomes = this.loadBlockGeneratorData(this.generatedBiomes, i1, i2, i3, i4);
		return this.generatedBiomes;
	}

	public double[] getTemperatures(double[] d1, int i2, int i3, int i4, int i5) {
		if(d1 == null || d1.length < i4 * i5) {
			d1 = new double[i4 * i5];
		}

		d1 = this.ngo1.generateNoiseOctaves(d1, (double)i2, (double)i3, i4, i5, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
		this.variation = this.ngo3.generateNoiseOctaves(this.variation, (double)i2, (double)i3, i4, i5, 0.25D, 0.25D, 0.5882352941176471D);
		int i6 = 0;

		for(int i7 = 0; i7 < i4; ++i7) {
			for(int i8 = 0; i8 < i5; ++i8) {
				double d9 = this.variation[i6] * 1.1D + 0.5D;
				double d11 = 0.01D;
				double d13 = 1.0D - d11;
				double d15 = (d1[i6] * 0.15D + 0.7D) * d13 + d9 * d11;
				d15 = 1.0D - (1.0D - d15) * (1.0D - d15);
				if(d15 < 0.0D) {
					d15 = 0.0D;
				}

				if(d15 > 1.0D) {
					d15 = 1.0D;
				}

				d1[i6] = d15;
				++i6;
			}
		}

		return d1;
	}

	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] biomeGenBase1, int i2, int i3, int i4, int i5) {
		if(biomeGenBase1 == null || biomeGenBase1.length < i4 * i5) {
			biomeGenBase1 = new BiomeGenBase[i4 * i5];
		}

		this.temperature = this.ngo1.generateNoiseOctaves(this.temperature, (double)i2, (double)i3, i4, i4, 0.02500000037252903D, 0.02500000037252903D, 0.25D);
		this.humidity = this.ngo2.generateNoiseOctaves(this.humidity, (double)i2, (double)i3, i4, i4, (double)0.05F, (double)0.05F, 0.3333333333333333D);
		this.variation = this.ngo3.generateNoiseOctaves(this.variation, (double)i2, (double)i3, i4, i4, 0.25D, 0.25D, 0.5882352941176471D);
		int i6 = 0;

		for(int i7 = 0; i7 < i4; ++i7) {
			for(int i8 = 0; i8 < i5; ++i8) {
				double d9 = this.variation[i6] * 1.1D + 0.5D;
				double d11 = 0.01D;
				double d13 = 1.0D - d11;
				double d15 = (this.temperature[i6] * 0.15D + 0.7D) * d13 + d9 * d11;
				d11 = 0.002D;
				d13 = 1.0D - d11;
				double d17 = (this.humidity[i6] * 0.15D + 0.5D) * d13 + d9 * d11;
				d15 = 1.0D - (1.0D - d15) * (1.0D - d15);
				if(d15 < 0.0D) {
					d15 = 0.0D;
				}

				if(d17 < 0.0D) {
					d17 = 0.0D;
				}

				if(d15 > 1.0D) {
					d15 = 1.0D;
				}

				if(d17 > 1.0D) {
					d17 = 1.0D;
				}

				this.temperature[i6] = d15;
				this.humidity[i6] = d17;
				biomeGenBase1[i6++] = BiomeGenBase.getBiomeFromLookup(d15, d17);
			}
		}

		return biomeGenBase1;
	}
}
