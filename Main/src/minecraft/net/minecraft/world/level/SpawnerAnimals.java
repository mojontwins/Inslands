package net.minecraft.world.level;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EnumCreatureType;
import net.minecraft.world.entity.IMobWithLevel;
import net.minecraft.world.entity.animal.farm.EntityCow;
import net.minecraft.world.entity.animal.farm.EntityPig;
import net.minecraft.world.entity.animal.farm.EntitySheep;
import net.minecraft.world.entity.mob.boss.EntityAlphaWitch;
import net.minecraft.world.entity.mob.undead.EntityHusk;
import net.minecraft.world.entity.mob.undead.EntitySkeleton;
import net.minecraft.world.entity.mob.spider.EntitySpider;
import net.minecraft.world.entity.mob.undead.EntityZombie;
import net.minecraft.world.entity.mob.undead.EntityZombieAlex;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkCoordIntPair;
import net.minecraft.world.level.chunk.ChunkCoordinates;
import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.level.pathfinder.PathPoint;
import net.minecraft.world.level.pathfinder.Pathfinder;
import net.minecraft.world.level.theme.LevelThemeGlobalSettings;
import net.minecraft.world.level.tile.BlockBed;

public final class SpawnerAnimals {
	private static final Set<ChunkCoordIntPair> eligibleChunksForSpawning = new HashSet<>();
	private static final Class<?>[] nightSpawnClasses = new Class<?>[]{EntitySpider.class, EntityZombie.class, EntitySkeleton.class};

	private static final int SPAWN_SEARCH_RADIUS = 8;
	private static final int SPAWN_SPREAD = 6;
	private static final int MAX_SPAWN_ATTEMPTS = 3;
	private static final float MIN_SPAWN_DISTANCE_FROM_SPAWN_SQ = 576.0F;
	private static final double MIN_PLAYER_DISTANCE = 24.0D;

	private static ChunkPosition getRandomSpawningPointInChunk(World world, int chunkX, int chunkZ) {
		int x = chunkX + world.rand.nextInt(16);
		int y = world.rand.nextInt(128);
		int z = chunkZ + world.rand.nextInt(16);
		return new ChunkPosition(x, y, z);
	}

	public static final int performSpawning(World world, boolean spawnHostileMobs, boolean spawnPeacefulMobs) {
		if(!spawnHostileMobs && !spawnPeacefulMobs) {
			return 0;
		}

		eligibleChunksForSpawning.clear();

		IChunkProvider chunkProvider = world.getChunkProvider().getChunkProviderGenerate();
		int minXChunk = WorldSize.getXChunkMinForReal(chunkProvider);
		int maxXChunk = WorldSize.getXChunkMaxForReal(chunkProvider);
		int minZChunk = WorldSize.getZChunkMinForReal(chunkProvider);
		int maxZChunk = WorldSize.getZChunkMaxForReal(chunkProvider);

		// Collect eligible chunks near all players
		world.playerEntities.forEach(player -> {
			int playerChunkX = MathHelper.floor_double(((EntityPlayer)player).posX / 16.0D);
			int playerChunkZ = MathHelper.floor_double(((EntityPlayer)player).posZ / 16.0D);

			IntStream.rangeClosed(-SPAWN_SEARCH_RADIUS, SPAWN_SEARCH_RADIUS).forEach(dx -> {
				int chunkX = playerChunkX + dx;
				if(chunkX >= minXChunk && chunkX < maxXChunk) {
					IntStream.rangeClosed(-SPAWN_SEARCH_RADIUS, SPAWN_SEARCH_RADIUS).forEach(dz -> {
						int chunkZ = playerChunkZ + dz;
						if(chunkZ >= minZChunk && chunkZ < maxZChunk) {
							eligibleChunksForSpawning.add(new ChunkCoordIntPair(chunkX, chunkZ));
						}
					});
				}
			});
		});

		int totalSpawned = 0;
		ChunkCoordinates spawnPoint = world.getSpawnPoint();

		for(EnumCreatureType creatureType : EnumCreatureType.values()) {
			int maxEntities = creatureType.getMaxNumberOfCreature() * eligibleChunksForSpawning.size() / 256;
			int activeEntities = countActiveEntities(world, creatureType);
			int hordeSize = 4;

			if(creatureType == EnumCreatureType.monster) {
				hordeSize += 2;
				if(world.worldInfo.isBloodMoon()) {
					maxEntities *= 3;
					hordeSize *= 3;
				}
				if(Seasons.currentSeason == 0) maxEntities += maxEntities >> 1;      // 3/2
				else if(Seasons.currentSeason == 2) maxEntities = (maxEntities >> 2) + (maxEntities >> 1); // 3/4
			} else {
				if(Seasons.currentSeason == 2) maxEntities += maxEntities >> 1;      // 3/2
				else if(Seasons.currentSeason == 0) maxEntities = (maxEntities >> 2) + (maxEntities >> 1); // 3/4
			}

			if(!isCreatureTypeAllowed(creatureType, spawnHostileMobs, spawnPeacefulMobs)) {
				continue;
			}

			if(activeEntities > maxEntities) {
				continue;
			}

			totalSpawned += trySpawnCreatureType(world, creatureType, spawnPoint, activeEntities, maxEntities, hordeSize);
		}

		return totalSpawned;
	}

	private static int countActiveEntities(World world, EnumCreatureType creatureType) {
		return eligibleChunksForSpawning.stream()
			.mapToInt(coords -> {
				Chunk chunk = world.getChunkFromChunkCoords(coords.chunkXPos, coords.chunkZPos);
				return chunk.getCreatureTypeCounter(creatureType);
			})
			.sum();
	}

	private static boolean isCreatureTypeAllowed(EnumCreatureType creatureType, boolean spawnHostileMobs, boolean spawnPeacefulMobs) {
		if(creatureType.getPeacefulCreature() && !spawnPeacefulMobs) return false;
		if(!creatureType.getPeacefulCreature() && !spawnHostileMobs) return false;
		return true;
	}

	private static int trySpawnCreatureType(World world, EnumCreatureType creatureType, ChunkCoordinates spawnPoint, int activeEntities, int maxEntities, int hordeSize) {
		int totalSpawned = 0;
		int budget = maxEntities - activeEntities;
		if(budget <= 0) {
			return 0;
		}

		for(ChunkCoordIntPair chunkCoords : eligibleChunksForSpawning) {
			if(totalSpawned >= budget) {
				break;
			}
			Chunk spawningChunk = world.getChunkFromChunkCoords(chunkCoords.chunkXPos, chunkCoords.chunkZPos);
			BiomeGenBase biome = spawningChunk.getBiomeGenAt(8, 8);
			List<SpawnListEntry> spawnList = biome.getSpawnableList(creatureType);

			if(spawnList == null || spawnList.isEmpty()) {
				continue;
			}

			SpawnListEntry selectedEntry = selectWeightedRandom(world, spawnList, spawningChunk);
			if(selectedEntry == null) {
				continue;
			}

			ChunkPosition spawnPos = getRandomSpawningPointInChunk(world, chunkCoords.chunkXPos * 16, chunkCoords.chunkZPos * 16);
			int spawnX = spawnPos.x;
			int spawnY = spawnPos.y;
			int spawnZ = spawnPos.z;

			if(world.isBlockNormalCube(spawnX, spawnY, spawnZ)) {
				continue;
			}

			if(world.getBlockMaterial(spawnX, spawnY, spawnZ) != creatureType.getCreatureMaterial()) {
				continue;
			}

			int spawnedInChunk = 0;

			for(int attempt = 0; attempt < MAX_SPAWN_ATTEMPTS; attempt++) {
				int x = spawnX;
				int y = spawnY;
				int z = spawnZ;

				for(int i = 0; i < hordeSize; i++) {
					x += world.rand.nextInt(SPAWN_SPREAD) - world.rand.nextInt(SPAWN_SPREAD);
					y += world.rand.nextInt(1) - world.rand.nextInt(1);
					z += world.rand.nextInt(SPAWN_SPREAD) - world.rand.nextInt(SPAWN_SPREAD);

					x = x % WorldSize.width;
					y = y % 128;
					z = z % WorldSize.length;

					if(!canCreatureTypeSpawnAtLocation(creatureType, world, x, y, z)) {
						continue;
					}

					float xFloat = (float)x + 0.5F;
					float yFloat = (float)y;
					float zFloat = (float)z + 0.5F;

					if(world.getClosestPlayer(xFloat, yFloat, zFloat, MIN_PLAYER_DISTANCE) != null) {
						continue;
					}

					float dxSpawn = xFloat - (float)spawnPoint.posX;
					float dySpawn = yFloat - (float)spawnPoint.posY;
					float dzSpawn = zFloat - (float)spawnPoint.posZ;
					float distanceSq = dxSpawn * dxSpawn + dySpawn * dySpawn + dzSpawn * dzSpawn;

					if(distanceSq < MIN_SPAWN_DISTANCE_FROM_SPAWN_SQ) {
						continue;
					}

					EntityLiving entity = instantiateEntity(selectedEntry.entityClass, world);
					if(entity == null) {
						return totalSpawned;
					}

					entity.setLocationAndAngles(xFloat, yFloat, zFloat, world.rand.nextFloat() * 360.0F, 0.0F);

					if(!entity.getCanSpawnHere()) {
						continue;
					}

					++spawnedInChunk;
					world.spawnEntityInWorld(entity);
					creatureSpecificInit(entity, world, xFloat, yFloat, zFloat);
					++totalSpawned;

					if(spawnedInChunk >= entity.getMaxSpawnedInChunk() || totalSpawned >= budget) {
						break;
					}
				}
			}
		}

		return totalSpawned;
	}

	private static SpawnListEntry selectWeightedRandom(World world, List<SpawnListEntry> spawnList, Chunk spawningChunk) {
		int totalWeight = spawnList.stream().mapToInt(entry -> entry.spawnRarityRate).sum();
		if(totalWeight <= 0) return null;

		int picker = world.rand.nextInt(totalWeight);
		for(SpawnListEntry entry : spawnList) {
			picker -= entry.spawnRarityRate;
			if(picker < 0) {
				if(!entry.isUrban || spawningChunk.hasBuilding || spawningChunk.hasRoad) {
					return entry;
				}
			}
		}

		// Fallback: return first entry that matches urban criteria
		for(SpawnListEntry entry : spawnList) {
			if(!entry.isUrban || spawningChunk.hasBuilding || spawningChunk.hasRoad) {
				return entry;
			}
		}

		return null;
	}

	private static EntityLiving instantiateEntity(Class<?> entityClass, World world) {
		try {
			return (EntityLiving)entityClass.getConstructor(World.class).newInstance(world);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType creatureType, World world, int x, int y, int z) {
		if(creatureType == EnumCreatureType.caveCreature) {
			if(!world.isBlockUnderground(x, y, z)) return false;
		}

		Material creatureMaterial = creatureType.getCreatureMaterial();

		if(creatureMaterial == Material.water) {
			return world.getBlockMaterial(x, y, z).getIsLiquid()
				&& !world.isBlockNormalCube(x, y + 1, z);
		} else {
			return world.isBlockNormalCube(x, y - 1, z)
				&& !world.isBlockNormalCube(x, y, z)
				&& !world.getBlockMaterial(x, y, z).getIsLiquid()
				&& !world.isBlockNormalCube(x, y + 1, z);
		}
	}

	private static EntityLiving spawnSpecial(EntityLiving entityLiving, World world, float posX, float posY, float posZ, float rotationYaw, float rotationPitch) {
		entityLiving.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
		world.spawnEntityInWorld(entityLiving);
		return entityLiving;
	}

	public static void creatureSpecificInit(EntityLiving entity, World world, float x, float y, float z) {
		if(entity instanceof EntityPig) {
			initPig(world, entity, x, y, z);
		} else if(entity instanceof EntityCow) {
			initCow(world, entity, x, y, z);
		} else if(entity instanceof EntitySpider) {
			initSpider(world, entity, x, y, z);
		} else if(entity instanceof EntitySheep) {
			initSheep((EntitySheep) entity, world);
		} else if(entity instanceof EntityAlphaWitch) {
			((EntityAlphaWitch)entity).fillInventory();
		} else if(entity instanceof IMobWithLevel) {
			initMobWithLevel(world, (IMobWithLevel)entity, x, y, z);
		}
	}

	private static void initPig(World world, EntityLiving pig, float x, float y, float z) {
		if(world.rand.nextInt(128) == 0) {
			EntityLiving rider = spawnSpecial(new EntityHusk(world), world, x, y, z, pig.rotationYaw, 0.0F);
			rider.mountEntity(pig);
		}
	}

	private static void initCow(World world, EntityLiving cow, float x, float y, float z) {
		if(world.rand.nextInt(128) == 0) {
			EntityLiving rider = spawnSpecial(new EntityHusk(world), world, x, y, z, cow.rotationYaw, 0.0F);
			rider.mountEntity(cow);
		}
	}

	private static void initSpider(World world, EntityLiving spider, float x, float y, float z) {
		EntityLiving rider = null;
		switch(world.rand.nextInt(128)) {
			case 0: rider = new EntitySkeleton(world); break;
			case 1: rider = new EntityZombie(world); break;
			case 2: rider = new EntityZombieAlex(world); break;
			case 3: rider = new EntityHusk(world); break;
		}

		if(rider != null) {
			spawnSpecial(rider, world, x, y, z, spider.rotationYaw, 0.0F);
			rider.mountEntity(spider);
		}
	}

	private static void initSheep(EntitySheep sheep, World world) {
		if(LevelThemeGlobalSettings.colourfulFlock) {
			sheep.setFleeceColor(EntitySheep.getRandomFleeceColorForReal(world.rand));
		} else {
			sheep.setFleeceColor(EntitySheep.getRandomFleeceColor(world.rand));
		}
	}

	private static void initMobWithLevel(World world, IMobWithLevel mob, float x, float y, float z) {
		int metadata = world.getBlockMetadata((int)x, (int)y, (int)z);
		int level = metadata == 0 ? world.rand.nextInt(mob.getMaxLevel()) : metadata & 7;
		mob.setLevel(level);
	}

	public static boolean performSleepSpawning(World world, List<EntityPlayer> players) {
		Pathfinder pathfinder = new Pathfinder(world);

		for(EntityPlayer player : players) {
			if(nightSpawnClasses == null || nightSpawnClasses.length == 0) {
				continue;
			}

			if(trySleepSpawnNearPlayer(world, pathfinder, player, nightSpawnClasses)) {
				return true;
			}
		}

		return false;
	}

	private static boolean trySleepSpawnNearPlayer(World world, Pathfinder pathfinder, EntityPlayer player, Class<?>[] spawnClasses) {
		for(int attempt = 0; attempt < 20; attempt++) {
			int spawnX = MathHelper.floor_double(player.posX) + world.rand.nextInt(32) - world.rand.nextInt(32);
			int spawnZ = MathHelper.floor_double(player.posZ) + world.rand.nextInt(32) - world.rand.nextInt(32);
			int spawnY = MathHelper.floor_double(player.posY) + world.rand.nextInt(16) - world.rand.nextInt(16);
			if(spawnY < 1) spawnY = 1;
			else if(spawnY > 128) spawnY = 128;

			int classIndex = world.rand.nextInt(spawnClasses.length);

			// Find ground level
			int groundY = spawnY;
			for(; groundY > 2 && !world.isBlockNormalCube(spawnX, groundY - 1, spawnZ); --groundY) {
			}

			// Search upward for valid spawn position
			while(!canCreatureTypeSpawnAtLocation(EnumCreatureType.monster, world, spawnX, groundY, spawnZ) && groundY < spawnY + 16 && groundY < 128) {
				++groundY;
			}

			if(groundY >= spawnY + 16 || groundY >= 128) {
				continue;
			}

			EntityLiving entity = instantiateEntity(spawnClasses[classIndex], world);
			if(entity == null) {
				return false;
			}

			float xFloat = (float)spawnX + 0.5F;
			float yFloat = (float)groundY;
			float zFloat = (float)spawnZ + 0.5F;

			entity.setLocationAndAngles(xFloat, yFloat, zFloat, world.rand.nextFloat() * 360.0F, 0.0F);

			if(!entity.getCanSpawnHere()) {
				continue;
			}

			PathEntity path = pathfinder.createEntityPathTo(entity, player, 32.0F);
			if(path == null || path.pathLength <= 1) {
				continue;
			}

			PathPoint pathEnd = path.getPathPoint();
			double dxPath = Math.abs(pathEnd.xCoord - player.posX);
			double dyPath = Math.abs(pathEnd.yCoord - player.posY);
			double dzPath = Math.abs(pathEnd.zCoord - player.posZ);

			if(dxPath >= 1.5D || dyPath >= 1.5D || dzPath >= 1.5D) {
				continue;
			}

			ChunkCoordinates bedPos = BlockBed.getNearestEmptyChunkCoordinates(
				world,
				MathHelper.floor_double(player.posX),
				MathHelper.floor_double(player.posY),
				MathHelper.floor_double(player.posZ),
				1
			);

			if(bedPos == null) {
				bedPos = new ChunkCoordinates(spawnX, groundY + 1, spawnZ);
			}

			float bedXFloat = (float)bedPos.posX + 0.5F;
			float bedYFloat = (float)bedPos.posY;
			float bedZFloat = (float)bedPos.posZ + 0.5F;

			entity.setLocationAndAngles(bedXFloat, bedYFloat, bedZFloat, 0.0F, 0.0F);
			world.spawnEntityInWorld(entity);
			creatureSpecificInit(entity, world, bedXFloat, bedYFloat, bedZFloat);
			player.wakeUpPlayer(true, false, false);
			entity.playLivingSound();
			return true;
		}

		return false;
	}
}
