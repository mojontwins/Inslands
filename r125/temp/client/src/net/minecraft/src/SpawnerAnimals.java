package net.minecraft.src;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public final class SpawnerAnimals {
	private static HashMap eligibleChunksForSpawning = new HashMap();
	protected static final Class[] nightSpawnEntities = new Class[]{EntitySpider.class, EntityZombie.class, EntitySkeleton.class};

	protected static ChunkPosition getRandomSpawningPointInChunk(World world0, int i1, int i2) {
		Chunk chunk3 = world0.getChunkFromChunkCoords(i1, i2);
		int i4 = i1 * 16 + world0.rand.nextInt(16);
		int i5 = world0.rand.nextInt(chunk3 == null ? 128 : Math.max(128, chunk3.getTopFilledSegment()));
		int i6 = i2 * 16 + world0.rand.nextInt(16);
		return new ChunkPosition(i4, i5, i6);
	}

	public static final int performSpawning(World world0, boolean z1, boolean z2) {
		if(!z1 && !z2) {
			return 0;
		} else {
			eligibleChunksForSpawning.clear();

			int i3;
			int i6;
			for(i3 = 0; i3 < world0.playerEntities.size(); ++i3) {
				EntityPlayer entityPlayer4 = (EntityPlayer)world0.playerEntities.get(i3);
				int i5 = MathHelper.floor_double(entityPlayer4.posX / 16.0D);
				i6 = MathHelper.floor_double(entityPlayer4.posZ / 16.0D);
				byte b7 = 8;

				for(int i8 = -b7; i8 <= b7; ++i8) {
					for(int i9 = -b7; i9 <= b7; ++i9) {
						boolean z10 = i8 == -b7 || i8 == b7 || i9 == -b7 || i9 == b7;
						ChunkCoordIntPair chunkCoordIntPair11 = new ChunkCoordIntPair(i8 + i5, i9 + i6);
						if(!z10) {
							eligibleChunksForSpawning.put(chunkCoordIntPair11, false);
						} else if(!eligibleChunksForSpawning.containsKey(chunkCoordIntPair11)) {
							eligibleChunksForSpawning.put(chunkCoordIntPair11, true);
						}
					}
				}
			}

			i3 = 0;
			ChunkCoordinates chunkCoordinates31 = world0.getSpawnPoint();
			EnumCreatureType[] enumCreatureType32 = EnumCreatureType.values();
			i6 = enumCreatureType32.length;

			label126:
			for(int i33 = 0; i33 < i6; ++i33) {
				EnumCreatureType enumCreatureType34 = enumCreatureType32[i33];
				if((!enumCreatureType34.getPeacefulCreature() || z2) && (enumCreatureType34.getPeacefulCreature() || z1) && world0.countEntities(enumCreatureType34.getCreatureClass()) <= enumCreatureType34.getMaxNumberOfCreature() * eligibleChunksForSpawning.size() / 256) {
					Iterator iterator35 = eligibleChunksForSpawning.keySet().iterator();

					label123:
					while(true) {
						int i12;
						int i13;
						int i14;
						do {
							do {
								ChunkCoordIntPair chunkCoordIntPair36;
								do {
									if(!iterator35.hasNext()) {
										continue label126;
									}

									chunkCoordIntPair36 = (ChunkCoordIntPair)iterator35.next();
								} while(((Boolean)eligibleChunksForSpawning.get(chunkCoordIntPair36)).booleanValue());

								ChunkPosition chunkPosition37 = getRandomSpawningPointInChunk(world0, chunkCoordIntPair36.chunkXPos, chunkCoordIntPair36.chunkZPos);
								i12 = chunkPosition37.x;
								i13 = chunkPosition37.y;
								i14 = chunkPosition37.z;
							} while(world0.isBlockNormalCube(i12, i13, i14));
						} while(world0.getBlockMaterial(i12, i13, i14) != enumCreatureType34.getCreatureMaterial());

						int i15 = 0;

						for(int i16 = 0; i16 < 3; ++i16) {
							int i17 = i12;
							int i18 = i13;
							int i19 = i14;
							byte b20 = 6;
							SpawnListEntry spawnListEntry21 = null;

							for(int i22 = 0; i22 < 4; ++i22) {
								i17 += world0.rand.nextInt(b20) - world0.rand.nextInt(b20);
								i18 += world0.rand.nextInt(1) - world0.rand.nextInt(1);
								i19 += world0.rand.nextInt(b20) - world0.rand.nextInt(b20);
								if(canCreatureTypeSpawnAtLocation(enumCreatureType34, world0, i17, i18, i19)) {
									float f23 = (float)i17 + 0.5F;
									float f24 = (float)i18;
									float f25 = (float)i19 + 0.5F;
									if(world0.getClosestPlayer((double)f23, (double)f24, (double)f25, 24.0D) == null) {
										float f26 = f23 - (float)chunkCoordinates31.posX;
										float f27 = f24 - (float)chunkCoordinates31.posY;
										float f28 = f25 - (float)chunkCoordinates31.posZ;
										float f29 = f26 * f26 + f27 * f27 + f28 * f28;
										if(f29 >= 576.0F) {
											if(spawnListEntry21 == null) {
												spawnListEntry21 = world0.getRandomMob(enumCreatureType34, i17, i18, i19);
												if(spawnListEntry21 == null) {
													break;
												}
											}

											EntityLiving entityLiving38;
											try {
												entityLiving38 = (EntityLiving)spawnListEntry21.entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{world0});
											} catch (Exception exception30) {
												exception30.printStackTrace();
												return i3;
											}

											entityLiving38.setLocationAndAngles((double)f23, (double)f24, (double)f25, world0.rand.nextFloat() * 360.0F, 0.0F);
											if(entityLiving38.getCanSpawnHere()) {
												++i15;
												world0.spawnEntityInWorld(entityLiving38);
												creatureSpecificInit(entityLiving38, world0, f23, f24, f25);
												if(i15 >= entityLiving38.getMaxSpawnedInChunk()) {
													continue label123;
												}
											}

											i3 += i15;
										}
									}
								}
							}
						}
					}
				}
			}

			return i3;
		}
	}

	public static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType enumCreatureType0, World world1, int i2, int i3, int i4) {
		if(enumCreatureType0.getCreatureMaterial() == Material.water) {
			return world1.getBlockMaterial(i2, i3, i4).isLiquid() && !world1.isBlockNormalCube(i2, i3 + 1, i4);
		} else {
			int i5 = world1.getBlockId(i2, i3 - 1, i4);
			return Block.isNormalCube(i5) && i5 != Block.bedrock.blockID && !world1.isBlockNormalCube(i2, i3, i4) && !world1.getBlockMaterial(i2, i3, i4).isLiquid() && !world1.isBlockNormalCube(i2, i3 + 1, i4);
		}
	}

	private static void creatureSpecificInit(EntityLiving entityLiving0, World world1, float f2, float f3, float f4) {
		if(entityLiving0 instanceof EntitySpider && world1.rand.nextInt(100) == 0) {
			EntitySkeleton entitySkeleton7 = new EntitySkeleton(world1);
			entitySkeleton7.setLocationAndAngles((double)f2, (double)f3, (double)f4, entityLiving0.rotationYaw, 0.0F);
			world1.spawnEntityInWorld(entitySkeleton7);
			entitySkeleton7.mountEntity(entityLiving0);
		} else if(entityLiving0 instanceof EntitySheep) {
			((EntitySheep)entityLiving0).setFleeceColor(EntitySheep.getRandomFleeceColor(world1.rand));
		} else if(entityLiving0 instanceof EntityOcelot && world1.rand.nextInt(7) == 0) {
			for(int i5 = 0; i5 < 2; ++i5) {
				EntityOcelot entityOcelot6 = new EntityOcelot(world1);
				entityOcelot6.setLocationAndAngles((double)f2, (double)f3, (double)f4, entityLiving0.rotationYaw, 0.0F);
				entityOcelot6.setGrowingAge(-24000);
				world1.spawnEntityInWorld(entityOcelot6);
			}
		}

	}

	public static void performWorldGenSpawning(World world0, BiomeGenBase biomeGenBase1, int i2, int i3, int i4, int i5, Random random6) {
		List list7 = biomeGenBase1.getSpawnableList(EnumCreatureType.creature);
		if(!list7.isEmpty()) {
			while(random6.nextFloat() < biomeGenBase1.getSpawningChance()) {
				SpawnListEntry spawnListEntry8 = (SpawnListEntry)WeightedRandom.getRandomItem(world0.rand, (Collection)list7);
				int i9 = spawnListEntry8.minGroupCount + random6.nextInt(1 + spawnListEntry8.maxGroupCount - spawnListEntry8.minGroupCount);
				int i10 = i2 + random6.nextInt(i4);
				int i11 = i3 + random6.nextInt(i5);
				int i12 = i10;
				int i13 = i11;

				for(int i14 = 0; i14 < i9; ++i14) {
					boolean z15 = false;

					for(int i16 = 0; !z15 && i16 < 4; ++i16) {
						int i17 = world0.getTopSolidOrLiquidBlock(i10, i11);
						if(canCreatureTypeSpawnAtLocation(EnumCreatureType.creature, world0, i10, i17, i11)) {
							float f18 = (float)i10 + 0.5F;
							float f19 = (float)i17;
							float f20 = (float)i11 + 0.5F;

							EntityLiving entityLiving21;
							try {
								entityLiving21 = (EntityLiving)spawnListEntry8.entityClass.getConstructor(new Class[]{World.class}).newInstance(new Object[]{world0});
							} catch (Exception exception23) {
								exception23.printStackTrace();
								continue;
							}

							entityLiving21.setLocationAndAngles((double)f18, (double)f19, (double)f20, random6.nextFloat() * 360.0F, 0.0F);
							world0.spawnEntityInWorld(entityLiving21);
							creatureSpecificInit(entityLiving21, world0, f18, f19, f20);
							z15 = true;
						}

						i10 += random6.nextInt(5) - random6.nextInt(5);

						for(i11 += random6.nextInt(5) - random6.nextInt(5); i10 < i2 || i10 >= i2 + i4 || i11 < i3 || i11 >= i3 + i4; i11 = i13 + random6.nextInt(5) - random6.nextInt(5)) {
							i10 = i12 + random6.nextInt(5) - random6.nextInt(5);
						}
					}
				}
			}

		}
	}
}
