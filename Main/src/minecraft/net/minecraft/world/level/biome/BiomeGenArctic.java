package net.minecraft.world.level.biome;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.Weather;
import net.minecraft.src.World;
import net.minecraft.world.entity.animal.EntityChicken;
import net.minecraft.world.entity.animal.EntityColdCow;
import net.minecraft.world.entity.animal.EntitySheep;
import net.minecraft.world.entity.animal.EntityTwilightBoar;
import net.minecraft.world.entity.monster.EntityCreeper;
import net.minecraft.world.entity.monster.EntityIceSkeleton;
import net.minecraft.world.entity.monster.EntitySlime;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.level.levelgen.feature.WorldGenLakes;
import net.minecraft.world.level.levelgen.feature.WorldGenMinable;

public class BiomeGenArctic extends BiomeGenBeta {

	public BiomeGenArctic() {
		super ();
		
		this.weather = Weather.cold;
		this.biomeColor = 0xFEFEFE;
		this.treeBaseAttemptsModifier = -2;
		this.topBlock = (byte)Block.blockSnow.blockID;
		this.fillerBlock = (byte)Block.dirt.blockID;
		
		this.spawnableCreatureList.clear();
		this.spawnableCreatureList.add(new SpawnListEntry(EntityColdCow.class, 8));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityChicken.class, 8));
		this.spawnableCreatureList.add(new SpawnListEntry(EntityTwilightBoar.class, 8));
		this.spawnableCreatureList.add(new SpawnListEntry(EntitySheep.class, 8));
		
		this.spawnableMonsterList.clear();
		this.spawnableMonsterList.add(new SpawnListEntry(EntityIceSkeleton.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityZombie.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityCreeper.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 10));
		
		this.minHeight = 0.7F;
		this.maxHeight = 1.5F;
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
	
	public void populate(World world, Random rand, int chunkX, int chunkZ) {
		int x, y, z, i;
		
		for(i = 0; i < 5; ++i) {
			x = chunkX + rand.nextInt(16);
			y = rand.nextInt(64) + 48;
			z = chunkZ + rand.nextInt(16);
			(new WorldGenMinable(Block.dirt.blockID, 32)).generate(world, rand, x, y, z);
		}
		
		for(i = 0; i < 2; ++i) {
			x = chunkX + rand.nextInt(16);
			y = rand.nextInt(64) + 48;
			z = chunkZ + rand.nextInt(16);
			(new WorldGenMinable(Block.regolith.blockID, 32)).generate(world, rand, x, y, z);
		}
		
		for(i = 0; i < 2; ++i) {
			x = chunkX + rand.nextInt(16);
			y = 12 + rand.nextInt(48);
			z = chunkZ + rand.nextInt(16);
			(new WorldGenMinable(Block.oreRuby.blockID, 7)).generate(world, rand, x, y, z);
		}
		
		for(i = 0; i < 2; ++i) {
			x = chunkX + rand.nextInt(16);
			y = 12 + rand.nextInt(48);
			z = chunkZ + rand.nextInt(16);
			(new WorldGenMinable(Block.oreEmerald.blockID, 7)).generate(world, rand, x, y, z);
		}

	}
}
