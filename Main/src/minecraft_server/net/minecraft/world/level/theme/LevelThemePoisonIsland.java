package net.minecraft.world.level.theme;

import net.minecraft.world.entity.animal.EntityCatBlack;
import net.minecraft.world.entity.sentient.EntityPoisonWitch;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.WorldType;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.levelgen.MapGenBase;
import net.minecraft.world.level.levelgen.MapGenPoisonCaves;
import net.minecraft.world.level.levelgen.feature.WorldGenWitchHut;

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
		if(height > 120) height = 118 + (height - 118) / 4;
		return height;
	}
	
	@Override
	public boolean getInitialSpawnLocation(World world) {
		// Find somewhere low but above sea level, and try VERY hard.
		int x = WorldSize.width / 2 + world.rand.nextInt(64) - world.rand.nextInt(64);
		int z = WorldSize.length / 2 + world.rand.nextInt(64) - world.rand.nextInt(64);
		int y;
		
		int attemptsLeft = 4096;
		
		while(attemptsLeft -- > 0) {
			x += world.rand.nextInt(64) - world.rand.nextInt(64);
			z += world.rand.nextInt(64) - world.rand.nextInt(64);
			x = x % WorldSize.width;
			z = z % WorldSize.length;
			y = world.getHeightValue(x, z) + 1;
			
			if(world.canBlockSeeTheSky(x, y, z) && y <= 70 && y >= 64) {
				world.getWorldInfo().setSpawn(x, y, z);
				break;
			}
			
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
		EntityCatBlack blackCat = new EntityCatBlack(world);
		blackCat.setWitchCat(true);
		
		int x = WorldSize.width / 2; int z = WorldSize.length / 2;
		int y = world.getHeightValue(x, z) + 1;
		
		WorldGenWitchHut hut = new WorldGenWitchHut(false);
		hut.generate(world, world.rand, x - 3, y, z - 3);

		witch.setLocationAndAngles((double)x, (double)y, (double)z, world.rand.nextFloat() * 360.0F, 0.0F);
		witch.setHomeArea(x, y, z, 20);
		
		blackCat.setLocationAndAngles((double)x, (double)y, (double)(z + 1), world.rand.nextFloat() * 360.0F, 0.0F);
		blackCat.setHomeArea(x, y, z, 8);
		
		world.spawnEntityInWorld(witch);
	}
}
