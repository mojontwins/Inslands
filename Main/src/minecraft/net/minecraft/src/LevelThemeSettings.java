package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LevelThemeSettings {
	public int id;
	public String name;
	public float lightMultiplier = 1.0F;
	public boolean dayCycle = false;
	public BiomeGenBase levelThemeMainBiome = null;
	public double temperature = 0.6D;
	public double humidity = 0.6D;
	public int permaSeason = -1;
	public int overlay = -1;
	public boolean canSnow = true;
	public boolean canRain = true;
	public boolean canThunder = true;
	public float fixedCelestialAngle = -1;
	public boolean sunriseSunsetColors = false;

	public static List<LevelThemeSettings> allThemeSettings = new ArrayList<LevelThemeSettings> ();
	
	public static LevelThemeSettings normal = new LevelThemeSettings(0).setName("Normal").setLightMultiplier(1.0F).setDayCycle(true).setLevelThemeMainBiome(BiomeGenBase.biomeDefault).setTemperature(0.6D).setHumidity(0.6D).setOverlay(-1);
	public static LevelThemeSettings hell = new LevelThemeSettings(1).setName("Hell").setLightMultiplier(0.4F).setDayCycle(true).setLevelThemeMainBiome(BiomeGenBase.themeHell).setTemperature(1.0D).setHumidity(0.1D).setOverlay(-1);
	public static LevelThemeSettings forest = new LevelThemeSettings(2).setName("Forest").setLightMultiplier(0.8F).setDayCycle(true).setLevelThemeMainBiome(BiomeGenBase.themeForest).withFixedCelestialAngle(0.24F).withSunriseSunsetColors(true).setTemperature(0.4D).setHumidity(0.9D).setOverlay(-1);
	public static LevelThemeSettings paradise = new LevelThemeSettings(3).setName("Paradise").setLightMultiplier(1.0F).setDayCycle(false).setLevelThemeMainBiome(BiomeGenBase.themeParadise).withFixedCelestialAngle(1.0F).setTemperature(0.6D).setHumidity(0.6D).setPermaSeason(Seasons.SUMMER).setOverlay(0x20F9FFA0).setCanRain(false).setCanSnow(false).setCanThunder(false);
	public static LevelThemeSettings biomes = new LevelThemeSettings(4).setName("Multi Biomes").setLightMultiplier(1.0F).setDayCycle(true).setLevelThemeMainBiome(null);
	
	public LevelThemeSettings(int id) {
		this.id = id;
		allThemeSettings.add(id, this);
	}

	public static LevelThemeSettings findThemeById(int id) {
		LevelThemeSettings themeSettings = allThemeSettings.get(id);
		if(themeSettings == null) themeSettings = normal;
		return themeSettings;
	}
	
	public static LevelThemeSettings findThemeByName(String name) {
		Iterator<LevelThemeSettings> iterator = allThemeSettings.iterator();
		while(iterator.hasNext()) {
			LevelThemeSettings levelThemeSettings = iterator.next();
			if(levelThemeSettings.name.equals(name)) return levelThemeSettings;
		}
		
		return normal;
	}
	
	public LevelThemeSettings setName(String name) {
		this.name = name;
		return this;
	}
	
	public LevelThemeSettings setLightMultiplier(float lightMultiplier) {
		this.lightMultiplier = lightMultiplier;
		return this;
	}
	
	public LevelThemeSettings setDayCycle(boolean dayCycle) {
		this.dayCycle = dayCycle;
		return this;
	}
	
	public LevelThemeSettings setLevelThemeMainBiome(BiomeGenBase levelThemeMainBiome) {
		this.levelThemeMainBiome = levelThemeMainBiome;
		return this;
	}
	
	public LevelThemeSettings setTemperature(double temperature) {
		this.temperature = temperature;
		return this;
	}
	
	public LevelThemeSettings setHumidity(double humidity) {
		this.humidity = humidity;
		return this;
	}
	
	public LevelThemeSettings setPermaSeason(int seasonId) {
		this.permaSeason = seasonId;
		return this;
	}
	
	public LevelThemeSettings setOverlay(int overlay) {
		this.overlay = overlay;
		return this;
	}
	
	public LevelThemeSettings setCanSnow(boolean canSnow) {
		this.canSnow = canSnow;
		return this;
	}
	
	public LevelThemeSettings setCanRain(boolean canRain) {
		this.canRain = canRain;
		return this;
	}
	
	public LevelThemeSettings setCanThunder(boolean canThunder) {
		this.canThunder = canThunder;
		return this;
	}
	
	public LevelThemeSettings withFixedCelestialAngle(float celestialAngle) {
		this.fixedCelestialAngle = celestialAngle;
		return this;
	}
	
	public LevelThemeSettings withSunriseSunsetColors(boolean sunriseSunsetColors) {
		this.sunriseSunsetColors = sunriseSunsetColors;
		return this;
	}
}
