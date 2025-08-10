package net.minecraft.world.level.biome;

import net.minecraft.src.SpawnListEntry;
import net.minecraft.world.entity.monster.EntityGhast;
import net.minecraft.world.entity.monster.EntityPigZombie;

public class BiomeGenHell extends BiomeGenBase {
	public BiomeGenHell() {
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableMonsterList.add(new SpawnListEntry(EntityGhast.class, 10));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityPigZombie.class, 10));
	}
}
