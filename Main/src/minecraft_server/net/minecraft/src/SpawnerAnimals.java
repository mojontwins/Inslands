package net.minecraft.src;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mojang.minecraft.witch.EntityAlphaWitch;

public final class SpawnerAnimals {
	private static Set<ChunkCoordIntPair> eligibleChunksForSpawning = new HashSet<ChunkCoordIntPair>();
	protected static final Class<?>[] nightSpawnEntities = new Class[]{EntitySpider.class, EntityZombie.class, EntitySkeleton.class};

	protected static ChunkPosition getRandomSpawningPointInChunk(World world, int i1, int i2) {
		int i3 = i1 + world.rand.nextInt(16);
		int i4 = world.rand.nextInt(128);
		int i5 = i2 + world.rand.nextInt(16);
		return new ChunkPosition(i3, i4, i5);
	}

	public static final int performSpawning(World world, boolean flag1, boolean flag2) {
		if(!flag1 && !flag2) {
			return 0;
		} else {
			eligibleChunksForSpawning.clear();

			IChunkProvider chunkProvider = world.getChunkProvider().getChunkProviderGenerate();
			
			int minXChunk = WorldSize.getXChunkMinForReal(chunkProvider);
			int maxXChunk = WorldSize.getXChunkMaxForReal(chunkProvider);
			int minZChunk = WorldSize.getZChunkMinForReal(chunkProvider);
			int maxZChunk = WorldSize.getZChunkMaxForReal(chunkProvider);
			
			int totalSpawned;
			
			for(int i = 0; i < world.playerEntities.size(); ++i) {
				EntityPlayer entityPlayer4 = (EntityPlayer)world.playerEntities.get(i);
				int x0 = MathHelper.floor_double(entityPlayer4.posX / 16.0D);
				int z0 = MathHelper.floor_double(entityPlayer4.posZ / 16.0D);
				byte radius = 8;
	
				int xx, zz;
				for(int x = -radius; x <= radius; ++x) {
					xx = x0 + x;
					if(xx >= minXChunk && xx < maxXChunk) {
						for(int z = -radius; z <= radius; ++z) {
							zz = z0 + z;
							if (zz >= minZChunk && zz < maxZChunk) {
								eligibleChunksForSpawning.add(new ChunkCoordIntPair(xx, zz));
							}
						}
					}
				}
			}

			totalSpawned = 0;
			ChunkCoordinates spawnPoint = world.getSpawnPoint();
			EnumCreatureType[] availableCreatureTypes = EnumCreatureType.values();
			int maxCreatureTypes = availableCreatureTypes.length;

			label133:
			for(int creatureTypeIndex = 0; creatureTypeIndex < maxCreatureTypes; ++creatureTypeIndex) {
				EnumCreatureType creatureType = availableCreatureTypes[creatureTypeIndex];

				// Change the values of these parameters when suited in your mod!
				int maxEntitiesOfThisType = creatureType.getMaxNumberOfCreature() * eligibleChunksForSpawning.size() / 256;
				
				//int activeEntitiesOfThisType = world.countEntities(creatureType.getCreatureClass());
				// Let's try and count only entities of this time in active chunks
				int activeEntitiesOfThisType = 0;
				Iterator<ChunkCoordIntPair> chunksIt = eligibleChunksForSpawning.iterator();
				while (chunksIt.hasNext()) {
					ChunkCoordIntPair chunkCoords = chunksIt.next();
					Chunk spawningChunk = world.getChunkFromChunkCoords(chunkCoords.chunkXPos, chunkCoords.chunkZPos);
					activeEntitiesOfThisType += spawningChunk.getCreatureTypeCounter(creatureType);					
				}
				
				int hordeSize = 4;

				if(creatureType == EnumCreatureType.monster) {
					hordeSize += 2;
					if(world.worldInfo.isBloodMoon()) { 
						maxEntitiesOfThisType *=3;
						hordeSize *= 3;
					}
					if(Seasons.currentSeason == 0) maxEntitiesOfThisType = maxEntitiesOfThisType + (maxEntitiesOfThisType >> 1);				// 3/2
					else if(Seasons.currentSeason == 2) maxEntitiesOfThisType = (maxEntitiesOfThisType >> 2) + (maxEntitiesOfThisType >> 1); 	// 3/4
				} else {
					if(Seasons.currentSeason == 2) maxEntitiesOfThisType = maxEntitiesOfThisType + (maxEntitiesOfThisType >> 1);				// 3/2
					else if(Seasons.currentSeason == 0) maxEntitiesOfThisType = (maxEntitiesOfThisType >> 2) + (maxEntitiesOfThisType >> 1); 	// 3/4
				}

				// System.out.println ("Chunks: " + eligibleChunksForSpawning.size() + ", TYPE: " + creatureType + " #" + activeEntitiesOfThisType + " of " + maxEntitiesOfThisType);
								
				if(
					(!creatureType.getPeacefulCreature() || flag2) && 
					(creatureType.getPeacefulCreature() || flag1) && 
					activeEntitiesOfThisType <= maxEntitiesOfThisType
				) {
					Iterator<ChunkCoordIntPair> chunksForSpawningIt = eligibleChunksForSpawning.iterator();

					label130:
					while(true) {
						
						SpawnListEntry mobToSpawn;						
						int x0;
						int y0;
						int z0;
						Chunk spawningChunk;
						BiomeGenBase biomeGen = null;
						
						do {
							do {
								ChunkCoordIntPair chunkCoords;
								List<SpawnListEntry> possibleMobsToSpawn;
								do {
									do {
										if(!chunksForSpawningIt.hasNext()) {
											continue label133;
										}

										chunkCoords = (ChunkCoordIntPair)chunksForSpawningIt.next();
										spawningChunk = world.getChunkFromChunkCoords(chunkCoords.chunkXPos, chunkCoords.chunkZPos);
										
										biomeGen = spawningChunk.getBiomeGenAt(8, 8);
										
										possibleMobsToSpawn = biomeGen.getSpawnableList(creatureType);
									} while(possibleMobsToSpawn == null);
								} while(possibleMobsToSpawn.isEmpty());

								int i13 = 0;

								for(Iterator<SpawnListEntry> mobsToSpawnIt = possibleMobsToSpawn.iterator(); mobsToSpawnIt.hasNext(); i13 += mobToSpawn.spawnRarityRate) {
									mobToSpawn = (SpawnListEntry)mobsToSpawnIt.next();
								}

								int picker = world.rand.nextInt(i13);
								mobToSpawn = (SpawnListEntry)possibleMobsToSpawn.get(0);
								Iterator<SpawnListEntry> mobsToSpawnIt = possibleMobsToSpawn.iterator();

								while(mobsToSpawnIt.hasNext()) {
									SpawnListEntry spawnListEntry = (SpawnListEntry)mobsToSpawnIt.next();

									picker -= spawnListEntry.spawnRarityRate;
									if(picker < 0) {
										// Urban mobs only spawn in cities
										if(!spawnListEntry.isUrban || spawningChunk.hasBuilding || spawningChunk.hasRoad) {
											mobToSpawn = spawnListEntry;
											break;
										}
									}
								}

								ChunkPosition chunkPosition41 = getRandomSpawningPointInChunk(world, chunkCoords.chunkXPos * 16, chunkCoords.chunkZPos * 16);
								x0 = chunkPosition41.x;
								y0 = chunkPosition41.y;
								z0 = chunkPosition41.z;
							} while(world.isBlockNormalCube(x0, y0, z0));
						} while(world.getBlockMaterial(x0, y0, z0) != creatureType.getCreatureMaterial());

						int spawnedCount = 0;

						int spawningAttempts = 3;
						
						//System.out.println("Biome " + biomeGen + " Mob to spawn " + mobToSpawn.entityClass);

						for(int spawningAttempt = 0; spawningAttempt < spawningAttempts; ++spawningAttempt) {
							int x1 = x0;
							int y1 = y0;
							int z1 = z0;
							byte spawnRadius = 6;

							for(int i26 = 0; i26 < hordeSize; ++i26) {
								x1 += world.rand.nextInt(spawnRadius) - world.rand.nextInt(spawnRadius);
								y1 += world.rand.nextInt(1) - world.rand.nextInt(1);
								z1 += world.rand.nextInt(spawnRadius) - world.rand.nextInt(spawnRadius);

								x1 = x1 % WorldSize.width;
								y1 = y1 % 128;
								z1 = z1 % WorldSize.length;
								
								//System.out.println ("Attempting @ " + x1 + " " + y1 + " " + z1);
								if(canCreatureTypeSpawnAtLocation(creatureType, world, x1, y1, z1)) {
									float xF = (float)x1 + 0.5F;
									float yF = (float)y1;
									float zF = (float)z1 + 0.5F;
									
									if(world.getClosestPlayer((double)xF, (double)yF, (double)zF, 24.0D) == null) {
										float xF1 = xF - (float)spawnPoint.posX;
										float yF1 = yF - (float)spawnPoint.posY;
										float zF1 = zF - (float)spawnPoint.posZ;
										float distanceSq = xF1 * xF1 + yF1 * yF1 + zF1 * zF1;
										if(distanceSq >= 576.0F) {
											EntityLiving entityLiving;
											try {
												entityLiving = (EntityLiving)mobToSpawn.entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{world});
											} catch (Exception exception34) {
												exception34.printStackTrace();
												return totalSpawned;
											}

											/*
											if (world.getBlockMaterial(x1, y1, z1).getIsLiquid() != entityLiving.getCanSpawnOnWater()) {
												continue;
											}
											*/

											entityLiving.setLocationAndAngles((double)xF, (double)yF, (double)zF, world.rand.nextFloat() * 360.0F, 0.0F);
											
											if(entityLiving.getCanSpawnHere()) {
												++spawnedCount;
												world.spawnEntityInWorld(entityLiving);
												creatureSpecificInit(entityLiving, world, xF, yF, zF);
												
												// Cut soon
												if(spawnedCount >= entityLiving.getMaxSpawnedInChunk()) {
													continue label130;
												}
											} //else System.out.println ("Failed 'canSpawnHere'");

											totalSpawned += spawnedCount;
										} //else System.out.println ("Failed 'tooCloseToSpawn'");
									} //else System.out.println ("Failed 'playerTooCloase'");
								} //else System.out.println ("Failed 'cancreatureSpawnAtlocation'");;
							}
						}
					}
				}
			}

			return totalSpawned;
		}
	}

	private static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType enumCreatureType, World world, int x, int y, int z) {
		
		if(enumCreatureType == EnumCreatureType.caveCreature) {
			if (world.canBlockSeeTheSky(x, y, z)) return false; 			// Cave creatures cannot spawn in the open!
		} 
		
		Material creatureMaterial = enumCreatureType.getCreatureMaterial();
		boolean res = false;
		
		if(creatureMaterial == Material.water) {
			res =   world.getBlockMaterial(x, y, z).getIsLiquid() &&  		// Cube is water
					!world.isBlockNormalCube(x, y + 1, z); 					// Cube above not solid
		} else {
			res =  	world.isBlockNormalCube(x, y - 1, z) && 				// Cube below is solid
					!world.isBlockNormalCube(x, y, z) && 					// This cube is not solid
					!world.getBlockMaterial(x, y, z).getIsLiquid() &&  		// nor water
					!world.isBlockNormalCube(x, y + 1, z); 					// Cube above is not solid
		}
		
		return res;
	}
	
	private static EntityLiving spawnSpecial(EntityLiving entityLiving, World world, float posX, float posY, float posZ, float rotationYaw, float rotationPitch) {
		entityLiving.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
		world.spawnEntityInWorld(entityLiving);
		return entityLiving;
	}
	
	/*
	private static void setupTrader(EntityTrader entityTrader, World world, boolean specialTrader) {
		entityTrader.fillTradingRecipeList(world, specialTrader);
	}
	*/

	protected static void creatureSpecificInit(EntityLiving entityLiving, World world, float xF, float yF, float zF) {
		//BiomeGenBase biome = world.getBiomeGenAt((int)xF, (int)zF);
		
		if(entityLiving instanceof EntityPig) {
			if(world.rand.nextInt(128) == 0) {
				EntityLiving entityRider = spawnSpecial(new EntityHusk(world), world, xF, yF, zF, entityLiving.rotationYaw, 0.0F);
				entityRider.mountEntity(entityLiving);
			} else if(world.rand.nextInt(32) == 0) {
				/*
				EntityLiving entityRider = spawnSpecial(new EntityPigman(world), world, xF, yF, zF, entityLiving.rotationYaw, 0.0F);
				setupTrader((EntityTrader)entityRider, world, false);
				entityRider.mountEntity(entityLiving);
				*/
			}
		} else if(entityLiving instanceof EntityCow) {
			if(world.rand.nextInt(128) == 0) {
				EntityLiving entityRider = spawnSpecial(new EntityHusk(world), world, xF, yF, zF, entityLiving.rotationYaw, 0.0F);
				entityRider.mountEntity(entityLiving);
			} else if(world.rand.nextInt(32) == 0) {
				/*
				EntityLiving entityRider = spawnSpecial(new EntityCowman(world), world, xF, yF, zF, entityLiving.rotationYaw, 0.0F);
				setupTrader((EntityTrader)entityRider, world, false);
				entityRider.mountEntity(entityLiving);
				*/
			}
		} else if(entityLiving instanceof EntitySpider) {
			EntityLiving entityRider = null;
			switch(world.rand.nextInt(128)) {
				case 0: entityRider = new EntitySkeleton(world); break;
				case 1: entityRider = new EntityZombie(world); break;
				case 2: entityRider = new EntityZombieAlex(world); break;
				case 3: entityRider = new EntityHusk(world); break;
			}
			
			if(entityRider != null) {
				spawnSpecial(entityRider, world, xF, yF, zF, entityLiving.rotationYaw, 0.0F);
				entityRider.mountEntity(entityLiving);
			}
		} else if(entityLiving instanceof EntitySheep) {
			if(LevelThemeGlobalSettings.colourfulFlock) {
				((EntitySheep)entityLiving).setFleeceColor(EntitySheep.getRandomFleeceColorForReal(world.rand));
			} else {
				((EntitySheep)entityLiving).setFleeceColor(EntitySheep.getRandomFleeceColor(world.rand));
			}
		} else if(entityLiving instanceof EntityAlphaWitch) {
			((EntityAlphaWitch)entityLiving).fillInventory();
		} else if(entityLiving instanceof IMobWithLevel) {
			int metadata = world.getBlockMetadata((int)xF, (int)yF, (int)zF);
			IMobWithLevel mobWithLevel = (IMobWithLevel)entityLiving;
			int level = metadata == 0 ? world.rand.nextInt(mobWithLevel.getMaxLevel()) : metadata & 7;
			mobWithLevel.setLevel(level);
		}

	}

	public static boolean performSleepSpawning(World world, List<EntityPlayer> list1) {
		boolean z2 = false;
		Pathfinder pathfinder3 = new Pathfinder(world);
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
				int i9 = MathHelper.floor_double(entityPlayer5.posX) + world.rand.nextInt(32) - world.rand.nextInt(32);
				int i10 = MathHelper.floor_double(entityPlayer5.posZ) + world.rand.nextInt(32) - world.rand.nextInt(32);
				int i11 = MathHelper.floor_double(entityPlayer5.posY) + world.rand.nextInt(16) - world.rand.nextInt(16);
				if(i11 < 1) {
					i11 = 1;
				} else if(i11 > 128) {
					i11 = 128;
				}

				int i12 = world.rand.nextInt(class6.length);

				int i13;
				for(i13 = i11; i13 > 2 && !world.isBlockNormalCube(i9, i13 - 1, i10); --i13) {
				}

				while(!canCreatureTypeSpawnAtLocation(EnumCreatureType.monster, world, i9, i13, i10) && i13 < i11 + 16 && i13 < 128) {
					++i13;
				}

				if(i13 < i11 + 16 && i13 < 128) {
					float f14 = (float)i9 + 0.5F;
					float f15 = (float)i13;
					float f16 = (float)i10 + 0.5F;

					EntityLiving entityLiving17;
					try {
						entityLiving17 = (EntityLiving)class6[i12].getConstructor(new Class[]{World.class}).newInstance(new Object[]{world});
					} catch (Exception exception21) {
						exception21.printStackTrace();
						return z2;
					}

					entityLiving17.setLocationAndAngles((double)f14, (double)f15, (double)f16, world.rand.nextFloat() * 360.0F, 0.0F);
					if(entityLiving17.getCanSpawnHere()) {
						PathEntity pathEntity18 = pathfinder3.createEntityPathTo(entityLiving17, entityPlayer5, 32.0F);
						if(pathEntity18 != null && pathEntity18.pathLength > 1) {
							PathPoint pathPoint19 = pathEntity18.getPathPoint();
							if(Math.abs((double)pathPoint19.xCoord - entityPlayer5.posX) < 1.5D && Math.abs((double)pathPoint19.zCoord - entityPlayer5.posZ) < 1.5D && Math.abs((double)pathPoint19.yCoord - entityPlayer5.posY) < 1.5D) {
								ChunkCoordinates chunkCoordinates20 = BlockBed.getNearestEmptyChunkCoordinates(world, MathHelper.floor_double(entityPlayer5.posX), MathHelper.floor_double(entityPlayer5.posY), MathHelper.floor_double(entityPlayer5.posZ), 1);
								if(chunkCoordinates20 == null) {
									chunkCoordinates20 = new ChunkCoordinates(i9, i13 + 1, i10);
								}

								entityLiving17.setLocationAndAngles((double)((float)chunkCoordinates20.posX + 0.5F), (double)chunkCoordinates20.posY, (double)((float)chunkCoordinates20.posZ + 0.5F), 0.0F, 0.0F);
								world.spawnEntityInWorld(entityLiving17);
								creatureSpecificInit(entityLiving17, world, (float)chunkCoordinates20.posX + 0.5F, (float)chunkCoordinates20.posY, (float)chunkCoordinates20.posZ + 0.5F);
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
