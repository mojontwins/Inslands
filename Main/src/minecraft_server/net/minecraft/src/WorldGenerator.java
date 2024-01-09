package net.minecraft.src;

import java.util.Random;

public abstract class WorldGenerator {
	public abstract boolean generate(World world, Random rand, int x, int y, int z);

	public void setScale(double scaleX, double scaleY, double scaleZ) {
	}
}
