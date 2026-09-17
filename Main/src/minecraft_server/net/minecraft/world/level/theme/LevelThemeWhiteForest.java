package net.minecraft.world.level.theme;

import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.WorldType;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.levelgen.MapGenBase;
import net.minecraft.world.level.levelgen.MapGenCavesLush;
import net.minecraft.world.level.levelgen.mcfeature.Feature;
import net.minecraft.world.level.levelgen.mcfeature.FeatureProvider;
import net.minecraft.world.level.levelgen.mcfeature.FeatureSlimeBossLair;

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
		this.creepy = true;
		this.hauntCows = true;

		this.levelThemeMainBiome = BiomeGenBase.themeWhiteForest;
	}
	
	@Override
	public MapGenBase overrideCaveGenerator() {
		return new MapGenCavesLush(); 
	}
	
	@Override
	public void modifyFeatureMap(Feature[] featureMap, World world, FeatureProvider featureProvider) {
		System.out.println ("Adding the secret boss lair . . .");
		int chunkX = WorldSize.xChunks / 2;
		int chunkZ = WorldSize.zChunks / 2;
		featureMap [WorldSize.coords2hash(chunkX, chunkZ)] = new FeatureSlimeBossLair(world, chunkX, chunkZ, featureProvider); 
	}
}
