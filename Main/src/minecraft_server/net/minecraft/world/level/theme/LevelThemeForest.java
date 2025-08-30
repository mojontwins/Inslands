package net.minecraft.world.level.theme;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.util.CoordXZ;
import net.minecraft.world.GlobalVars;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldSize;
import net.minecraft.world.level.WorldType;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.dimension.WorldProviderSky;
import net.minecraft.world.level.levelgen.feature.TFGenHedgeMaze;
import net.minecraft.world.level.levelgen.feature.TFGenHillMaze;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;

public class LevelThemeForest extends LevelThemeSettings {
	private Random rand;
	private boolean skyGen = false;

	public LevelThemeForest(int id) {
		super(id);

		// General properties

		this.name = "Forest";
		this.lightMultiplier = 0.8F;
		this.dayCycle = false;
		this.levelThemeMainBiome = BiomeGenBase.themeForest;
		this.levelThemeNetherBiome = BiomeGenBase.themeForestHell;
		this.preferredWorldType = WorldType.INFDEV.id;
		this.fixedCelestialAngle = 0.24F;
		this.sunriseSunsetColors = true;
		this.colourfulFlock = true;
		this.temperature = 0.4D;
		this.humidity = 0.9D;
		this.dynamicSnow = false;
		this.overlay = -1;
	}

	@Override
	public void specialPostGeneration(World world) {
		// Seed properly
		this.rand = new Random(world.getRandomSeed());
		
		// When this runs, the whole world is generated and
		// we can perform special stuff and detections
		
		this.skyGen = world.worldProvider instanceof WorldProviderSky;
				
		// Generate hedge mazes.
		this.generateHedgeMazes(world);
		
		// Generate underhill mazes.
		this.generateUnderhillMazes(world);
	}

	private void generateUnderhillMazes(World world) {
		// Underhill mazes are somewhat easier, at least on
		// non foating gens. In floating world it can be 
		// more fishy but I've modified the generator to 
		// only carve the islands and not produce a ugly box
		// around it.
		
		// The max amount of hedge mazes allowed depends on the
		// level size. 
		
		// small = 1,
		// normal = 2,
		// big = 4,
		// huge = 8.
		
		int maxDungeons = WorldSize.zChunks / 8;
		
		int dungeonSize = 3;
		int minSolid = 70;
		
		if(this.skyGen) {
			dungeonSize = 2;
			minSolid = 15;
		}
		
		WorldGenerator dungeonGen = new TFGenHillMaze(dungeonSize, true, minSolid);
			
		// We don't want them to spawn one on top of another se we
		// are keeping track of their center.
		
		List<CoordXZ> dungeonCenters = new ArrayList<CoordXZ> ();
		
		for(int i = 0; i < maxDungeons; i ++) {
			// Pick random coordinates. Try several times
			int attempts = 16;
			boolean valid = false;
			
			while(attempts-- > 0 && !valid) {
				boolean validCoord = false;
				int coordAttempts = 8;
				int x = 0, z = 0;
				
				while(coordAttempts-- > 0 && !validCoord) {
					x = 32 + this.rand.nextInt(WorldSize.width - 64);
					z = 32 + this.rand.nextInt(WorldSize.width - 64);
					
					validCoord = true;
					CoordXZ tempCoord = new CoordXZ(x, z); 
					for(CoordXZ center : dungeonCenters) {
						if(center.distSqFrom(tempCoord) < 64*64) validCoord = false;
					}
				}
				
				if(!validCoord) break;
				
				int y = this.skyGen ? 
						16 + this.rand.nextInt(96)
					:
						16 + this.rand.nextInt(32);
				valid = dungeonGen.generate(world, world.rand, x, y, z);
				if(valid) {
					GlobalVars.numHedgeMazes ++;
					dungeonCenters.add(new CoordXZ(x, z));
				}
			}
		}
	}

	private void generateHedgeMazes(World world) {
		// To generate a hedge maze we'll have to clear a big
		// area and then fix the trees.
		
		// But before we decide if the selected place is good.
		
		// The max amount of hedge mazes allowed depends on the
		// level size. 
		
		// small = 1,
		// normal = 2,
		// big = 4,
		// huge = 8.
		
		int maxDungeons = WorldSize.zChunks / 8;
		
		int dungeonSize = 16; // size in maze cells
		int minY = 58;
		
		WorldGenerator dungeonGen = new TFGenHedgeMaze(dungeonSize, minY);
		
		if(this.skyGen) {
			dungeonSize = 10;
			minY = 8;
		}
		
		// We don't want them to spawn one on top of another se we
		// are keeping track of their center.
		
		List<CoordXZ> dungeonCenters = new ArrayList<CoordXZ> ();
		
		for(int i = 0; i < maxDungeons; i ++) {
			// Pick random coordinates. Try several times
			int attempts = 16;
			boolean valid = false;
			
			while(attempts-- > 0 && !valid) {
				boolean validCoord = false;
				int coordAttempts = 8;
				int x = 0, z = 0;
				
				while(coordAttempts-- > 0 && !validCoord) {
					x = 32 + this.rand.nextInt(WorldSize.width - 64);
					z = 32 + this.rand.nextInt(WorldSize.width - 64);
					
					validCoord = true;
					CoordXZ tempCoord = new CoordXZ(x, z); 
					for(CoordXZ center : dungeonCenters) {
						if(center.distSqFrom(tempCoord) < 64*64) validCoord = false;
					}
				}
				
				if(!validCoord) break;
				
				valid = dungeonGen.generate(world, world.rand, x, minY, z);
				if(valid) {
					GlobalVars.numHedgeMazes ++;
					dungeonCenters.add(new CoordXZ(x, z));
				}
			}
		}
	}
	
}
