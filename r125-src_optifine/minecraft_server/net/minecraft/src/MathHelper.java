package net.minecraft.src;

import java.util.Random;

public class MathHelper {
	private static float[] SIN_TABLE = new float[65536];

	public static final float sin(float f0) {
		return SIN_TABLE[(int)(f0 * 10430.378F) & 65535];
	}

	public static final float cos(float f0) {
		return SIN_TABLE[(int)(f0 * 10430.378F + 16384.0F) & 65535];
	}

	public static final float sqrt_float(float f0) {
		return (float)Math.sqrt((double)f0);
	}

	public static final float sqrt_double(double d0) {
		return (float)Math.sqrt(d0);
	}

	public static int floor_float(float f0) {
		int i1 = (int)f0;
		return f0 < (float)i1 ? i1 - 1 : i1;
	}

	public static int floor_double(double d0) {
		int i2 = (int)d0;
		return d0 < (double)i2 ? i2 - 1 : i2;
	}

	public static long floor_double_long(double d0) {
		long j2 = (long)d0;
		return d0 < (double)j2 ? j2 - 1L : j2;
	}

	public static float abs(float f0) {
		return f0 >= 0.0F ? f0 : -f0;
	}

	public static int abs(int i0) {
		return i0 >= 0 ? i0 : -i0;
	}

	public static int clamp_int(int i0, int i1, int i2) {
		return i0 < i1 ? i1 : (i0 > i2 ? i2 : i0);
	}

	public static double abs_max(double d0, double d2) {
		if(d0 < 0.0D) {
			d0 = -d0;
		}

		if(d2 < 0.0D) {
			d2 = -d2;
		}

		return d0 > d2 ? d0 : d2;
	}

	public static int getRandomIntegerInRange(Random random0, int i1, int i2) {
		return i1 >= i2 ? i1 : random0.nextInt(i2 - i1 + 1) + i1;
	}

	static {
		for(int i0 = 0; i0 < 65536; ++i0) {
			SIN_TABLE[i0] = (float)Math.sin((double)i0 * Math.PI * 2.0D / 65536.0D);
		}

	}
}
