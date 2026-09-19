package net.minecraft.world.level;

import java.util.Random;

public class WorldNameGen {
	private static final String[] ADJECTIVES = {
		"Ancient", "Bleak", "Bright", "Broken", "Calm", "Cursed", "Dark", "Distant",
		"Emerald", "Eternal", "Frozen", "Golden", "Grim", "Hidden", "Loft", "Lonely",
		"Lost", "Misty", "Mossy", "Old", "Quiet", "Radiant", "Rusty", "Sacred",
		"Silent", "Silver", "Stony", "Sunken", "Twisted", "Verdant", "Whispering", "Wild"
	};
	private static final String[] NOUNS = {
		"Arch", "Ashes", "Bay", "Brooks", "Caverns", "Cliffs", "Crags", "Crown",
		"Deep", "Dells", "Downs", "Falls", "Fields", "Fens", "Firth", "Fjord",
		"Foothills", "Grove", "Harbor", "Heath", "Hills", "Hollow", "Isle", "Knoll",
		"Marshes", "Meadow", "Moor", "Peaks", "Pines", "Pits", "Reach", "Reef",
		"Ridge", "River", "Sea", "Shore", "Spires", "Strand", "Thicket", "Vale",
		"Valley", "Wastes", "Waters", "Woods", "Wylds"
	};

	public static long deriveSeed(long baseSeed, int worldId) {
		if(worldId == 0) return baseSeed;
		long z = baseSeed + 0x9E3779B97F4A7C15L * (long) worldId;
		z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
		z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
		return z ^ (z >>> 31);
	}

	public static String getName(long baseSeed, int worldId) {
		Random random = new Random(deriveSeed(baseSeed, worldId));
		String name = ADJECTIVES[random.nextInt(ADJECTIVES.length)] + " " + NOUNS[random.nextInt(NOUNS.length)];
		return name + " #" + worldId;
	}
}