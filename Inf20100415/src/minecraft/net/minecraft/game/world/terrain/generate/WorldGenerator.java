package net.minecraft.game.world.terrain.generate;

import java.util.Random;

import net.minecraft.game.world.World;

public abstract class WorldGenerator {
	public abstract boolean generate(World world1, Random random2, int i3, int i4, int i5);

	public void setScale(double d1, double d3, double d5) {
	}
}