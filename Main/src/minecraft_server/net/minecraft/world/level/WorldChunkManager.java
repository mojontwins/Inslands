package net.minecraft.world.level;

import java.util.Random;

import net.minecraft.world.GlobalVars;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.chunk.ChunkCoordIntPair;
import net.minecraft.world.level.levelgen.synth.NoiseGeneratorOctaves2;
import net.minecraft.world.level.levelgen.synth.NoiseGeneratorPerlin;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;

public class WorldChunkManager {
	// Noise generators for climate noise
	private NoiseGeneratorOctaves2 temperatureNoiseGen;
	private NoiseGeneratorOctaves2 humidityNoiseGen;
	private NoiseGeneratorOctaves2 variationNoiseGen;
	private NoiseGeneratorPerlin altChunkNoiseGen;
	
	// Scratch buffers reused across calls. Each loadBlockGeneratorData() call overwrites
	// them. They hold the post-processed temperature / humidity for the most recently
	// generated x/z grid. Values are copied into Chunk.temperatureCache / humidityCache
	// during provideChunk() so later calls cannot clobber them.
	public double[] temperatureScratch;
	public double[] humidityScratch;
	public double[] variationScratch;
	public BiomeGenBase[] generatedBiomes;

	protected WorldChunkManager() {
	}

	public WorldChunkManager(World world) {
		this.temperatureNoiseGen = new NoiseGeneratorOctaves2(new Random(world.getRandomSeed() * 9871L), 4);
		this.humidityNoiseGen = new NoiseGeneratorOctaves2(new Random(world.getRandomSeed() * 39811L), 4);
		this.variationNoiseGen = new NoiseGeneratorOctaves2(new Random(world.getRandomSeed() * 543321L), 2);
		
		this.altChunkNoiseGen = new NoiseGeneratorPerlin(new Random(world.getRandomSeed() * 9871L));
	}
	
	// For easy alternate chunk decoration in single biome themes (or whatever) you can use this
	public boolean isAltChunk(int chunkX, int chunkZ, double threshold) {
	    return this.altChunkNoiseGen.generateNoise(chunkX / 16.0F, chunkZ / 16.0F) > threshold;
	}

	public BiomeGenBase getBiomeGenAtChunkCoord(ChunkCoordIntPair chunkCoordIntPair1) {
		return this.getBiomeGenAt(chunkCoordIntPair1.chunkXPos << 4, chunkCoordIntPair1.chunkZPos << 4);
	}

	public BiomeGenBase getBiomeGenAt(int i1, int i2) {
		return this.getBiomesForGeneration(i1, i2, 1, 1)[0];
	}

	public BiomeGenBase[] getBiomesForGeneration(int i1, int i2, int i3, int i4) {
		this.generatedBiomes = this.loadBlockGeneratorData(this.generatedBiomes, i1, i2, i3, i4);
		return this.generatedBiomes;
	}

	// Calculate which biome
	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] biomeGenArray, int xPos, int zPos, int width, int length) {
		if(biomeGenArray == null || biomeGenArray.length < width * length) {
			biomeGenArray = new BiomeGenBase[width * length];
		}
		
		xPos += GlobalVars.noiseOffsetX;
		zPos += GlobalVars.noiseOffsetZ;
		
		this.temperatureScratch = this.temperatureNoiseGen.generateNoiseOctaves(this.temperatureScratch, (double)xPos, (double)zPos, width, length, 0.025D, 0.025D, 0.25D);
		this.humidityScratch = this.humidityNoiseGen.generateNoiseOctaves(this.humidityScratch, (double)xPos, (double)zPos, width, length, 0.05D, 0.05D, 0.33D);
		this.variationScratch = this.variationNoiseGen.generateNoiseOctaves(this.variationScratch, (double)xPos, (double)zPos, width, length, 0.25D, 0.25D, 0.5882352941176471D);
		
		/*
		this.temperatureScratch = this.temperatureNoiseGen.generateNoiseOctaves(this.temperatureScratch, (double)xPos, (double)zPos, width, length, 0.04D, 0.4D, 0.25D);
		this.humidityScratch = this.humidityNoiseGen.generateNoiseOctaves(this.humidityScratch, (double)xPos, (double)zPos, width, length, 0.08D, 0.08D, 0.33D);
		this.variationScratch = this.variationNoiseGen.generateNoiseOctaves(this.variationScratch, (double)xPos, (double)zPos, width, length, 0.25D, 0.25D, 0.5882352941176471D);
		*/
		int biomeIndex = 0;

		for(int x = 0; x < width; ++x) {
			for(int z = 0; z < length; ++z) {
				BiomeGenBase biome;
				double temperature;
				double humidity;
				
				if(LevelThemeGlobalSettings.levelThemeMainBiome != null) {
					biome = LevelThemeGlobalSettings.levelThemeMainBiome;
					temperature = LevelThemeGlobalSettings.temperature;
					humidity = LevelThemeGlobalSettings.humidity;
				} else {
					
					double d9 = this.variationScratch[biomeIndex] * 1.1D + 0.5D;
					double d11 = 0.01D;
					double d13 = 1.0D - d11;
					temperature = (this.temperatureScratch[biomeIndex] * 0.15D + 0.7D) * d13 + d9 * d11;
					d11 = 0.002D;
					d13 = 1.0D - d11;
					humidity = (this.humidityScratch[biomeIndex] * 0.15D + 0.5D) * d13 + d9 * d11;
					temperature = 1.0D - (1.0D - temperature) * (1.0D - temperature);
	
					if(temperature < 0.0D) {
						temperature = 0.0D;
					}
	
					if(humidity < 0.0D) {
						humidity = 0.0D;
					}
	
					if(temperature > 1.0D) {
						temperature = 1.0D;
					}
	
					if(humidity > 1.0D) {
						humidity = 1.0D;
					}
		
					biome = BiomeGenBase.getBiomeFromLookup(temperature, humidity);
				}
				
				this.temperatureScratch[biomeIndex] = temperature;
				this.humidityScratch[biomeIndex] = humidity;
				biomeGenArray[biomeIndex] = biome;
				biomeIndex ++;
			}
		}

		return biomeGenArray;
	}
}
