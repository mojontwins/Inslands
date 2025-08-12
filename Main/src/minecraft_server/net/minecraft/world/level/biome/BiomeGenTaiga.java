package net.minecraft.world.level.biome;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.Weather;
import net.minecraft.src.World;
import net.minecraft.world.entity.animal.EntityColdCow;
import net.minecraft.world.entity.animal.EntitySheep;
import net.minecraft.world.entity.animal.EntityWolf;
import net.minecraft.world.entity.monster.EntityCreeper;
import net.minecraft.world.entity.monster.EntityIceSkeleton;
import net.minecraft.world.entity.monster.EntitySlime;
import net.minecraft.world.entity.monster.EntitySpider;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.entity.monster.EntityZombieAlex;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.levelgen.city.BuildingTaigaHut;
import net.minecraft.world.level.levelgen.feature.WorldGenIgloos;
import net.minecraft.world.level.levelgen.feature.WorldGenLakes;
import net.minecraft.world.level.levelgen.feature.WorldGenMinable;
import net.minecraft.world.level.levelgen.feature.WorldGenerator;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenFir;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenPineTree;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenTaigaTree1;
import net.minecraft.world.level.levelgen.feature.trees.WorldGenTaigaTree2;

public class BiomeGenTaiga extends BiomeGenForest {
	public BiomeGenTaiga() {
		super();
		this.bigTreesEach10Trees = 5;
		this.treeBaseAttemptsModifier = 7;
		this.tallGrassAttempts = 128;
		
		this.weather = Weather.cold;
		
		this.spawnableCreatureList.clear();
		this.spawnableCreatureList.add(new SpawnListEntry(EntitySheep.class, 12));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityColdCow.class, 8));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 2));
		
		this.spawnableMonsterList.clear();
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySpider.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 5));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityZombieAlex.class, 5));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityIceSkeleton.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 10));
	}
	
	public boolean isPermaFrost() {
		return true;
	}
	
	public int getAlgaeAmount() {
		return 1;
	}
	
	public int getCoralAmount() {
		return 8;
	}
	
	public WorldGenerator getTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		return new WorldGenTaigaTree2();
	}
	
	public WorldGenerator getBigTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		if(rand.nextInt(100) == 0) return new WorldGenPineTree(); //new WorldGenFir(4 + rand.nextInt(7), true);
		if(rand.nextInt(10) == 0) return new WorldGenFir(3 + rand.nextInt(3), false);
		return new WorldGenTaigaTree1();
	}

	public void prePopulate(World world, Random rand, int x0, int z0) {
		int x, y, z;
		
		// Generate lakes
		for(int i = 0; i < 2; i ++) {
			x = x0 + rand.nextInt(16) + 8;
			y = rand.nextInt(128);
			z = z0 + rand.nextInt(16) + 8;
			(new WorldGenLakes(Block.waterMoving.blockID)).generate(world, rand, x, y, z);
		}
	}
	
	public void populate (World world, Random rand, int chunkX, int chunkZ) {
		super.populate(world, rand, chunkX, chunkZ);
		
		int x, y, z;
	
		// Generate igloos
		if (rand.nextInt(64) == 0) {
			x = chunkX + rand.nextInt(16) + 8;
			z = chunkZ + rand.nextInt(16) + 8;
			WorldGenIgloos worldgenIgloos = new WorldGenIgloos();
			worldgenIgloos.generate(world, rand, x, world.getHeightValue(x, z), z);
		}
		
		// Generate snow pools
		for(int i = 0; i < 20; ++i) {
			x = chunkX + rand.nextInt(16);
			y = rand.nextInt(128);
			z = chunkZ + rand.nextInt(16);
			(new WorldGenMinable(Block.blockSnow.blockID, 32)).generate(world, rand, x, y, z);
		}
	}
	
	@Override
	public boolean isHumid() {
		return true;
	}
	
	@Override
	public void generate(Random rand, int y0, Chunk chunk) {
		if(rand.nextInt(64) == 0) {
			(new BuildingTaigaHut ()).generate(y0, chunk);
		}
	}

}
