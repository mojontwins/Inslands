package net.minecraft.src;

public class ColorizerWater {
	private static int[] waterBuffer = new int[65536];

	public static void setWaterBiomeColorizer(int[] i0) {
		waterBuffer = i0;
	}
}
