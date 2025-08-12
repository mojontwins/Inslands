package net.minecraft.world.level.biome;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.Weather;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenIceSpike;
import net.minecraft.src.WorldGenIgloos;
import net.minecraft.src.WorldGenMinable;
import net.minecraft.src.WorldGenShrub;
import net.minecraft.src.WorldGenerator;
import net.minecraft.world.entity.animal.EntityColdCow;
import net.minecraft.world.entity.monster.EntityCreeper;
import net.minecraft.world.entity.monster.EntityIceSkeleton;
import net.minecraft.world.entity.monster.EntitySlime;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.levelgen.city.BuildingTaigaHut;

public class BiomeGenTundra extends BiomeGenPlains {
	public BiomeGenTundra() {
		super();
		this.pumpkinChance = 0;
		
		this.weather = Weather.cold;
		
		this.spawnableCreatureList.clear();
		this.spawnableCreatureList.add(new SpawnListEntry(EntityColdCow.class, 8));
		
		this.spawnableMonsterList.clear();
		this.spawnableMonsterList.add(new SpawnListEntry(EntityIceSkeleton.class, 20));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 10));
		
	}
	
	public boolean isPermaFrost() {
		return true;
	}
	
	public WorldGenerator getTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		return new WorldGenShrub();
	}
	
	public void populate (World world, Random rand, int chunkX, int chunkZ) {
		int x, y, z;
		
		// Generate IceSpike
		if (rand.nextInt(8) == 0) {
			x = chunkX + rand.nextInt(16) + 8;
			z = chunkZ + rand.nextInt(16) + 8;
			WorldGenIceSpike worldgenIceSpike = new WorldGenIceSpike();
			worldgenIceSpike.generate(world, rand, x, world.getHeightValue(x, z) + 1, z);
		}
	
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
	public void generate(Random rand, int y0, Chunk chunk) {
		if(rand.nextInt(128) == 0) {
			(new BuildingTaigaHut ()).generate(y0, chunk);
		}
	}
}
