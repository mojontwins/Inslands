package net.minecraft.game.world;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityLiving;
import net.minecraft.game.world.material.Material;

import util.IProgressUpdate;
import util.MathHelper;

public final class MobSpawner {
	private int maxSpawns;
	private Class entityType;
	private Class[] entities;

	public MobSpawner(int i1, Class class2, Class[] class3) {
		this.maxSpawns = i1;
		this.entityType = class2;
		this.entities = class3;
	}

	public final void onUpdate(World world1) {
		if(world1.countEntities(this.entityType) < this.maxSpawns) {
			this.performSpawning(world1, 10, world1.playerEntity, (IProgressUpdate)null);
		}

	}

	private int performSpawning(World world1, int i2, Entity entity3, IProgressUpdate iProgressUpdate4) {
		i2 = 0;
		int i30 = MathHelper.floor_double(entity3.posX);
		int i5 = MathHelper.floor_double(entity3.posZ);

		for(int i6 = 0; i6 < 10; ++i6) {
			int i7 = world1.rand.nextInt(this.entities.length);
			int i8 = i30 + world1.rand.nextInt(256) - 128;
			int i9 = world1.rand.nextInt(128);
			int i10 = i5 + world1.rand.nextInt(256) - 128;
			if(!world1.isSolid(i8, i9, i10) && world1.getBlockMaterial(i8, i9, i10) == Material.air) {
				for(int i11 = 0; i11 < 6; ++i11) {
					int i12 = i8;
					int i13 = i9;
					int i14 = i10;

					for(int i15 = 0; i15 < 6; ++i15) {
						i12 += world1.rand.nextInt(6) - world1.rand.nextInt(6);
						i13 += world1.rand.nextInt(1) - world1.rand.nextInt(1);
						i14 += world1.rand.nextInt(6) - world1.rand.nextInt(6);
						if(world1.isSolid(i12, i13 - 1, i14) && !world1.isSolid(i12, i13, i14) && !world1.getBlockMaterial(i12, i13, i14).getIsLiquid() && !world1.isSolid(i12, i13 + 1, i14)) {
							float f16 = (float)i12 + 0.5F;
							float f17 = (float)i13 + 1.0F;
							float f18 = (float)i14 + 0.5F;
							if(entity3 != null) {
								double d21 = (double)f16 - entity3.posX;
								double d23 = (double)f17 - entity3.posY;
								double d25 = (double)f18 - entity3.posZ;
								if(d21 * d21 + d23 * d23 + d25 * d25 < 256.0D) {
									continue;
								}
							} else {
								float f31 = f16 - (float)world1.spawnX;
								float f22 = f17 - (float)world1.spawnY;
								float f33 = f18 - (float)world1.spawnZ;
								if(f31 * f31 + f22 * f22 + f33 * f33 < 256.0F) {
									continue;
								}
							}

							EntityLiving entityLiving32;
							try {
								entityLiving32 = (EntityLiving)this.entities[i7].getConstructor(new Class[]{World.class}).newInstance(new Object[]{world1});
							} catch (Exception exception29) {
								exception29.printStackTrace();
								return i2;
							}

							entityLiving32.setLocationAndAngles((double)f16, (double)f17, (double)f18, world1.rand.nextFloat() * 360.0F, 0.0F);
							if(entityLiving32.getCanSpawnHere(f16, f17, f18)) {
								++i2;
								world1.spawnEntityInWorld(entityLiving32);
							}
						}
					}
				}
			}
		}

		return i2;
	}
}