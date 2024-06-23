package net.minecraft.src;

public class LevelThemeGlobalSettings {
	public static float lightMultiplier = 1.0F;
	public static boolean dayCycle = false;
	public static BiomeGenBase levelThemeMainBiome = BiomeGenBase.themeParadise;
	public static double temperature = 0.6D;
	public static double humidity = 0.6D;
	public static int permaSeason = -1;
	public static int overlay = -1;
	public static int themeID = 0;
	public static int worldTypeID = 0;
	public static boolean canSnow = true;
	public static boolean canRain = true;
	public static boolean canThunder = true;
	
	public static void loadThemeById(int id) {
		LevelThemeSettings settings = LevelThemeSettings.findThemeById(id);
		if(settings != null) {
			lightMultiplier = settings.lightMultiplier;
			dayCycle = settings.dayCycle;
			levelThemeMainBiome = settings.levelThemeMainBiome; // TODO: If this is null, do natural ramp-based biomes
			temperature = settings.temperature; 				// So those 
			humidity = settings.humidity;						// should be somehow ignored? 
			permaSeason = settings.permaSeason;
			overlay = -1;
			canSnow = settings.canSnow;
			canRain = settings.canRain;
			canThunder = settings.canThunder;
			themeID = id;
		}
	}

	static {
		loadThemeById(0);
	}
}
