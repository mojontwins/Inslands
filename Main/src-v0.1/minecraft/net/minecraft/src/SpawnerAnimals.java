package net.minecraft.src;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class SpawnerAnimals {
	private static Set<ChunkCoordIntPair> eligibleChunksForSpawning = new HashSet<ChunkCoordIntPair>();
	protected static final Class<?>[] nightSpawnEntities = new Class[]{EntitySpider.class, EntityZombie.class, EntitySkeleton.class};

	protected static ChunkPosition getRandomSpawningPointInChunk(World world0, int i1, int i2) {
		int i3 = i1 + world0.rand.nextInt(16);
		int i4 = world0.rand.nextInt(128);
		int i5 = i2 + world0.rand.nextInt(16);
		return new ChunkPosition(i3, i4, i5);
	}

	public static final int performSpawning(World world0, boolean flag1, boolean flag2) {
		if(!flag1 && !flag2) {
			return 0;
		} else {
			eligibleChunksForSpawning.clear();

			int totalSpawned;
			
			for(int i = 0; i < world0.playerEntities.size(); ++i) {
				EntityPlayer entityPlayer4 = (EntityPlayer)world0.playerEntities.get(i);
				int x0 = MathHelper.floor_double(entityPlayer4.posX / 16.0D);
				int z0 = MathHelper.floor_double(entityPlayer4.posZ / 16.0D);
				byte radius = 8;

				int xx, zz;
				for(int x = -radius; x <= radius; ++x) {
					for(int z = -radius; z <= radius; ++z) {
						xx = x + x0; zz = z + z0;
						if(xx >= 0 && xx < WorldSize.xChunks && zz >= 0 && zz < WorldSize.zChunks) {
							eligibleChunksForSpawning.add(new ChunkCoordIntPair(xx, zz));
						}
					}
				}
			}

			totalSpawned = 0;
			ChunkCoordinates chunkCoordinates35 = world0.getSpawnPoint();
			EnumCreatureType[] enumCreatureType36 = EnumCreatureType.values();
			int maxCreatureTypes = enumCreatureType36.length;

			label133:
			for(int creatureTypeIndex = 0; creatureTypeIndex < maxCreatureTypes; ++creatureTypeIndex) {
				EnumCreatureType enumCreatureType38 = enumCreatureType36[creatureTypeIndex];

				// Change the values of these parameters when suited in your mod!
				int maxEntitiesOfThisType = enumCreatureType38.getMaxNumberOfCreature() * eligibleChunksForSpawning.size() / 256;
				int hordeSize = 4;

				if(
					(!enumCreatureType38.getPeacefulCreature() || flag2) && 
					(enumCreatureType38.getPeacefulCreature() || flag1) && 
					world0.countEntities(enumCreatureType38.getCreatureClass()) <= maxEntitiesOfThisType
				) {
					Iterator<ChunkCoordIntPair> iterator39 = eligibleChunksForSpawning.iterator();

					label130:
					while(true) {
						
						SpawnListEntry spawnListEntry15;						
						int x0;
						int y0;
						int z0;
						
						do {
							do {
								ChunkCoordIntPair chunkCoordIntPair10;
								List<SpawnListEntry> list12;
								do {
									do {
										if(!iterator39.hasNext()) {
											continue label133;
										}

										chunkCoordIntPair10 = (ChunkCoordIntPair)iterator39.next();
										BiomeGenBase biomeGenBase11 = world0.getWorldChunkManager().getBiomeGenAtChunkCoord(chunkCoordIntPair10);
										list12 = biomeGenBase11.getSpawnableList(enumCreatureType38);
									} while(list12 == null);
								} while(list12.isEmpty());

								int i13 = 0;

								for(Iterator<SpawnListEntry> iterator14 = list12.iterator(); iterator14.hasNext(); i13 += spawnListEntry15.spawnRarityRate) {
									spawnListEntry15 = (SpawnListEntry)iterator14.next();
								}

								int i40 = world0.rand.nextInt(i13);
								spawnListEntry15 = (SpawnListEntry)list12.get(0);
								Iterator<SpawnListEntry> iterator16 = list12.iterator();

								while(iterator16.hasNext()) {
									SpawnListEntry spawnListEntry17 = (SpawnListEntry)iterator16.next();
									i40 -= spawnListEntry17.spawnRarityRate;
									if(i40 < 0) {
										spawnListEntry15 = spawnListEntry17;
										break;
									}
								}

								ChunkPosition chunkPosition41 = getRandomSpawningPointInChunk(world0, chunkCoordIntPair10.chunkXPos * 16, chunkCoordIntPair10.chunkZPos * 16);
								x0 = chunkPosition41.x;
								y0 = chunkPosition41.y;
								z0 = chunkPosition41.z;
							} while(world0.isBlockNormalCube(x0, y0, z0));
						} while(world0.getBlockMaterial(x0, y0, z0) != enumCreatureType38.getCreatureMaterial());

						int spawnedCount = 0;

						int spawningAttempts = 3;

						for(int spawningAttempt = 0; spawningAttempt < spawningAttempts; ++spawningAttempt) {
							int x1 = x0;
							int y1 = y0;
							int z1 = z0;
							byte spawnRadius = 6;

							for(int i26 = 0; i26 < hordeSize; ++i26) {
								x1 += world0.rand.nextInt(spawnRadius) - world0.rand.nextInt(spawnRadius);
								y1 += world0.rand.nextInt(1) - world0.rand.nextInt(1);
								z1 += world0.rand.nextInt(spawnRadius) - world0.rand.nextInt(spawnRadius);

								if(canCreatureTypeSpawnAtLocation(enumCreatureType38, world0, x1, y1, z1)) {
									float xF = (float)x1 + 0.5F;
									float yF = (float)y1;
									float zF = (float)z1 + 0.5F;
									if(world0.getClosestPlayer((double)xF, (double)yF, (double)zF, 24.0D) == null) {
										float xF1 = xF - (float)chunkCoordinates35.posX;
										float yF1 = yF - (float)chunkCoordinates35.posY;
										float zF1 = zF - (float)chunkCoordinates35.posZ;
										float distanceSq = xF1 * xF1 + yF1 * yF1 + zF1 * zF1;
										if(distanceSq >= 576.0F) {
											EntityLiving entityLiving43;
											try {
												entityLiving43 = (EntityLiving)spawnListEntry15.entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{world0});
											} catch (Exception exception34) {
												exception34.printStackTrace();
												return totalSpawned;
											}

											entityLiving43.setLocationAndAngles((double)xF, (double)yF, (double)zF, world0.rand.nextFloat() * 360.0F, 0.0F);
											if(entityLiving43.getCanSpawnHere()) {
												++spawnedCount;
												world0.entityJoinedWorld(entityLiving43);
												creatureSpecificInit(entityLiving43, world0, xF, yF, zF);
												if(spawnedCount >= entityLiving43.getMaxSpawnedInChunk()) {
													continue label130;
												}
											}

											totalSpawned += spawnedCount;
										}
									}
								}
							}
						}
					}
				}
			}

			return totalSpawned;
		}
	}

	private static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType enumCreatureType0, World world1, int i2, int i3, int i4) {
		return enumCreatureType0.getCreatureMaterial() == Material.water ? world1.getBlockMaterial(i2, i3, i4).getIsLiquid() && !world1.isBlockNormalCube(i2, i3 + 1, i4) : world1.isBlockNormalCube(i2, i3 - 1, i4) && !world1.isBlockNormalCube(i2, i3, i4) && !world1.getBlockMaterial(i2, i3, i4).getIsLiquid() && !world1.isBlockNormalCube(i2, i3 + 1, i4);
	}

	private static void creatureSpecificInit(EntityLiving entityLiving0, World world1, float f2, float f3, float f4) {
		if(entityLiving0 instanceof EntitySpider && world1.rand.nextInt(100) == 0) {
			EntitySkeleton entitySkeleton5 = new EntitySkeleton(world1);
			entitySkeleton5.setLocationAndAngles((double)f2, (double)f3, (double)f4, entityLiving0.rotationYaw, 0.0F);
			world1.entityJoinedWorld(entitySkeleton5);
			entitySkeleton5.mountEntity(entityLiving0);
		} else if(entityLiving0 instanceof EntitySheep) {
			((EntitySheep)entityLiving0).setFleeceColor(EntitySheep.getRandomFleeceColor(world1.rand));
		}

	}

	public static boolean performSleepSpawning(World world0, List<EntityPlayer> list1) {
		boolean z2 = false;
		Pathfinder pathfinder3 = new Pathfinder(world0);
		Iterator<EntityPlayer> iterator4 = list1.iterator();

		while(true) {
			EntityPlayer entityPlayer5;
			Class<?>[] class6;
			do {
				do {
					if(!iterator4.hasNext()) {
						return z2;
					}

					entityPlayer5 = (EntityPlayer)iterator4.next();
					class6 = nightSpawnEntities;
				} while(class6 == null);
			} while(class6.length == 0);

			boolean z7 = false;

			for(int i8 = 0; i8 < 20 && !z7; ++i8) {
				int i9 = MathHelper.floor_double(entityPlayer5.posX) + world0.rand.nextInt(32) - world0.rand.nextInt(32);
				int i10 = MathHelper.floor_double(entityPlayer5.posZ) + world0.rand.nextInt(32) - world0.rand.nextInt(32);
				int i11 = MathHelper.floor_double(entityPlayer5.posY) + world0.rand.nextInt(16) - world0.rand.nextInt(16);
				if(i11 < 1) {
					i11 = 1;
				} else if(i11 > 128) {
					i11 = 128;
				}

				int i12 = world0.rand.nextInt(class6.length);

				int i13;
				for(i13 = i11; i13 > 2 && !world0.isBlockNormalCube(i9, i13 - 1, i10); --i13) {
				}

				while(!canCreatureTypeSpawnAtLocation(EnumCreatureType.monster, world0, i9, i13, i10) && i13 < i11 + 16 && i13 < 128) {
					++i13;
				}

				if(i13 < i11 + 16 && i13 < 128) {
					float f14 = (float)i9 + 0.5F;
					float f15 = (float)i13;
					float f16 = (float)i10 + 0.5F;

					EntityLiving entityLiving17;
					try {
						entityLiving17 = (EntityLiving)class6[i12].getConstructor(new Class[]{World.class}).newInstance(new Object[]{world0});
					} catch (Exception exception21) {
						exception21.printStackTrace();
						return z2;
					}

					entityLiving17.setLocationAndAngles((double)f14, (double)f15, (double)f16, world0.rand.nextFloat() * 360.0F, 0.0F);
					if(entityLiving17.getCanSpawnHere()) {
						PathEntity pathEntity18 = pathfinder3.createEntityPathTo(entityLiving17, entityPlayer5, 32.0F);
						if(pathEntity18 != null && pathEntity18.pathLength > 1) {
							PathPoint pathPoint19 = pathEntity18.getPathPoint();
							if(Math.abs((double)pathPoint19.xCoord - entityPlayer5.posX) < 1.5D && Math.abs((double)pathPoint19.zCoord - entityPlayer5.posZ) < 1.5D && Math.abs((double)pathPoint19.yCoord - entityPlayer5.posY) < 1.5D) {
								ChunkCoordinates chunkCoordinates20 = BlockBed.getNearestEmptyChunkCoordinates(world0, MathHelper.floor_double(entityPlayer5.posX), MathHelper.floor_double(entityPlayer5.posY), MathHelper.floor_double(entityPlayer5.posZ), 1);
								if(chunkCoordinates20 == null) {
									chunkCoordinates20 = new ChunkCoordinates(i9, i13 + 1, i10);
								}

								entityLiving17.setLocationAndAngles((double)((float)chunkCoordinates20.posX + 0.5F), (double)chunkCoordinates20.posY, (double)((float)chunkCoordinates20.posZ + 0.5F), 0.0F, 0.0F);
								world0.entityJoinedWorld(entityLiving17);
								creatureSpecificInit(entityLiving17, world0, (float)chunkCoordinates20.posX + 0.5F, (float)chunkCoordinates20.posY, (float)chunkCoordinates20.posZ + 0.5F);
								entityPlayer5.wakeUpPlayer(true, false, false);
								entityLiving17.playLivingSound();
								z2 = true;
								z7 = true;
							}
						}
					}
				}
			}
		}
	}
}
