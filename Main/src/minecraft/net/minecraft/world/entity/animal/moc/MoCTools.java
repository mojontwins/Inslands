package net.minecraft.world.entity.animal.moc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EnumCreatureType;
import net.minecraft.world.entity.animal.farm.EntityChicken;
import net.minecraft.world.entity.animal.farm.EntityCow;
import net.minecraft.world.entity.animal.farm.EntityPig;
import net.minecraft.world.entity.animal.farm.EntitySheep;
import net.minecraft.world.entity.animal.water.EntitySquid;
import net.minecraft.world.entity.animal.wild.EntityWolf;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPosition;
import net.minecraft.world.level.SpawnListEntry;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldChunkManager;
import net.minecraft.world.level.biome.BiomeGenBase;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathEntity;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.level.tile.BlockFluid;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.Vec3D;

public class MoCTools {

	public static boolean nearMaterialWithDistance(World world, Entity entity, Double distance, Material material) {
		AxisAlignedBB bounds = entity.boundingBox.expand(distance.doubleValue(), distance.doubleValue(), distance.doubleValue());
		int minX = MathHelper.floor_double(bounds.minX);
		int maxX = MathHelper.floor_double(bounds.maxX + 1.0D);
		int minY = MathHelper.floor_double(bounds.minY);
		int maxY = MathHelper.floor_double(bounds.maxY + 1.0D);
		int minZ = MathHelper.floor_double(bounds.minZ);
		int maxZ = MathHelper.floor_double(bounds.maxZ + 1.0D);

		for (int x = minX; x < maxX; ++x) {
			for (int y = minY; y < maxY; ++y) {
				for (int z = minZ; z < maxZ; ++z) {
					int blockId = world.getBlockID(x, y, z);
					if (blockId != 0 && Block.blocksList[blockId].blockMaterial == material) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public static boolean isNearTorch(World world, Entity entity) {
		return isNearBlockName(world, entity, 8.0D, "tile.torch");
	}

	public static boolean isNearTorch(World world, Entity entity, Double dist) {
		return isNearBlockName(world, entity, dist, "tile.torch");
	}

	public static boolean isNearBlockName(World world, Entity entity, Double dist, String blockName) {
		AxisAlignedBB bounds = entity.boundingBox.expand(dist.doubleValue(), dist.doubleValue() / 2.0D, dist.doubleValue());
		int minX = MathHelper.floor_double(bounds.minX);
		int maxX = MathHelper.floor_double(bounds.maxX + 1.0D);
		int minY = MathHelper.floor_double(bounds.minY);
		int maxY = MathHelper.floor_double(bounds.maxY + 1.0D);
		int minZ = MathHelper.floor_double(bounds.minZ);
		int maxZ = MathHelper.floor_double(bounds.maxZ + 1.0D);

		for (int x = minX; x < maxX; ++x) {
			for (int y = minY; y < maxY; ++y) {
				for (int z = minZ; z < maxZ; ++z) {
					int blockId = world.getBlockID(x, y, z);
					if (blockId != 0) {
						String nameToCheck = Block.blocksList[blockId].getBlockName();
						if (nameToCheck != null && nameToCheck != "" && nameToCheck.equals(blockName)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public static void checkForTwistedEntities(World world) {
		for (int i = 0; i < world.loadedEntityList.size(); ++i) {
			Entity entity = world.loadedEntityList.get(i);
			if (entity instanceof EntityLiving) {
				EntityLiving living = (EntityLiving) entity;
				if (living.deathTime > 0 && living.ridingEntity == null && living.health > 0) {
					living.deathTime = 0;
				}
			}
		}
	}

	public static double getSqDistanceTo(Entity entity, double x, double y, double z) {
		double dx = entity.posX - x;
		double dy = entity.posY - y;
		double dz = entity.posZ - z;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public static int[] returnNearestMaterialCoord(World world, Entity entity, Material material, Double distance, Double yOffset) {
		double shortestDistance = -1.0D;
		double currentDistance = 0.0D;
		int x = -9999;
		int y = -1;
		int z = -1;
		AxisAlignedBB bounds = entity.boundingBox.expand(distance.doubleValue(), yOffset.doubleValue(), distance.doubleValue());
		int minX = MathHelper.floor_double(bounds.minX);
		int maxX = MathHelper.floor_double(bounds.maxX + 1.0D);
		int minY = MathHelper.floor_double(bounds.minY);
		int maxY = MathHelper.floor_double(bounds.maxY + 1.0D);
		int minZ = MathHelper.floor_double(bounds.minZ);
		int maxZ = MathHelper.floor_double(bounds.maxZ + 1.0D);

		for (int bx = minX; bx < maxX; ++bx) {
			for (int by = minY; by < maxY; ++by) {
				for (int bz = minZ; bz < maxZ; ++bz) {
					int blockId = world.getBlockID(bx, by, bz);
					if (blockId != 0 && Block.blocksList[blockId].blockMaterial == material) {
						currentDistance = getSqDistanceTo(entity, bx, by, bz);
						if (shortestDistance == -1.0D) {
							x = bx;
							y = by;
							z = bz;
							shortestDistance = currentDistance;
						}

						if (currentDistance < shortestDistance) {
							x = bx;
							y = by;
							z = bz;
							shortestDistance = currentDistance;
						}
					}
				}
			}
		}

		if (entity.posX > x) {
			x -= 2;
		} else {
			x += 2;
		}

		if (entity.posZ > z) {
			z -= 2;
		} else {
			z += 2;
		}

		return new int[] { x, y, z };
	}

	public static void MoveCreatureToXYZ(World world, EntityCreature movingEntity, int x, int y, int z, float speed) {
		PathEntity path = world.getEntityPathToXYZ(movingEntity, x, y, z, speed, true, false, false, true);
		if (path != null) {
			movingEntity.setPathToEntity(path);
		}
	}

	public static void MoveToWater(World world, EntityCreature entity) {
		int[] coords = returnNearestMaterialCoord(world, entity, Material.water, 20.0D, 2.0D);
		if (coords[0] > -1000) {
			MoveCreatureToXYZ(world, entity, coords[0], coords[1], coords[2], 24.0F);
		}
	}

	public static float realAngle(float origAngle) {
		return origAngle % 360.0F;
	}

	public static void SlideEntityToXYZ(Entity entity, int x, int y, int z) {
		if (entity != null) {
			if (entity.posY < y) {
				entity.motionY += 0.15D;
			}

			double delta;
			if (entity.posX < x) {
				delta = x - entity.posX;
				if (delta > 0.5D) {
					entity.motionX += 0.05D;
				}
			} else {
				delta = entity.posX - x;
				if (delta > 0.5D) {
					entity.motionX -= 0.05D;
				}
			}

			if (entity.posZ < z) {
				delta = z - entity.posZ;
				if (delta > 0.5D) {
					entity.motionZ += 0.05D;
				}
			} else {
				delta = entity.posZ - z;
				if (delta > 0.5D) {
					entity.motionZ -= 0.05D;
				}
			}
		}
	}

	public static void destroyDrops(World world, Entity entity, double range) {
		List<Entity> drops = world.getEntitiesWithinAABBExcludingEntity(entity, entity.boundingBox.expand(range, range, range));

		for (int i = 0; i < drops.size(); ++i) {
			Entity entry = drops.get(i);
			if (entry instanceof EntityItem) {
				EntityItem item = (EntityItem) entry;
				if (item != null && item.age < 50) {
					item.setEntityDead();
				}
			}
		}
	}

	public static void dropGoodies(World world, Entity entity) {
		EntityItem item = new EntityItem(world, entity.posX, entity.posY, entity.posZ, new ItemStack(Block.wood, 64));
		item.delayBeforeCanPickup = 10;
		world.spawnEntityInWorld(item);
		EntityItem item2 = new EntityItem(world, entity.posX, entity.posY, entity.posZ, new ItemStack(Item.diamond, 64));
		item2.delayBeforeCanPickup = 10;
		world.spawnEntityInWorld(item2);
		EntityItem item3 = new EntityItem(world, entity.posX, entity.posY, entity.posZ, new ItemStack(Item.coal, 64));
		item3.delayBeforeCanPickup = 10;
		world.spawnEntityInWorld(item3);
		EntityItem item4 = new EntityItem(world, entity.posX, entity.posY, entity.posZ, new ItemStack(Block.stone, 64));
		item4.delayBeforeCanPickup = 10;
		world.spawnEntityInWorld(item4);
	}

	public static float distanceToSurface(World world, Entity entity) {
		int x = MathHelper.floor_double(entity.posX);
		int y = MathHelper.floor_double(entity.posY);
		int z = MathHelper.floor_double(entity.posZ);
		int blockId = world.getBlockID(x, y, z);
		if (blockId != 0 && Block.blocksList[blockId].blockMaterial == Material.water) {
			for (int offset = 1; offset < 64; ++offset) {
				blockId = world.getBlockID(x, y + offset, z);
				if (blockId == 0 || Block.blocksList[blockId].blockMaterial != Material.water) {
					return offset;
				}
			}
		}

		return 0.0F;
	}

	public boolean isInsideOfMaterial(World world, Material material, Entity entity) {
		double eyeY = entity.posY + entity.getEyeHeight();
		int x = MathHelper.floor_double(entity.posX);
		int y = MathHelper.floor_float(MathHelper.floor_double(eyeY));
		int z = MathHelper.floor_double(entity.posZ);
		int blockId = world.getBlockID(x, y, z);
		if (blockId != 0 && Block.blocksList[blockId].blockMaterial == material) {
			float heightPercent = BlockFluid.getFluidHeightPercent(world.getBlockMetadata(x, y, z)) - 0.1111111F;
			float surfaceY = y + 1 - heightPercent;
			return eyeY < surfaceY;
		} else {
			return false;
		}
	}

	public static void DestroyBlast(World world, Entity entity, double d, double d1, double d2, float f, boolean flag) {
		world.playSoundEffect(d, d1, d2, "destroy", 4.0F,
				(1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
		HashSet<ChunkPosition> hashset = new HashSet<ChunkPosition>();
		float f1 = f;
		byte i = 16;

		int k;
		int i1;
		int k1;
		double i4;
		double i5;
		double d14;
		for (k = 0; k < i; ++k) {
			for (i1 = 0; i1 < i; ++i1) {
				for (k1 = 0; k1 < i; ++k1) {
					if (k == 0 || k == i - 1 || i1 == 0 || i1 == i - 1 || k1 == 0 || k1 == i - 1) {
						double l1 = k / (i - 1.0F) * 2.0F - 1.0F;
						double j2 = i1 / (i - 1.0F) * 2.0F - 1.0F;
						double vec3d = k1 / (i - 1.0F) * 2.0F - 1.0F;
						double i3 = Math.sqrt(l1 * l1 + j2 * j2 + vec3d * vec3d);
						l1 /= i3;
						j2 /= i3;
						vec3d /= i3;
						float k3 = f * (0.7F + world.rand.nextFloat() * 0.6F);
						i4 = d;
						i5 = d1;
						d14 = d2;
						float d16 = 0.3F;

						for (float f4 = 5.0F; k3 > 0.0F; k3 -= d16 * 0.75F) {
							int d18 = MathHelper.floor_double(i4);
							int l5 = MathHelper.floor_double(i5);
							int d20 = MathHelper.floor_double(d14);
							int j6 = world.getBlockID(d18, l5, d20);
							if (j6 > 0) {
								f4 = Block.blocksList[j6].getHardness();
								k3 -= (Block.blocksList[j6].getExplosionResistance(entity) + 0.3F) * (d16 / 10.0F);
							}

							if (k3 > 0.0F && i5 > entity.posY && f4 < 3.0F) {
								hashset.add(new ChunkPosition(d18, l5, d20));
							}

							i4 += l1 * d16;
							i5 += j2 * d16;
							d14 += vec3d * d16;
						}
					}
				}
			}
		}

		f *= 2.0F;
		k = MathHelper.floor_double(d - f - 1.0D);
		i1 = MathHelper.floor_double(d + f + 1.0D);
		k1 = MathHelper.floor_double(d1 - f - 1.0D);
		int i44 = MathHelper.floor_double(d1 + f + 1.0D);
		int i2 = MathHelper.floor_double(d2 - f - 1.0D);
		int i45 = MathHelper.floor_double(d2 + f + 1.0D);
		List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(entity,
				AxisAlignedBB.getBoundingBoxFromPool(k, k1, i2, i1, i44, i45));
		Vec3D vec3D46 = Vec3D.createVector(d, d1, d2);

		double d54;
		double d55;
		double d56;
		for (int arraylist = 0; arraylist < list.size(); ++arraylist) {
			Entity entity48 = list.get(arraylist);
			double chunkposition1 = entity48.getDistance(d, d1, d2) / f;
			if (chunkposition1 <= 1.0D) {
				i4 = entity48.posX - d;
				i5 = entity48.posY - d1;
				d14 = entity48.posZ - d2;
				d54 = MathHelper.sqrt_double(i4 * i4 + i5 * i5 + d14 * d14);
				i4 /= d54;
				i5 /= d54;
				d14 /= d54;
				d55 = world.getBlockDensity(vec3D46, entity48.boundingBox);
				d56 = (1.0D - chunkposition1) * d55;

				entity48.attackEntityFrom(entity, (int) ((d56 * d56 + d56) / 2.0D * 3.0D * f + 1.0D));
				entity48.motionX += i4 * d56;
				entity48.motionY += i5 * d56;
				entity48.motionZ += d14 * d56;
			}
		}

		f = f1;
		ArrayList<ChunkPosition> arrayList47 = new ArrayList<ChunkPosition>();
		arrayList47.addAll(hashset);

		int k4;
		int i49;
		ChunkPosition chunkPosition50;
		int i51;
		int i52;
		int i53;
		for (i49 = arrayList47.size() - 1; i49 >= 0; --i49) {
			chunkPosition50 = arrayList47.get(i49);
			i51 = chunkPosition50.x;
			i52 = chunkPosition50.y;
			k4 = chunkPosition50.z;
			i53 = world.getBlockID(i51, i52, k4);

			for (int j5 = 0; j5 < 5; ++j5) {
				d14 = i51 + world.rand.nextFloat();
				d54 = i52 + world.rand.nextFloat();
				d55 = k4 + world.rand.nextFloat();
				d56 = d14 - d;
				double d22 = d54 - d1;
				double d23 = d55 - d2;
				double d24 = MathHelper.sqrt_double(d56 * d56 + d22 * d22 + d23 * d23);
				d56 /= d24;
				d22 /= d24;
				d23 /= d24;
				double d25 = 0.5D / (d24 / f + 0.1D);
				d25 *= world.rand.nextFloat() * world.rand.nextFloat() + 0.3F;
				--d25;
				d56 *= d25;
				d22 *= d25 - 1.0D;
				d23 *= d25;
				world.spawnParticle("explode", (d14 + d * 1.0D) / 2.0D, (d54 + d1 * 1.0D) / 2.0D,
						(d55 + d2 * 1.0D) / 2.0D, d56, d22, d23);
				entity.motionX -= 0.001000000047497451D;
				entity.motionY -= 0.001000000047497451D;
			}

			if (i53 > 0) {
				Block.blocksList[i53].dropBlockAsItemWithChance(world, i51, i52, k4,
						world.getBlockMetadata(i51, i52, k4), 0.3F);
				world.setBlockWithNotify(i51, i52, k4, 0);
				Block.blocksList[i53].onBlockDestroyedByExplosion(world, i51, i52, k4);
			}
		}

		if (flag) {
			for (i49 = arrayList47.size() - 1; i49 >= 0; --i49) {
				chunkPosition50 = arrayList47.get(i49);
				i51 = chunkPosition50.x;
				i52 = chunkPosition50.y;
				k4 = chunkPosition50.z;
				i53 = world.getBlockID(i51, i52, k4);
				if (i53 == 0 && world.rand.nextInt(8) == 0) {
					world.setBlockWithNotify(i51, i52, k4, Block.fire.blockID);
				}
			}
		}
	}

	public static void disorientEntity(World world, Entity entity) {
		double rotD = 0.0D;
		double motD = 0.0D;
		double random = world.rand.nextGaussian();
		double motRandom = 0.1D * random;
		motD = 0.2D * motRandom + 0.8D * motD;
		entity.motionX += motD;
		entity.motionZ += motD;
		double rotRandom = 0.78D * random;
		rotD = 0.125D * rotRandom + 0.875D * rotD;
		entity.rotationYaw = (float) (entity.rotationYaw + rotD);
		entity.rotationPitch = (float) (entity.rotationPitch + rotD);
	}

	public static void slowEntity(Entity entity) {
		entity.motionX *= 0.8D;
		entity.motionZ *= 0.8D;
	}

	public static int colorize(int color) {
		return ~color & 15;
	}

	protected static int entityDespawnCheck(World world, EntityLiving entityLiving) {
		int count = 0;
		EntityPlayer player = world.getClosestPlayerToEntity(entityLiving, -1.0D);
		if (player != null) {
			double dx = player.posX - entityLiving.posX;
			double dy = player.posY - entityLiving.posY;
			double dz = player.posZ - entityLiving.posZ;
			double distanceSq = dx * dx + dy * dy + dz * dz;
			if (distanceSq > 16384.0D) {
				entityLiving.setEntityDead();
				++count;
			}

			if (entityLiving.entityAge > 600 && world.rand.nextInt(800) == 0) {
				if (distanceSq < 1024.0D) {
					entityLiving.entityAge = 0;
				} else {
					entityLiving.setEntityDead();
					++count;
				}
			}
		}

		return count;
	}

	public int countEntities(Class<?> entityClass, World world) {
		int count = 0;

		for (int i = 0; i < world.loadedEntityList.size(); ++i) {
			Entity entity = world.loadedEntityList.get(i);
			if (entityClass.isAssignableFrom(entity.getClass())) {
				++count;
			}
		}

		return count;
	}

	public static int despawnVanillaAnimals(World world) {
		return despawnVanillaAnimals(world, (List<Class<?>>[]) null);
	}

	public static int despawnVanillaAnimals(World world, List<Class<?>>[] classList) {
		int count = 0;

		for (int i = 0; i < world.loadedEntityList.size(); ++i) {
			Entity entity = world.loadedEntityList.get(i);
			if (entity instanceof EntityLiving && (entity instanceof EntityCow || entity instanceof EntitySheep
					|| entity instanceof EntityPig || entity instanceof EntityChicken || entity instanceof EntitySquid
					|| entity instanceof EntityWolf)) {
				count += entityDespawnCheck(world, (EntityLiving) entity);
			}
		}

		return count;
	}

	public static List<SpawnListEntry> spawnList(World world, EnumCreatureType creatureType, int x, int y, int z) {
		WorldChunkManager chunkManager = world.getWorldChunkManager();
		if (chunkManager == null) {
			return null;
		} else {
			BiomeGenBase biome = chunkManager.getBiomeGenAt(x >> 4, z >> 4);
			return biome == null ? null : biome.getSpawnableList(creatureType);
		}
	}

	public static BiomeGenBase whatBiome(World world, int x, int y, int z) {
		WorldChunkManager chunkManager = world.getWorldChunkManager();
		if (chunkManager == null) {
			return null;
		} else {
			BiomeGenBase biome = chunkManager.getBiomeGenAt(x, z);
			return biome == null ? null : biome;
		}
	}

	public static float distToPlayer(Entity entity) {
		return 0.0F;
	}

	public static String BiomeName(World world, int x, int y, int z) {
		WorldChunkManager chunkManager = world.getWorldChunkManager();
		if (chunkManager == null) {
			return null;
		} else {
			BiomeGenBase biome = chunkManager.getBiomeGenAt(x, z);
			return biome == null ? null : biome.biomeName;
		}
	}

	public static EntityItem getClosestEntityItem(World world, Entity entity, double distance) {
		double bestDistance = -1D;
		EntityItem closestItem = null;
		List<Entity> items = world.getEntitiesWithinAABBExcludingEntity(entity, entity.boundingBox.expand(distance, distance, distance));
		for (int i = 0; i < items.size(); i++) {
			Entity entry = items.get(i);
			if (!(entry instanceof EntityItem)) {
				continue;
			}
			EntityItem item = (EntityItem) entry;
			double itemDistance = item.getDistanceSq(entity.posX, entity.posY, entity.posZ);
			if (((distance < 0.0D) || (itemDistance < (distance * distance))) && ((bestDistance == -1D) || (itemDistance < bestDistance))) {
				bestDistance = itemDistance;
				closestItem = item;
			}
		}

		return closestItem;
	}

	public static void bigsmack(Entity smacker, Entity smacked, float force) {
		double dx = smacker.posX - smacked.posX;
		double dz = smacker.posZ - smacked.posZ;
		for (dz = smacker.posZ - smacked.posZ; ((dx * dx) + (dz * dz)) < 0.0001D; dz = (Math.random() - Math.random())
				* 0.01D) {
			dx = (Math.random() - Math.random()) * 0.01D;
		}

		float distance = MathHelper.sqrt_double((dx * dx) + (dz * dz));
		smacked.motionX /= 2D;
		smacked.motionY /= 2D;
		smacked.motionZ /= 2D;
		smacked.motionX -= (dx / distance) * force;
		smacked.motionY += force;
		smacked.motionZ -= (dz / distance) * force;
		if (smacked.motionY > force) {
			smacked.motionY = force;
		}
	}


}
