package net.minecraft.src;

public class LevelThemeGlobalSettings {
	public static float lightMultiplier = 1.0F;
	public static boolean dayCycle = false;
	public static BiomeGenBase levelThemeMainBiome = BiomeGenBase.themeParadise;
	public static double temperature = 0.6D;
	public static double humidity = 0.6D;
	public static boolean permaSpring = true;
	public static int overlay = -1;
	public static int themeID = 0;
	public static int worldTypeID = 0;
	
	public static void loadThemeById(int id) {
		LevelThemeSettings settings = LevelThemeSettings.findThemeById(id);
		if(settings != null) {
			lightMultiplier = settings.lightMultiplier;
			dayCycle = settings.dayCycle;
			levelThemeMainBiome = settings.levelThemeMainBiome;
			temperature = settings.temperature;
			humidity = settings.humidity;
			permaSpring = settings.permaSpring;
			overlay = -1;
			themeID = id;
		}
	}

	static {
		loadThemeById(0);
	}
}
