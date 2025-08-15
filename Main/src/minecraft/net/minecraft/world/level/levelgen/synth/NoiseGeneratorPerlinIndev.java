package net.minecraft.world.level.levelgen.synth;

import java.util.Random;

import net.minecraft.util.MathHelper;

public class NoiseGeneratorPerlinIndev {

	private int[] permutations;
	private double xCoord;
	private double yCoord;
	private double zCoord;
	
	public NoiseGeneratorPerlinIndev() {
		this(new Random());
	}
	
	public NoiseGeneratorPerlinIndev(final Random random) {
		this.permutations = new int[512];
		this.xCoord = random.nextDouble() * 256.0;
		this.yCoord = random.nextDouble() * 256.0;
		this.zCoord = random.nextDouble() * 256.0;
		for (int i = 0; i < 256; ++i) {
			this.permutations[i] = i;
		}
		for (int j = 0; j < 256; ++j) {
			final int k = random.nextInt(256 - j) + j;
			final int l = this.permutations[j];
			this.permutations[j] = this.permutations[k];
			this.permutations[k] = l;
			this.permutations[j + 256] = this.permutations[j];
		}
	}
	
	public double generateNoise(double d1, double d2, double d3) {
		double d4 = d1 + this.xCoord;
		double d5 = d2 + this.yCoord;
		double d6 = d3 + this.zCoord;
		d1 = (MathHelper.floor_double(d4) & 0xFF);
		double i = MathHelper.floor_double(d5) & 0xFF;
		d2 = (MathHelper.floor_double(d6) & 0xFF);
		d4 -= MathHelper.floor_double(d4);
		d5 -= MathHelper.floor_double(d5);
		d6 -= MathHelper.floor_double(d6);
		final double d7 = generateNoise(d4);
		final double d8 = generateNoise(d5);
		final double d9 = generateNoise(d6);
		double j = this.permutations[(int)d1] + i;
		d3 = this.permutations[(int)j] + d2;
		j = this.permutations[(int)j + 1] + d2;
		d1 = this.permutations[(int)d1 + 1] + i;
		i = this.permutations[(int)d1] + d2;
		d1 = this.permutations[(int)d1 + 1] + d2;
		return lerp(d9, lerp(d8, lerp(d7, grad(this.permutations[(int)d3], d4, d5, d6), grad(this.permutations[(int)i], d4 - 1.0, d5, d6)), lerp(d7, grad(this.permutations[(int)j], d4, d5 - 1.0, d6), grad(this.permutations[(int)d1], d4 - 1.0, d5 - 1.0, d6))), lerp(d8, lerp(d7, grad(this.permutations[(int)d3 + 1], d4, d5, d6 - 1.0), grad(this.permutations[(int)i + 1], d4 - 1.0, d5, d6 - 1.0)), lerp(d7, grad(this.permutations[(int)j + 1], d4, d5 - 1.0, d6 - 1.0), grad(this.permutations[(int)d1 + 1], d4 - 1.0, d5 - 1.0, d6 - 1.0))));
	}
	
	private static double generateNoise(final double d1) {
		return d1 * d1 * d1 * (d1 * (d1 * 6.0 - 15.0) + 10.0);
	}
	
	private static double lerp(final double d1, final double d2, final double d3) {
		return d2 + d1 * (d3 - d2);
	}
	
	private static double grad(int i, final double d1, final double d2, final double d3) {
		final double d4 = ((i &= 0xF) >= 8) ? d2 : d1;
		final double d5 = (i >= 4) ? ((i != 12 && i != 14) ? d3 : d1) : d2;
		return (((i & 0x1) != 0x0) ? (-d4) : d4) + (((i & 0x2) != 0x0) ? (-d5) : d5);
	}
	
	public double generateNoise(final double d1, final double d2) {
		return this.generateNoise(d1, d2, 0.0);
	}
	

}
