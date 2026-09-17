package net.minecraft.world;

import java.util.Random;

public class WorldNameGen {
	private static final Random rand = new Random();
	static String getName(long initialSeed, int worldNumber) {
		String randomName = "World Name";
		rand.setSeed(initialSeed + 24117 * worldNumber);
		
		// TODO : Actually generate a random name
		
		return randomName;
	}
}
