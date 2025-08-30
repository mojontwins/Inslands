package net.minecraft.world.level.theme;

import java.util.List;
import java.util.Random;

import net.minecraft.util.CoordXZ;
import net.minecraft.util.CoordXZUtils;
import net.minecraft.world.GlobalVars;
import net.minecraft.world.level.Seasons;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.WorldType;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.levelgen.feature.AetherGenDungeonBronze;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.tile.Block;

public class LevelThemeParadise extends LevelThemeSettings {
	Random rand;

	WorldGenerator dungeon = new AetherGenDungeonBronze(
			Block.lockedDungeonStone.blockID, 
			Block.lockedLightDungeonStone.blockID, 
			Block.dungeonStone.blockID, 
			Block.lightDungeonStone.blockID, 
			Block.cobblestoneMossy.blockID, 0, 
			Block.cobblestone.blockID, 0, 
			16, true);
	
	public LevelThemeParadise(int id) {
		super(id);
		
		// General properties

		this.name = "Paradise";
		this.lightMultiplier = 1.0F;
		this.dayCycle = false;
		this.levelThemeMainBiome = BiomeGenBase.themeParadise;
		this.preferredWorldType = WorldType.SKY.id;
		this.fixedCelestialAngle = 1.0F;
		this.temperature = 0.6D;
		this.humidity = 0.6D;
		this.permaSeason = Seasons.SUMMER; 
		this.overlay = 0x20F9FFA0;
		this.canRain = false;
		this.canSnow = false;
		this.canThunder = false;
	}

	@Override
	public void specialPostGeneration(World world) {
		// Seed properly
		this.rand = new Random(world.getRandomSeed());
				
		// When this runs, the whole world is generated and
		// we can perform special stuff and detections
		
		// In paradise, we use this to generate a number of bronze 
		// dungeons.
		
		int maxDungeons = WorldSize.zChunks / 8;
		
		// Start from the center and advance outwards.
		System.out.println ("Paradise post generation");
		
		List<CoordXZ> coords = CoordXZUtils.getCoordsOrderedFromCenter(WorldSize.width, WorldSize.length, WorldSize.xChunks / 4);
		CoordXZ latestXZ = null;
		
		for(CoordXZ coord : coords) {
			boolean generated = false;
			
			if(GlobalVars.numBronzeDungeons > maxDungeons) break;
			if(latestXZ != null && latestXZ.distSqFrom(coord) < 4096) continue;
			
			int y = world.getLandSurfaceHeightValue(coord.x, coord.z);

			// y cointains the topmost block Y coordinate.
			// So if we find  y in range we scan downwards.
			if(y > 16 && y < 128) {
				// To generate, aether bronze dungeons need a 16x12x16
				// cube starting on x, y, z. So we start finding the spot
				// 13 blocks below the surface
				y -= 13;
				
				while(y -- > 1 && !generated) {
					if(dungeon.generate(world, world.rand, coord.x, y, coord.z)) {	
						// Mark as generated
						generated = true;
						latestXZ = coord;
					}
				}
			}
		}
	}
}
