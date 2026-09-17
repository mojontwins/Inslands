package net.minecraft.world.level;

import java.util.Arrays;

import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.chunk.ChunkCoordIntPair;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;

public class WorldChunkManagerHell extends WorldChunkManager {
	// Single-biome fallback values for the hell-style worlds
	private BiomeGenBase biomeHell;
	private double temperatureHell;
	private double humidityHell;

	public WorldChunkManagerHell(BiomeGenBase biome, double temperature, double humidity) {
		this.biomeHell = biome;
		this.temperatureHell = temperature;
		this.humidityHell = humidity;
	}

	public BiomeGenBase getBiomeGenAtChunkCoord(ChunkCoordIntPair chunkCoordIntPair1) {
		return getHellBiome();
	}

	public BiomeGenBase getBiomeGenAt(int i1, int i2) {
		return getHellBiome();
	}

	public BiomeGenBase[] getBiomesForGeneration(int i1, int i2, int i3, int i4) {
		this.generatedBiomes = this.loadBlockGeneratorData(this.generatedBiomes, i1, i2, i3, i4);
		return this.generatedBiomes;
	}

	// Todo : do something for multi-biome theme
	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] biomeGenArray, int xPos, int zPos, int width, int length) {
		if(biomeGenArray == null || biomeGenArray.length < width * length) {
			biomeGenArray = new BiomeGenBase[width * length];
		}

		if(this.temperatureScratch == null || this.temperatureScratch.length < width * length) {
			this.temperatureScratch = new double[width * length];
			this.humidityScratch = new double[width * length];
		}

		Arrays.fill(biomeGenArray, 0, width * length, getHellBiome());
		Arrays.fill(this.humidityScratch, 0, width * length, this.humidityHell);
		Arrays.fill(this.temperatureScratch, 0, width * length, this.temperatureHell);
		return biomeGenArray;
	}
	
	public BiomeGenBase getHellBiome() {
		BiomeGenBase biomeHell = LevelThemeGlobalSettings.levelThemeNetherBiome;
		if (biomeHell == null) biomeHell = this.biomeHell;
		return biomeHell;
	}
}