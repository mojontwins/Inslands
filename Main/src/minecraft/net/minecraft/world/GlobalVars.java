package net.minecraft.world;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.mcfeature.FeatureAABB;

public class GlobalVars {
	public static Entity currentBoss = null;

	// Used for theme-based world validations
	public static boolean hasBronzeDungeon = false;
	public static boolean hasCorrectMinoshroomMaze = false;
	public static int numUnderHillMazes = 0;
	public static int numHedgeMazes = 0;
	
	public static FeatureAABB minoshroomMazeBB = null;
	
	// Used for then noise generators in themes with biomes
	public static int noiseOffsetX = 0;
	public static int noiseOffsetZ = 0;
	
	public static void initializeGameFlags() {
		hasBronzeDungeon = false;
		hasCorrectMinoshroomMaze = false;
		numUnderHillMazes = 0;
		numHedgeMazes = 0;
		minoshroomMazeBB = null;
	}

}
