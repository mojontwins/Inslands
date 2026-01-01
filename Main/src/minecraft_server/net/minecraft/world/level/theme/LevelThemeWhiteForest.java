package net.minecraft.world.level.theme;

import net.minecraft.world.level.WorldType;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.levelgen.MapGenBase;
import net.minecraft.world.level.levelgen.MapGenCavesLush;

public class LevelThemeWhiteForest extends LevelThemeSettings {

	public LevelThemeWhiteForest(int id) {
		super(id);
		
		// General properties
		
		this.canRain = true;
		this.canSnow = true;
		this.canThunder = true;
		this.colorizedPlants = true;
		this.dayCycle = true;
		this.dynamicSnow = false;
		this.lightMultiplier = 0.8F;
		this.name="WhiteJungle";
		this.preferredWorldType = WorldType.INFDEV.id;
		this.forcedWorldType = false;

		this.levelThemeMainBiome = BiomeGenBase.themeWhiteForest;
	}
	
	@Override
	public MapGenBase overrideCaveGenerator() {
		return new MapGenCavesLush(); 
	}
}
