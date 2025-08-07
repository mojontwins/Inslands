package com.mojang.minecraft.infdev;

import java.util.Random;

import net.minecraft.src.NoiseGenerator;

public class NoiseGeneratorOctavesInfdev extends NoiseGenerator {
	private NoiseGeneratorPerlinInfdev[] generatorCollection;
	private int octaves;

	public NoiseGeneratorOctavesInfdev(Random random1, int i2) {
		this.octaves = i2;
		this.generatorCollection = new NoiseGeneratorPerlinInfdev[i2];

		for(int i3 = 0; i3 < i2; ++i3) {
			this.generatorCollection[i3] = new NoiseGeneratorPerlinInfdev(random1);
		}

	}

	public double noiseGenerator(final double x, final double z) {
		double noise = 0.0;
		double divisor = 1.0;
		for (int i = 0; i < this.octaves; ++i) {
			noise += this.generatorCollection[i].generateNoise(x / divisor, z / divisor) * divisor;
			divisor *= 2.0;
		}
		return noise;
	}

	public final double generateNoiseOctaves(double d1, double d3, double d5) {
		double d7 = 0.0D;
		double d9 = 1.0D;

		for(int i11 = 0; i11 < this.octaves; ++i11) {
			d7 += this.generatorCollection[i11].generateNoise(d1 / d9, d3 / d9, d5 / d9) * d9;
			d9 *= 2.0D;
		}

		return d7;
	}
}
