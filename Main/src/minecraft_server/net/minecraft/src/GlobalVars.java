package net.minecraft.src;

import com.mojontwins.minecraft.feature.FeatureAABB;

public class GlobalVars {
	public static Entity currentBoss = null;

	// Used for theme-based world validations
	public static boolean hasBronzeDungeon = false;
	public static boolean hasCorrectMinoshroomMaze = false;
	public static int numUnderHillMazes = 0;
	public static int numHedgeMazes = 0;
	
	public static FeatureAABB minoshroomMazeBB = null;
	
	public static void initializeGameFlags() {
		hasBronzeDungeon = false;
		hasCorrectMinoshroomMaze = false;
		numUnderHillMazes = 0;
		numHedgeMazes = 0;
		minoshroomMazeBB = null;
	}

}
