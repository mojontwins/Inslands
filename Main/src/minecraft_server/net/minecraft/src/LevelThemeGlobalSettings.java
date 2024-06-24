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
	public static float fixedCelestialAngle = -1;
	public static boolean sunriseSunsetColors = false;
	public static boolean colourfulFlock = false;
	public static boolean colorizedPlants = false;
	
	public static void loadThemeById(int id) {
		LevelThemeSettings settings = LevelThemeSettings.findThemeById(id);
		if(settings != null) {
			lightMultiplier = settings.lightMultiplier;
			dayCycle = settings.dayCycle;
			levelThemeMainBiome = settings.levelThemeMainBiome;
			temperature = settings.temperature;
			humidity = settings.humidity;
			permaSeason = settings.permaSeason;
			overlay = -1;
			canSnow = settings.canSnow;
			canRain = settings.canRain;
			canThunder = settings.canThunder;
			themeID = id;
			fixedCelestialAngle = settings.fixedCelestialAngle;
			sunriseSunsetColors = settings.sunriseSunsetColors;
			colourfulFlock = settings.colourfulFlock;
			colorizedPlants = settings.colorizedPlants;
		}
	}

	static {
		loadThemeById(0);
	}
}
