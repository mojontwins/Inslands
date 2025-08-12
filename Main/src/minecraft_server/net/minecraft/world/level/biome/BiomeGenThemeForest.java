package net.minecraft.world.level.biome;

import java.util.Random;

import com.benimatic.twilightforest.TFGenCanopyMushroom;
import com.benimatic.twilightforest.TFGenCanopyTree;
import com.benimatic.twilightforest.TFGenFoundation;
import com.benimatic.twilightforest.TFGenHedgeMaze;
import com.benimatic.twilightforest.TFGenHillMaze;
import com.benimatic.twilightforest.TFGenMonolith;
import com.benimatic.twilightforest.TFGenMyceliumBlob;
import com.benimatic.twilightforest.TFGenOutsideStalagmite;
import com.benimatic.twilightforest.TFGenStoneCircle;
import com.benimatic.twilightforest.TFGenWell;
import com.benimatic.twilightforest.TFGenWitchHut;
import com.benimatic.twilightforest.TFGenerator;

import net.minecraft.src.Block;
import net.minecraft.src.GlobalVars;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenBigMushroom;
import net.minecraft.src.WorldGenBigTree;
import net.minecraft.src.WorldGenCypress;
import net.minecraft.src.WorldGenFir;
import net.minecraft.src.WorldGenFlowers;
import net.minecraft.src.WorldGenHugeTrees;
import net.minecraft.src.WorldGenLilypad;
import net.minecraft.src.WorldGenMazeMarker;
import net.minecraft.src.WorldGenPineTree;
import net.minecraft.src.WorldGenTrees;
import net.minecraft.src.WorldGenVines;
import net.minecraft.src.WorldGenerator;
import net.minecraft.src.WorldSize;
import net.minecraft.world.entity.animal.EntityBetaOcelot;
import net.minecraft.world.entity.animal.EntityChickenBlack;
import net.minecraft.world.entity.animal.EntityTwilightBighorn;
import net.minecraft.world.entity.animal.EntityTwilightBoar;
import net.minecraft.world.entity.animal.EntityTwilightDeer;
import net.minecraft.world.entity.animal.EntityWolf;
import net.minecraft.world.entity.monster.EntityFungalCalamity;
import net.minecraft.world.entity.monster.EntityTFHedgeSpider;
import net.minecraft.world.entity.monster.EntityTFKobold;
import net.minecraft.world.entity.monster.EntityTFRedcap;
import net.minecraft.world.level.dimension.WorldProviderSky;

public class BiomeGenThemeForest extends BiomeGenForest {
	
	private WorldGenPineTree pineTreeGen = new WorldGenPineTree();
	private WorldGenTrees normalTreeGen = new WorldGenTrees();
	private WorldGenBigTree bigTreeGen = new WorldGenBigTree();
	private WorldGenBigTree bigBigTreeGen = new WorldGenBigTree();
	private TFGenCanopyTree canopyTreeGen = new TFGenCanopyTree();
	private TFGenCanopyMushroom canopyMushroomGen = new TFGenCanopyMushroom();
	private TFGenMyceliumBlob myceliumBlobGen = new TFGenMyceliumBlob(5);
	private WorldGenBigMushroom brownMushroomGen = new WorldGenBigMushroom(0);
	private WorldGenBigMushroom redMushroomGen = new WorldGenBigMushroom(1);
	private WorldGenBigMushroom greenMushroomGen = new WorldGenBigMushroom(2);
	
	private int myceliumPerChunk = 8;
	
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
		
		this.spawnableMonsterList.add(new SpawnListEntry(EntityFungalCalamity.class, 20));
				
		this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 2));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityBetaOcelot.class, 5, true));
		
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFKobold.class, 10));
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFRedcap.class, 5));
		this.spawnableCaveCreatureList.add(new SpawnListEntry(EntityTFHedgeSpider.class, 5));
		
		this.bigBigTreeGen.setScale(2.0F, 3.0F, 2.0F);
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
		if(world.getWorldChunkManager().isAltChunk(chunkX, chunkZ, 0.0D)) {
			if(rand.nextInt(8) != 0) return rand.nextInt(10) == 0 ? this.greenMushroomGen : (rand.nextInt(3) == 0 ? this.redMushroomGen : this.brownMushroomGen) ;
		}
		
		if(rand.nextInt(5) == 0) return this.canopyTreeGen;
		if(rand.nextBoolean()) {
			return this.normalTreeGen;
		} else {
			return new WorldGenFir(3+rand.nextInt(3), false);
		}
	}
	
	public WorldGenerator getBigTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		if(world.getWorldChunkManager().isAltChunk(chunkX, chunkZ, 0.0D)) {
			if(rand.nextInt(8) != 0) return this.canopyMushroomGen;
		}
		
		if(rand.nextInt(256) == 0) return this.bigBigTreeGen;
		if(rand.nextInt(64) == 0) return new WorldGenHugeTrees(16 + rand.nextInt(16));
		if(rand.nextInt(32) == 0) return this.bigTreeGen;
		
		if(rand.nextBoolean()) {
			return this.pineTreeGen;
		} else {
			return new WorldGenCypress(5+rand.nextInt(5));
		}
	}
	
	public void prePopulate(World world, Random rand, int x0, int z0) {
		super.prePopulate(world, rand, x0, z0);
		
		int i, x, y, z;
		int cx = x0 >> 4; 
		int cz = z0 >> 4;
		
		// Attempt a hedge maze
		byte border = 6;
		if(cx >= border && cz >= border && cx < WorldSize.xChunks && cz < WorldSize.zChunks && rand.nextInt(WorldSize.xChunks * WorldSize.zChunks / 32) == 0) {
			x = x0 + 8;
			z = z0 + 8;
			
			// Mazes cover from x - 7 - 48 to (x - 7 - 48 + 48) = x - 7?!
			int x1 = x - 7 - 48;
			int x2 = x1 + 47;
			int z1 = z - 7 - 48;
			int z2 = z1 + 47;
			
			// Get land surface height for corners
			int h1 = world.getLandSurfaceHeightValue(x1, z1);
			int h2 = world.getLandSurfaceHeightValue(x1, z2);
			int h3 = world.getLandSurfaceHeightValue(x2, z1);
			int h4 = world.getLandSurfaceHeightValue(x2, z2);
			
			// Get min
			y = h1;
			if(h2 < y) y = h2;
			if(h3 < y) y = h3;
			if(h4 < y) y = h4;
			
			if((new TFGenHedgeMaze(3)).generate(world, rand, x, y+1, z)) {
				GlobalVars.numHedgeMazes ++;
			}
		}
		
		if(rand.nextInt(3) == 0) {
			x = x0 + rand.nextInt(16) + 8;
			z = z0 + rand.nextInt(16) + 8;
			y = world.getLandSurfaceHeightValue(x, z) + 1;
			TFGenerator tFGenerator22 = this.randomFeature(rand);
			if(tFGenerator22.generate(world, rand, x, y, z)) {
				//System.out.println(tFGenerator22 + " success at " + x + ", " + y + ", " + z);
			}
		}
		
		if(world.getWorldChunkManager().isAltChunk(x0 >> 4, z0 >> 4, 0.0D)) {
			for(i = 0; i < this.myceliumPerChunk ; ++i) {
				x = x0 + rand.nextInt(16) + 8;
				z = z0 + rand.nextInt(16) + 8;
				y = world.getHeightValue(x, z);
				this.myceliumBlobGen.generate(world, rand, x, y, z);
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
		if(cx > 3 && cz > 3 && cx < WorldSize.xChunks - 3 && cz < WorldSize.zChunks - 3) {
			if(world.worldProvider instanceof WorldProviderSky) {
				if(rand.nextInt(WorldSize.xChunks * WorldSize.zChunks / 16) == 0) {
					x = chunkX + 7;
					y = rand.nextInt(64) + 32;
					z = chunkZ + 7;
					if ((new TFGenHillMaze(2, true, 30)).generate(world, rand, x, y, z)) {
						(new WorldGenMazeMarker(true)).generate(world, rand, x, y + 4, z);
						GlobalVars.numUnderHillMazes ++;
					};
				}
			} else {
				if(rand.nextInt(WorldSize.xChunks * WorldSize.zChunks / 32) == 0) {
					x = chunkX + rand.nextInt(16) + 8;
					y = rand.nextInt(32) + 16;
					z = chunkZ + rand.nextInt(16) + 8;
					if ((new TFGenHillMaze(3, true, 90)).generate(world, rand, x, y, z)) {
						(new WorldGenMazeMarker(false)).generate(world, rand, x, world.getLandSurfaceHeightValue(x, z), z);
						GlobalVars.numUnderHillMazes ++;
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
