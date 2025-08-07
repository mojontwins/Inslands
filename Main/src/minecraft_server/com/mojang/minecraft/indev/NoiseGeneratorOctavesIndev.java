package com.mojang.minecraft.indev;

import java.util.Random;

import net.minecraft.src.NoiseGenerator;

public class NoiseGeneratorOctavesIndev extends NoiseGenerator {

	private NoiseGeneratorPerlinIndev[] generatorCollection;
	private int octaves;
	
	public NoiseGeneratorOctavesIndev(final int octaves) {
		this(new Random(), octaves);
	}
	
	public NoiseGeneratorOctavesIndev(final Random rand, final int octaves) {
		this.octaves = octaves;
		this.generatorCollection = new NoiseGeneratorPerlinIndev[octaves];
		
		for (int octave = 0; octave < octaves; ++octave) {
			this.generatorCollection[octave] = new NoiseGeneratorPerlinIndev(rand);
		}
	}
	
	public double generateNoise(final double x, final double z) {
		double noise = 0.0;
		double divisor = 1.0;
		for (int i = 0; i < this.octaves; ++i) {
			noise += this.generatorCollection[i].generateNoise(x / divisor, z / divisor) * divisor;
			divisor *= 2.0;
		}
		return noise;
	}
	
	public double generateNoiseOctaves(final double d, double d1, final double d2) {
		double d3 = 0.0;
		double d4 = 1.0;
		for (d1 = 0.0; d1 < this.octaves; ++d1) {
			d3 += this.generatorCollection[(int)d1].generateNoise(d / d4, 0.0 / d4, d2 / d4) * d4;
			d4 *= 2.0;
		}
		return d3;
	}

}
