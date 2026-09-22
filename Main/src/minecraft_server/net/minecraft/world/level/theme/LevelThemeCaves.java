package net.minecraft.world.level.theme;

import net.minecraft.world.level.WorldType;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.dimension.WorldProvider;
import net.minecraft.world.level.dimension.WorldProviderCaves;

public class LevelThemeCaves extends LevelThemeSettings {

	public LevelThemeCaves(int id) {
		super(id);

		// General properties

		this.name = "Caves";
		this.dayCycle = false;
		this.levelThemeMainBiome = BiomeGenBase.themeCaves;
		this.levelThemeNetherBiome = BiomeGenBase.themeForestHell;
		this.preferredWorldType = WorldType.CAVES.id;
		this.forcedWorldType = true;
		this.fixedCelestialAngle = 0.24F;
		this.sunriseSunsetColors = false;
		this.colourfulFlock = true;
		this.temperature = 0.6D;
		this.humidity = 0D;
		this.dynamicSnow = false;
		this.overlay = -1;
	}
	
	@Override
	public WorldProvider createWorldProvider() {
		return new WorldProviderCaves();
	}
}
