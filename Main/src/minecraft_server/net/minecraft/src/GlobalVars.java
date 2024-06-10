package net.minecraft.src;

public class GlobalVars {
	public static Entity currentBoss = null;

	// Used for theme-based world validations
	public static boolean hasBronzeDungeon = false;
	public static boolean hasCorrectMinoshroomMaze = false;
	public static boolean hasUnderHillMaze = false;
	
	public static void initializeGameFlags() {
		hasBronzeDungeon = false;
		hasCorrectMinoshroomMaze = false;
		hasUnderHillMaze = false;
	}

}
