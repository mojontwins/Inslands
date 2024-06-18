package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class VillageSiege {
	private World field_48510_a;
	private boolean field_48508_b = false;
	private int field_48509_c = -1;
	private int field_48506_d;
	private int field_48507_e;
	private Village field_48504_f;
	private int field_48505_g;
	private int field_48511_h;
	private int field_48512_i;

	public VillageSiege(World world1) {
		this.field_48510_a = world1;
	}

	public void tick() {
		boolean z1 = false;
		if(z1) {
			if(this.field_48509_c == 2) {
				this.field_48506_d = 100;
				return;
			}
		} else {
			if(this.field_48510_a.isDaytime()) {
				this.field_48509_c = 0;
				return;
			}

			if(this.field_48509_c == 2) {
				return;
			}

			if(this.field_48509_c == 0) {
				float f2 = this.field_48510_a.getCelestialAngle(0.0F);
				if((double)f2 < 0.5D || (double)f2 > 0.501D) {
					return;
				}

				this.field_48509_c = this.field_48510_a.rand.nextInt(10) == 0 ? 1 : 2;
				this.field_48508_b = false;
				if(this.field_48509_c == 2) {
					return;
				}
			}
		}

		if(!this.field_48508_b) {
			if(!this.func_48502_b()) {
				return;
			}

			this.field_48508_b = true;
		}

		if(this.field_48507_e > 0) {
			--this.field_48507_e;
		} else {
			this.field_48507_e = 2;
			if(this.field_48506_d > 0) {
				this.func_48503_c();
				--this.field_48506_d;
			} else {
				this.field_48509_c = 2;
			}

		}
	}

	private boolean func_48502_b() {
		List list1 = this.field_48510_a.playerEntities;
		Iterator iterator2 = list1.iterator();

		Vec3D vec3D10;
		do {
			do {
				do {
					do {
						do {
							if(!iterator2.hasNext()) {
								return false;
							}

							EntityPlayer entityPlayer3 = (EntityPlayer)iterator2.next();
							this.field_48504_f = this.field_48510_a.villageCollectionObj.findNearestVillage((int)entityPlayer3.posX, (int)entityPlayer3.posY, (int)entityPlayer3.posZ, 1);
						} while(this.field_48504_f == null);
					} while(this.field_48504_f.getNumVillageDoors() < 10);
				} while(this.field_48504_f.getTicksSinceLastDoorAdding() < 20);
			} while(this.field_48504_f.getNumVillagers() < 20);

			ChunkCoordinates chunkCoordinates4 = this.field_48504_f.getCenter();
			float f5 = (float)this.field_48504_f.getVillageRadius();
			boolean z6 = false;

			for(int i7 = 0; i7 < 10; ++i7) {
				this.field_48505_g = chunkCoordinates4.posX + (int)((double)(MathHelper.cos(this.field_48510_a.rand.nextFloat() * (float)Math.PI * 2.0F) * f5) * 0.9D);
				this.field_48511_h = chunkCoordinates4.posY;
				this.field_48512_i = chunkCoordinates4.posZ + (int)((double)(MathHelper.sin(this.field_48510_a.rand.nextFloat() * (float)Math.PI * 2.0F) * f5) * 0.9D);
				z6 = false;
				Iterator iterator8 = this.field_48510_a.villageCollectionObj.func_48628_b().iterator();

				while(iterator8.hasNext()) {
					Village village9 = (Village)iterator8.next();
					if(village9 != this.field_48504_f && village9.isInRange(this.field_48505_g, this.field_48511_h, this.field_48512_i)) {
						z6 = true;
						break;
					}
				}

				if(!z6) {
					break;
				}
			}

			if(z6) {
				return false;
			}

			vec3D10 = this.func_48501_a(this.field_48505_g, this.field_48511_h, this.field_48512_i);
		} while(vec3D10 == null);

		this.field_48507_e = 0;
		this.field_48506_d = 20;
		return true;
	}

	private boolean func_48503_c() {
		Vec3D vec3D1 = this.func_48501_a(this.field_48505_g, this.field_48511_h, this.field_48512_i);
		if(vec3D1 == null) {
			return false;
		} else {
			EntityZombie entityZombie2;
			try {
				entityZombie2 = new EntityZombie(this.field_48510_a);
			} catch (Exception exception4) {
				exception4.printStackTrace();
				return false;
			}

			entityZombie2.setLocationAndAngles(vec3D1.xCoord, vec3D1.yCoord, vec3D1.zCoord, this.field_48510_a.rand.nextFloat() * 360.0F, 0.0F);
			this.field_48510_a.spawnEntityInWorld(entityZombie2);
			ChunkCoordinates chunkCoordinates3 = this.field_48504_f.getCenter();
			entityZombie2.setHomeArea(chunkCoordinates3.posX, chunkCoordinates3.posY, chunkCoordinates3.posZ, this.field_48504_f.getVillageRadius());
			return true;
		}
	}

	private Vec3D func_48501_a(int i1, int i2, int i3) {
		for(int i4 = 0; i4 < 10; ++i4) {
			int i5 = i1 + this.field_48510_a.rand.nextInt(16) - 8;
			int i6 = i2 + this.field_48510_a.rand.nextInt(6) - 3;
			int i7 = i3 + this.field_48510_a.rand.nextInt(16) - 8;
			if(this.field_48504_f.isInRange(i5, i6, i7) && SpawnerAnimals.canCreatureTypeSpawnAtLocation(EnumCreatureType.monster, this.field_48510_a, i5, i6, i7)) {
				return Vec3D.createVector((double)i5, (double)i6, (double)i7);
			}
		}

		return null;
	}
}
