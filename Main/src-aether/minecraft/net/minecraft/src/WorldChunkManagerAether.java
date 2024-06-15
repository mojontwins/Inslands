package net.minecraft.src;

import java.util.Arrays;

public class WorldChunkManagerAether extends WorldChunkManager {
	private BiomeGenBase field_4201_e = BiomeGenAether.me;
	private double field_4200_f;

	public WorldChunkManagerAether(double d) {
		this.field_4200_f = d;
	}

	public BiomeGenBase getBiomeGenAtChunkCoord(ChunkCoordIntPair chunkcoordintpair) {
		return this.field_4201_e;
	}

	public BiomeGenBase getBiomeGenAt(int i, int j) {
		return this.field_4201_e;
	}

	public double getTemperature(int i, int j) {
		return this.field_4200_f;
	}

	public double[] getTemperatures(double[] ad, int i, int j, int k, int l) {
		if(ad == null || ad.length < k * l) {
			ad = new double[k * l];
		}

		Arrays.fill(ad, 0, k * l, this.field_4200_f);
		return ad;
	}

	public BiomeGenBase[] func_4069_a(int i, int j, int k, int l) {
		this.field_4195_d = this.loadBlockGeneratorData(this.field_4195_d, i, j, k, l);
		return this.field_4195_d;
	}

	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] abiomegenbase, int i, int j, int k, int l) {
		if(abiomegenbase == null || abiomegenbase.length < k * l) {
			abiomegenbase = new BiomeGenBase[k * l];
		}

		if(this.temperature == null || this.temperature.length < k * l) {
			this.temperature = new double[k * l];
			this.humidity = new double[k * l];
		}

		Arrays.fill(abiomegenbase, 0, k * l, this.field_4201_e);
		return abiomegenbase;
	}
}
