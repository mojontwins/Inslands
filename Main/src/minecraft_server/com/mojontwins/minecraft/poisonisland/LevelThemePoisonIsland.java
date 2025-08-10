package com.mojontwins.minecraft.poisonisland;

import net.minecraft.src.LevelThemeSettings;
import net.minecraft.src.MapGenBase;
import net.minecraft.src.World;
import net.minecraft.src.WorldSize;
import net.minecraft.src.WorldType;
import net.minecraft.world.entity.sentient.EntityPoisonWitch;
import net.minecraft.world.level.biome.BiomeGenBase;

public class LevelThemePoisonIsland extends LevelThemeSettings {

	public LevelThemePoisonIsland(int id) {
		super(id);

		// General properties

		this.canRain = false;
		this.canSnow = false;
		this.canThunder = true;
		this.colorizedPlants = false;
		this.dayCycle = true;
		this.dynamicSnow = false;
		this.lightMultiplier = 0.4F;
		this.name = "Poison";
		this.preferredWorldType = WorldType.INDEV.id;
		this.forcedWorldType = true;
		
		this.levelThemeMainBiome = BiomeGenBase.themePoison;
	}

	@Override
	public int adjustPerlinHeight(int height) {
		if(height > 70) height += 16;
		if(height > 120) height = 120;
		return height;
	}
	
	@Override
	public boolean getInitialSpawnLocation(World world) {
		// Find somewhere low but above sea level, and try VERY hard.
		int x = WorldSize.width / 2;
		int z = WorldSize.length / 2;
		int y = world.getHeightValue(x, z) + 1;
		
		int attemptsLeft = 4096;
		
		while(attemptsLeft -- > 0) {
			if(world.canBlockSeeTheSky(x, y, z) && y < 7 && y >= 64) {
				world.getWorldInfo().setSpawn(x, y, z);
				break;
			}
		
			x += world.rand.nextInt(64) - world.rand.nextInt(64);
			z += world.rand.nextInt(64) - world.rand.nextInt(64);
			x = x % WorldSize.width;
			z = z % WorldSize.length;
			y = world.getHeightValue(x, z) + 1;
		}
		
		// TRUE if still not found
		return attemptsLeft <= 0;
	}
	
	@Override
	public MapGenBase overrideCaveGenerator() {
		return new MapGenPoisonCaves();
	}
	
	@Override
	public void levelThemeSpecificInits(World world) {
		EntityPoisonWitch witch = new EntityPoisonWitch(world);
		
		int x = WorldSize.width / 2; int z = WorldSize.length / 2;
		int y = world.getHeightValue(x, z) + 1;
		
		witch.setPosition((double) x, (double) y, (double) z);
		witch.setHomeArea(x, y, z, 15);
		
		world.spawnEntityInWorld(witch);
	}
}
