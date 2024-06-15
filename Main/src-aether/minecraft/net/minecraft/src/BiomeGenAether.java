package net.minecraft.src;

import java.util.Random;

public class BiomeGenAether extends BiomeGenBase {
	public static BiomeGenAether me = null;

	public BiomeGenAether() {
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		if(mod_Aether.raritySwet != 0) {
			this.spawnableCreatureList.add(new SpawnListEntry(EntitySwet.class, mod_Aether.raritySwet));
		}

		if(mod_Aether.rarityAechorPlant != 0) {
			this.spawnableCreatureList.add(new SpawnListEntry(EntityAechorPlant.class, mod_Aether.rarityAechorPlant));
		}

		if(mod_Aether.rarityCockatrice != 0) {
			this.spawnableMonsterList.add(new SpawnListEntry(EntityCockatrice.class, mod_Aether.rarityCockatrice));
		}

		if(mod_Aether.rarityAerwhale != 0) {
			this.spawnableMonsterList.add(new SpawnListEntry(EntityAerwhale.class, mod_Aether.rarityAerwhale));
		}

		if(mod_Aether.rarityZephyr != 0) {
			this.spawnableMonsterList.add(new SpawnListEntry(EntityZephyr.class, mod_Aether.rarityZephyr));
		}

		if(mod_Aether.raritySheepuff != 0) {
			this.spawnableCreatureList.add(new SpawnListEntry(EntitySheepuff.class, mod_Aether.raritySheepuff));
		}

		if(mod_Aether.rarityPhyg != 0) {
			this.spawnableCreatureList.add(new SpawnListEntry(EntityPhyg.class, mod_Aether.rarityPhyg));
		}

		if(mod_Aether.rarityMoa != 0) {
			this.spawnableCreatureList.add(new SpawnListEntry(EntityMoa.class, mod_Aether.rarityMoa));
		}

		if(mod_Aether.rarityFlyingCow != 0) {
			this.spawnableCreatureList.add(new SpawnListEntry(EntityFlyingCow.class, mod_Aether.rarityFlyingCow));
		}

		if(mod_Aether.rarityWhirlwind != 0) {
			this.spawnableCreatureList.add(new SpawnListEntry(Whirly.class, mod_Aether.rarityWhirlwind));
		}

		if(mod_Aether.rarityAerbunny != 0) {
			this.spawnableCreatureList.add(new SpawnListEntry(EntityAerbunny.class, mod_Aether.rarityAerbunny));
		}

		me = this;
	}

	public WorldGenerator getRandomWorldGenForTrees(Random random) {
		return (WorldGenerator)(random.nextInt(100) == 0 ? new AetherGenGoldenOak() : new AetherGenSkyroot());
	}

	public int getSkyColorByTemp(float f) {
		return 12632319;
	}
}
