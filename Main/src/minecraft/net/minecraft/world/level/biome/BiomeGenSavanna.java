package net.minecraft.world.level.biome;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.Weather;
import net.minecraft.src.World;
import net.minecraft.src.WorldGenBaobab;
import net.minecraft.src.WorldGenLakes;
import net.minecraft.src.WorldGenLilypad;
import net.minecraft.src.WorldGenPalmTree1;
import net.minecraft.src.WorldGenPalmTree3;
import net.minecraft.src.WorldGenSeaweed;
import net.minecraft.src.WorldGenVines;
import net.minecraft.src.WorldGenerator;
import net.minecraft.world.entity.animal.EntityBetaOcelot;

public class BiomeGenSavanna extends BiomeGenBeta {

	public BiomeGenSavanna() {
		super();
		this.bigTreesEach10Trees = 5;
		this.treeBaseAttemptsModifier = -2;
		this.tallGrassAttempts = 32;
		this.pumpkinChance = 0;
		this.deadBushAttempts = 4;

		this.weather = Weather.hot;

		this.spawnableCreatureList.add(new SpawnListEntry(EntityBetaOcelot.class, 10, true));
	}

	public WorldGenerator getTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		if (rand.nextInt(3) == 0) {
			return new WorldGenPalmTree1();
		} else
			return new WorldGenBaobab(2 + rand.nextInt(3));
	}

	public WorldGenerator getBigTreeGen(World world, Random rand, int chunkX, int chunkZ) {
		return new WorldGenBaobab(6 + rand.nextInt(7));
	}

	public byte getTopBlock(Random rand) {
		return (byte) (rand.nextInt(8) != 0 ? Block.grass.blockID : Block.dirtPath.blockID);
	}

	public void prePopulate(World world, Random rand, int x0, int z0) {
		int x, y, z;

		// Generate lakes
		if (rand.nextInt(4) == 0) {
			x = x0 + rand.nextInt(16) + 8;
			y = rand.nextInt(128);
			z = z0 + rand.nextInt(16) + 8;
			(new WorldGenLakes(Block.waterMoving.blockID)).generate(world, rand, x, y, z);
		}

		// Generate lava lakes
		if (rand.nextInt(8) == 0) {
			x = x0 + rand.nextInt(16) + 8;
			y = rand.nextInt(rand.nextInt(120) + 8);
			z = z0 + rand.nextInt(16) + 8;
			if (y < 64 || rand.nextInt(10) == 0) {
				(new WorldGenLakes(Block.lavaMoving.blockID)).generate(world, rand, x, y, z);
			}
		}
	}

	public void populate(World world, Random rand, int chunkX, int chunkZ) {
		int x, y, z;

		// Generate vines

		for (int i = 0; i < 50; i++) {
			x = chunkX + rand.nextInt(16) + 8;
			y = 64;
			z = chunkZ + rand.nextInt(16) + 8;

			(new WorldGenVines()).generate(world, rand, x, y, z);
		}

		// Generate Lilypads

		for (int i = 0; i < 8; i++) {
			x = chunkX + rand.nextInt(16) + 8;
			z = chunkZ + rand.nextInt(16) + 8;

			for (y = rand.nextInt(128); y > 0 && world.getBlockId(x, y - 1, z) == 0; y--) {
			}

			(new WorldGenLilypad()).generate(world, rand, x, y, z);
		}

		// Generate algae
		int algae = rand.nextInt(32);

		for (int i = 0; i < algae; i++) {
			x = chunkX + rand.nextInt(16) + 8;
			z = chunkZ + rand.nextInt(16) + 8;
			y = world.getHeightValueUnderWater(x, z);
			(new WorldGenSeaweed()).generate(world, rand, x, y, z);
		}

		// Generate coral
		for (int i = 0; i < 32; i++) {
			x = chunkX + rand.nextInt(16) + 8;
			z = chunkZ + rand.nextInt(16) + 8;
			y = world.getHeightValueUnderWater(x, z) + 1;
			if (Block.coral.canBlockStay(world, x, y, z)) {
				world.setBlockAndMetadataWithNotify(x, y, z, Block.coral.blockID, 8 | rand.nextInt(3));
			}
		}

		// Palms
		if (rand.nextInt(3) == 0) {
			x = chunkX + rand.nextInt(16) + 8;
			z = chunkZ + rand.nextInt(16) + 8;
			y = world.getLandSurfaceHeightValue(x, z);

			WorldGenerator treeGen = rand.nextBoolean() ? new WorldGenPalmTree1() : new WorldGenPalmTree3();
			treeGen.generate(world, rand, x, y, z);
		}
	}

}
