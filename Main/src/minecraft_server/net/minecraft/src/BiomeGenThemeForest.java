package net.minecraft.src;

import java.util.Random;

import com.benimatic.twilightforest.EntityTFHedgeSpider;
import com.benimatic.twilightforest.EntityTFKobold;
import com.benimatic.twilightforest.EntityTFRedcap;
import com.benimatic.twilightforest.EntityTwilightBighorn;
import com.benimatic.twilightforest.EntityTwilightBoar;
import com.benimatic.twilightforest.EntityTwilightDeer;
import com.benimatic.twilightforest.TFGenCanopyTree;
import com.benimatic.twilightforest.TFGenFoundation;
import com.benimatic.twilightforest.TFGenHillMaze;
import com.benimatic.twilightforest.TFGenMonolith;
import com.benimatic.twilightforest.TFGenOutsideStalagmite;
import com.benimatic.twilightforest.TFGenStoneCircle;
import com.benimatic.twilightforest.TFGenWell;
import com.benimatic.twilightforest.TFGenWitchHut;
import com.benimatic.twilightforest.TFGenerator;
import com.mojang.minecraft.ocelot.EntityBetaOcelot;

public class BiomeGenThemeForest extends BiomeGenForest {
	public BiomeGenThemeForest() {
		super();
		this.overrideSkyColor = 0x757D87;
		this.overrideFogColor = 0x4D5A5B;
		this.overrideCloudColor = 0x4D5A5B;
		
		this.bigTreesEach10Trees = 5;
		this.treeBaseAttemptsModifier = 7;
		this.tallGrassAttempts = 128;
		this.redFlowersAttempts = 16;
		this.yellowFlowersAttempts = 24;
		
		// Replace with TF variants
		this.spawnableCreatureList.clear();
		this.spawnableCreatureList.add(new SpawnListEntry(EntityTwilightBighorn.class, 12));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityTwilightBoar.class, 10));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityChickenBlack.class, 10));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityTwilightDeer.class, 8));
				
		this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 2));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityBetaOcelot.class, 5, true));
		
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFKobold.class, 10));
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFRedcap.class, 5));
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFHedgeSpider.class, 5));
	}
	
	public int getAlgaeAmount() {
		return 96;
	}
	
	public int getCoralAmount() {
		return 64;
	}
	
	public int getNetherVinesPerChunk() {
		return 64;
	}
	
	public WorldGenerator getTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		if(rand.nextInt(5) == 0) return new TFGenCanopyTree();
		if(rand.nextBoolean()) {
			return new WorldGenTrees();
		} else {
			return new WorldGenFir(3+rand.nextInt(3), false);
		}
	}
	
	public WorldGenerator getBigTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		if(rand.nextInt(64) == 0) return new WorldGenHugeTrees(16 + rand.nextInt(16));
		if(rand.nextBoolean()) {
			return new WorldGenPineTree(); // WorldGenFir(5+rand.nextInt(5), true);
		} else {
			return new WorldGenCypress(5+rand.nextInt(5));
		}
	}
	
	public void prePopulate(World world, Random rand, int x0, int z0) {
		super.prePopulate(world, rand, x0, z0);
		
		int x, y, z;
		
		if(rand.nextInt(3) == 0) {
			x = x0 + rand.nextInt(16) + 8;
			z = z0 + rand.nextInt(16) + 8;
			y = world.getLandSurfaceHeightValue(x, z) + 1;
			TFGenerator tFGenerator22 = this.randomFeature(rand);
			//System.out.println(tFGenerator22 + " attempt at " + x + ", " + y + ", " + z);
			if(tFGenerator22.generate(world, rand, x, y, z)) {
				System.out.println(tFGenerator22 + " success at " + x + ", " + y + ", " + z);
			}
		}
	}
	
	public void populate(World world, Random rand, int chunkX, int chunkZ) {	
		super.populate(world, rand, chunkX, chunkZ);
		int x, y, z;
		
		// Generate vines
		for(int i = 0; i < 120; i++) {
			x = chunkX + rand.nextInt(16) + 8;
			y = 32;
			z = chunkZ + rand.nextInt(16) + 8;
			
			(new WorldGenVines()).generate(world, rand, x, y, z);
		}
		
		// Generate Lilypads
		for(int i = 0; i < 8; i ++) {
			x = chunkX + rand.nextInt(16) + 8;
			z = chunkZ + rand.nextInt(16) + 8;
			
			for(y = rand.nextInt(128); y > 0 && world.getBlockId(x, y - 1, z) == 0; y --) {}
			
			(new WorldGenLilypad()).generate(world, rand, x, y, z);
		}
		
		// Paeonias
		for(int i = 0; i < 12; ++i) {
			x = chunkX + rand.nextInt(16) + 8;
			y = rand.nextInt(128);
			z = chunkZ + rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.paeonia.blockID)).generate(world, rand, x, y, z);
		}
		
		// Blue Flowers
		for(int i = 0; i < 10; ++i) {
			x = chunkX + rand.nextInt(16) + 8;
			y = rand.nextInt(128);
			z = chunkZ + rand.nextInt(16) + 8;
			(new WorldGenFlowers(Block.blueFlower.blockID)).generate(world, rand, x, y, z);
		}
		
		// Maze
		int cx = chunkX >> 4; 
		int cz = chunkZ >> 4;
		if(cx > 3 && cz > 3 && cz < WorldSize.xChunks - 3 && cz < WorldSize.zChunks - 3) {
			if(world.worldProvider instanceof WorldProviderSky) {
				x = chunkX + 7;
				y = rand.nextInt(64) + 32;
				z = chunkZ + 7;
				if ((new TFGenHillMaze(2, true, 80)).generate(world, rand, x, y, z)) {
					(new WorldGenMazeMarker(true)).generate(world, rand, x, y + 4, z);
				};
			} else if(WorldSize.xChunks > 16) {
				if(rand.nextInt(WorldSize.xChunks * WorldSize.zChunks / 4) == 0) {
					x = chunkX + rand.nextInt(16) + 8;
					y = rand.nextInt(32) + 16;
					z = chunkZ + rand.nextInt(16) + 8;
					if ((new TFGenHillMaze(3, true, 90)).generate(world, rand, x, y, z)) {
						(new WorldGenMazeMarker(false)).generate(world, rand, x, world.getLandSurfaceHeightValue(x, z), z);
					}
				}
			}
		}
	}
	
	@Override
	public boolean isHumid() {
		return true;
	}
	
	public TFGenerator randomFeature(Random rand) {
		int rf = rand.nextInt(6);
		switch(rf) {
		case 0:
			return new TFGenStoneCircle();
		case 1:
			return new TFGenWell();
		case 2:
			return new TFGenWitchHut();
		case 3:
			return new TFGenOutsideStalagmite();
		case 4:
			return new TFGenFoundation();
		case 5:
			return new TFGenMonolith();
		default:
			return new TFGenStoneCircle();
		}
	}
}
