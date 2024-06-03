package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LevelThemeSettings {
	public int id;
	public String name;
	public float lightMultiplier = 1.0F;
	public boolean dayCycle = false;
	public BiomeGenBase levelThemeMainBiome = BiomeGenBase.themeParadise;
	public double temperature = 0.6D;
	public double humidity = 0.6D;
	public boolean permaSpring = true;
	public int overlay = -1;

	public static List<LevelThemeSettings> allThemeSettings = new ArrayList<LevelThemeSettings> ();
	
	public static LevelThemeSettings normal = new LevelThemeSettings(0).setName("Normal").setLightMultiplier(1.0F).setDayCycle(true).setLevelThemeMainBiome(BiomeGenBase.biomeDefault).setTemperature(0.6D).setHumidity(0.6D).setPermaSpring(false).setOverlay(-1);
	public static LevelThemeSettings hell = new LevelThemeSettings(1).setName("Hell").setLightMultiplier(0.4F).setDayCycle(true).setLevelThemeMainBiome(BiomeGenBase.themeHell).setTemperature(1.0D).setHumidity(0.1D).setPermaSpring(false).setOverlay(-1);
	public static LevelThemeSettings forest = new LevelThemeSettings(2).setName("Forest").setLightMultiplier(0.8F).setDayCycle(true).setLevelThemeMainBiome(BiomeGenBase.themeForest).setTemperature(0.4D).setHumidity(0.9D).setPermaSpring(false).setOverlay(-1);
	public static LevelThemeSettings paradise = new LevelThemeSettings(3).setName("Paradise").setLightMultiplier(1.0F).setDayCycle(false).setLevelThemeMainBiome(BiomeGenBase.themeParadise).setTemperature(0.6D).setHumidity(0.6D).setPermaSpring(true).setOverlay(0x20F9FFA0);
	
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
	
	public LevelThemeSettings setPermaSpring(boolean permaSpring) {
		this.permaSpring = permaSpring;
		return this;
	}
	
	public LevelThemeSettings setOverlay(int overlay) {
		this.overlay = overlay;
		return this;
	}
}
