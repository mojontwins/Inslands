package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class EntityDragon extends EntityDragonBase {
	public double targetX;
	public double targetY;
	public double targetZ;
	public double[][] field_40144_d = new double[64][3];
	public int field_40145_e = -1;
	public EntityDragonPart[] dragonPartArray = new EntityDragonPart[]{this.dragonPartHead = new EntityDragonPart(this, "head", 6.0F, 6.0F), this.dragonPartBody = new EntityDragonPart(this, "body", 8.0F, 8.0F), this.dragonPartTail1 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail2 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail3 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartWing1 = new EntityDragonPart(this, "wing", 4.0F, 4.0F), this.dragonPartWing2 = new EntityDragonPart(this, "wing", 4.0F, 4.0F)};
	public EntityDragonPart dragonPartHead;
	public EntityDragonPart dragonPartBody;
	public EntityDragonPart dragonPartTail1;
	public EntityDragonPart dragonPartTail2;
	public EntityDragonPart dragonPartTail3;
	public EntityDragonPart dragonPartWing1;
	public EntityDragonPart dragonPartWing2;
	public float field_40149_n = 0.0F;
	public float field_40150_o = 0.0F;
	public boolean field_40160_p = false;
	public boolean field_40159_q = false;
	private Entity target;
	public int field_40158_r = 0;
	public EntityEnderCrystal healingEnderCrystal = null;

	public EntityDragon(World world1) {
		super(world1);
		this.maxHealth = 200;
		this.setEntityHealth(this.maxHealth);
		this.texture = "/mob/enderdragon/ender.png";
		this.setSize(16.0F, 8.0F);
		this.noClip = true;
		this.isImmuneToFire = true;
		this.targetY = 100.0D;
		this.ignoreFrustumCheck = true;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, new Integer(this.maxHealth));
	}

	public double[] func_40139_a(int i1, float f2) {
		if(this.health <= 0) {
			f2 = 0.0F;
		}

		f2 = 1.0F - f2;
		int i3 = this.field_40145_e - i1 * 1 & 63;
		int i4 = this.field_40145_e - i1 * 1 - 1 & 63;
		double[] d5 = new double[3];
		double d6 = this.field_40144_d[i3][0];

		double d8;
		for(d8 = this.field_40144_d[i4][0] - d6; d8 < -180.0D; d8 += 360.0D) {
		}

		while(d8 >= 180.0D) {
			d8 -= 360.0D;
		}

		d5[0] = d6 + d8 * (double)f2;
		d6 = this.field_40144_d[i3][1];
		d8 = this.field_40144_d[i4][1] - d6;
		d5[1] = d6 + d8 * (double)f2;
		d5[2] = this.field_40144_d[i3][2] + (this.field_40144_d[i4][2] - this.field_40144_d[i3][2]) * (double)f2;
		return d5;
	}

	public void onLivingUpdate() {
		this.field_40149_n = this.field_40150_o;
		if(!this.worldObj.isRemote) {
			this.dataWatcher.updateObject(16, this.health);
		}

		float f1;
		float f3;
		float f26;
		if(this.health <= 0) {
			f1 = (this.rand.nextFloat() - 0.5F) * 8.0F;
			f26 = (this.rand.nextFloat() - 0.5F) * 4.0F;
			f3 = (this.rand.nextFloat() - 0.5F) * 8.0F;
			this.worldObj.spawnParticle("largeexplode", this.posX + (double)f1, this.posY + 2.0D + (double)f26, this.posZ + (double)f3, 0.0D, 0.0D, 0.0D);
		} else {
			this.updateDragonEnderCrystal();
			f1 = 0.2F / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
			f1 *= (float)Math.pow(2.0D, this.motionY);
			if(this.field_40159_q) {
				this.field_40150_o += f1 * 0.5F;
			} else {
				this.field_40150_o += f1;
			}

			while(this.rotationYaw >= 180.0F) {
				this.rotationYaw -= 360.0F;
			}

			while(this.rotationYaw < -180.0F) {
				this.rotationYaw += 360.0F;
			}

			if(this.field_40145_e < 0) {
				for(int i2 = 0; i2 < this.field_40144_d.length; ++i2) {
					this.field_40144_d[i2][0] = (double)this.rotationYaw;
					this.field_40144_d[i2][1] = this.posY;
				}
			}

			if(++this.field_40145_e == this.field_40144_d.length) {
				this.field_40145_e = 0;
			}

			this.field_40144_d[this.field_40145_e][0] = (double)this.rotationYaw;
			this.field_40144_d[this.field_40145_e][1] = this.posY;
			double d4;
			double d6;
			double d8;
			double d25;
			float f31;
			if(this.worldObj.isRemote) {
				if(this.newPosRotationIncrements > 0) {
					d25 = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
					d4 = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
					d6 = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;

					for(d8 = this.newRotationYaw - (double)this.rotationYaw; d8 < -180.0D; d8 += 360.0D) {
					}

					while(d8 >= 180.0D) {
						d8 -= 360.0D;
					}

					this.rotationYaw = (float)((double)this.rotationYaw + d8 / (double)this.newPosRotationIncrements);
					this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
					--this.newPosRotationIncrements;
					this.setPosition(d25, d4, d6);
					this.setRotation(this.rotationYaw, this.rotationPitch);
				}
			} else {
				d25 = this.targetX - this.posX;
				d4 = this.targetY - this.posY;
				d6 = this.targetZ - this.posZ;
				d8 = d25 * d25 + d4 * d4 + d6 * d6;
				if(this.target != null) {
					this.targetX = this.target.posX;
					this.targetZ = this.target.posZ;
					double d10 = this.targetX - this.posX;
					double d12 = this.targetZ - this.posZ;
					double d14 = Math.sqrt(d10 * d10 + d12 * d12);
					double d16 = (double)0.4F + d14 / 80.0D - 1.0D;
					if(d16 > 10.0D) {
						d16 = 10.0D;
					}

					this.targetY = this.target.boundingBox.minY + d16;
				} else {
					this.targetX += this.rand.nextGaussian() * 2.0D;
					this.targetZ += this.rand.nextGaussian() * 2.0D;
				}

				if(this.field_40160_p || d8 < 100.0D || d8 > 22500.0D || this.isCollidedHorizontally || this.isCollidedVertically) {
					this.func_41037_w();
				}

				d4 /= (double)MathHelper.sqrt_double(d25 * d25 + d6 * d6);
				f31 = 0.6F;
				if(d4 < (double)(-f31)) {
					d4 = (double)(-f31);
				}

				if(d4 > (double)f31) {
					d4 = (double)f31;
				}

				for(this.motionY += d4 * (double)0.1F; this.rotationYaw < -180.0F; this.rotationYaw += 360.0F) {
				}

				while(this.rotationYaw >= 180.0F) {
					this.rotationYaw -= 360.0F;
				}

				double d11 = 180.0D - Math.atan2(d25, d6) * 180.0D / (double)(float)Math.PI;

				double d13;
				for(d13 = d11 - (double)this.rotationYaw; d13 < -180.0D; d13 += 360.0D) {
				}

				while(d13 >= 180.0D) {
					d13 -= 360.0D;
				}

				if(d13 > 50.0D) {
					d13 = 50.0D;
				}

				if(d13 < -50.0D) {
					d13 = -50.0D;
				}

				Vec3D vec3D15 = Vec3D.createVector(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ).normalize();
				Vec3D vec3D39 = Vec3D.createVector((double)MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F), this.motionY, (double)(-MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F))).normalize();
				float f17 = (float)(vec3D39.dotProduct(vec3D15) + 0.5D) / 1.5F;
				if(f17 < 0.0F) {
					f17 = 0.0F;
				}

				this.randomYawVelocity *= 0.8F;
				float f18 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0F + 1.0F;
				double d19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0D + 1.0D;
				if(d19 > 40.0D) {
					d19 = 40.0D;
				}

				this.randomYawVelocity = (float)((double)this.randomYawVelocity + d13 * ((double)0.7F / d19 / (double)f18));
				this.rotationYaw += this.randomYawVelocity * 0.1F;
				float f21 = (float)(2.0D / (d19 + 1.0D));
				float f22 = 0.06F;
				this.moveFlying(0.0F, -1.0F, f22 * (f17 * f21 + (1.0F - f21)));
				if(this.field_40159_q) {
					this.moveEntity(this.motionX * (double)0.8F, this.motionY * (double)0.8F, this.motionZ * (double)0.8F);
				} else {
					this.moveEntity(this.motionX, this.motionY, this.motionZ);
				}

				Vec3D vec3D23 = Vec3D.createVector(this.motionX, this.motionY, this.motionZ).normalize();
				float f24 = (float)(vec3D23.dotProduct(vec3D39) + 1.0D) / 2.0F;
				f24 = 0.8F + 0.15F * f24;
				this.motionX *= (double)f24;
				this.motionZ *= (double)f24;
				this.motionY *= (double)0.91F;
			}

			this.renderYawOffset = this.rotationYaw;
			this.dragonPartHead.width = this.dragonPartHead.height = 3.0F;
			this.dragonPartTail1.width = this.dragonPartTail1.height = 2.0F;
			this.dragonPartTail2.width = this.dragonPartTail2.height = 2.0F;
			this.dragonPartTail3.width = this.dragonPartTail3.height = 2.0F;
			this.dragonPartBody.height = 3.0F;
			this.dragonPartBody.width = 5.0F;
			this.dragonPartWing1.height = 2.0F;
			this.dragonPartWing1.width = 4.0F;
			this.dragonPartWing2.height = 3.0F;
			this.dragonPartWing2.width = 4.0F;
			f26 = (float)(this.func_40139_a(5, 1.0F)[1] - this.func_40139_a(10, 1.0F)[1]) * 10.0F / 180.0F * (float)Math.PI;
			f3 = MathHelper.cos(f26);
			float f27 = -MathHelper.sin(f26);
			float f5 = this.rotationYaw * (float)Math.PI / 180.0F;
			float f28 = MathHelper.sin(f5);
			float f7 = MathHelper.cos(f5);
			this.dragonPartBody.onUpdate();
			this.dragonPartBody.setLocationAndAngles(this.posX + (double)(f28 * 0.5F), this.posY, this.posZ - (double)(f7 * 0.5F), 0.0F, 0.0F);
			this.dragonPartWing1.onUpdate();
			this.dragonPartWing1.setLocationAndAngles(this.posX + (double)(f7 * 4.5F), this.posY + 2.0D, this.posZ + (double)(f28 * 4.5F), 0.0F, 0.0F);
			this.dragonPartWing2.onUpdate();
			this.dragonPartWing2.setLocationAndAngles(this.posX - (double)(f7 * 4.5F), this.posY + 2.0D, this.posZ - (double)(f28 * 4.5F), 0.0F, 0.0F);
			if(!this.worldObj.isRemote) {
				this.func_41033_v();
			}

			if(!this.worldObj.isRemote && this.maxHurtTime == 0) {
				this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.boundingBox.expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
				this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.boundingBox.expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
				this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.boundingBox.expand(1.0D, 1.0D, 1.0D)));
			}

			double[] d29 = this.func_40139_a(5, 1.0F);
			double[] d9 = this.func_40139_a(0, 1.0F);
			f31 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
			float f33 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
			this.dragonPartHead.onUpdate();
			this.dragonPartHead.setLocationAndAngles(this.posX + (double)(f31 * 5.5F * f3), this.posY + (d9[1] - d29[1]) * 1.0D + (double)(f27 * 5.5F), this.posZ - (double)(f33 * 5.5F * f3), 0.0F, 0.0F);

			for(int i30 = 0; i30 < 3; ++i30) {
				EntityDragonPart entityDragonPart32 = null;
				if(i30 == 0) {
					entityDragonPart32 = this.dragonPartTail1;
				}

				if(i30 == 1) {
					entityDragonPart32 = this.dragonPartTail2;
				}

				if(i30 == 2) {
					entityDragonPart32 = this.dragonPartTail3;
				}

				double[] d34 = this.func_40139_a(12 + i30 * 2, 1.0F);
				float f35 = this.rotationYaw * (float)Math.PI / 180.0F + this.simplifyAngle(d34[0] - d29[0]) * (float)Math.PI / 180.0F * 1.0F;
				float f37 = MathHelper.sin(f35);
				float f36 = MathHelper.cos(f35);
				float f38 = 1.5F;
				float f40 = (float)(i30 + 1) * 2.0F;
				entityDragonPart32.onUpdate();
				entityDragonPart32.setLocationAndAngles(this.posX - (double)((f28 * f38 + f37 * f40) * f3), this.posY + (d34[1] - d29[1]) * 1.0D - (double)((f40 + f38) * f27) + 1.5D, this.posZ + (double)((f7 * f38 + f36 * f40) * f3), 0.0F, 0.0F);
			}

			if(!this.worldObj.isRemote) {
				this.field_40159_q = this.destroyBlocksInAABB(this.dragonPartHead.boundingBox) | this.destroyBlocksInAABB(this.dragonPartBody.boundingBox);
			}

		}
	}

	private void updateDragonEnderCrystal() {
		if(this.healingEnderCrystal != null) {
			if(this.healingEnderCrystal.isDead) {
				if(!this.worldObj.isRemote) {
					this.attackEntityFromPart(this.dragonPartHead, DamageSource.explosion, 10);
				}

				this.healingEnderCrystal = null;
			} else if(this.ticksExisted % 10 == 0 && this.health < this.maxHealth) {
				++this.health;
			}
		}

		if(this.rand.nextInt(10) == 0) {
			float f1 = 32.0F;
			List list2 = this.worldObj.getEntitiesWithinAABB(EntityEnderCrystal.class, this.boundingBox.expand((double)f1, (double)f1, (double)f1));
			EntityEnderCrystal entityEnderCrystal3 = null;
			double d4 = Double.MAX_VALUE;
			Iterator iterator6 = list2.iterator();

			while(iterator6.hasNext()) {
				Entity entity7 = (Entity)iterator6.next();
				double d8 = entity7.getDistanceSqToEntity(this);
				if(d8 < d4) {
					d4 = d8;
					entityEnderCrystal3 = (EntityEnderCrystal)entity7;
				}
			}

			this.healingEnderCrystal = entityEnderCrystal3;
		}

	}

	private void func_41033_v() {
	}

	private void collideWithEntities(List list1) {
		double d2 = (this.dragonPartBody.boundingBox.minX + this.dragonPartBody.boundingBox.maxX) / 2.0D;
		double d4 = (this.dragonPartBody.boundingBox.minZ + this.dragonPartBody.boundingBox.maxZ) / 2.0D;
		Iterator iterator6 = list1.iterator();

		while(iterator6.hasNext()) {
			Entity entity7 = (Entity)iterator6.next();
			if(entity7 instanceof EntityLiving) {
				double d8 = entity7.posX - d2;
				double d10 = entity7.posZ - d4;
				double d12 = d8 * d8 + d10 * d10;
				entity7.addVelocity(d8 / d12 * 4.0D, (double)0.2F, d10 / d12 * 4.0D);
			}
		}

	}

	private void attackEntitiesInList(List list1) {
		for(int i2 = 0; i2 < list1.size(); ++i2) {
			Entity entity3 = (Entity)list1.get(i2);
			if(entity3 instanceof EntityLiving) {
				entity3.attackEntityFrom(DamageSource.causeMobDamage(this), 10);
			}
		}

	}

	private void func_41037_w() {
		this.field_40160_p = false;
		if(this.rand.nextInt(2) == 0 && this.worldObj.playerEntities.size() > 0) {
			this.target = (Entity)this.worldObj.playerEntities.get(this.rand.nextInt(this.worldObj.playerEntities.size()));
		} else {
			boolean z1 = false;

			do {
				this.targetX = 0.0D;
				this.targetY = (double)(70.0F + this.rand.nextFloat() * 50.0F);
				this.targetZ = 0.0D;
				this.targetX += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
				this.targetZ += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
				double d2 = this.posX - this.targetX;
				double d4 = this.posY - this.targetY;
				double d6 = this.posZ - this.targetZ;
				z1 = d2 * d2 + d4 * d4 + d6 * d6 > 100.0D;
			} while(!z1);

			this.target = null;
		}

	}

	private float simplifyAngle(double d1) {
		while(d1 >= 180.0D) {
			d1 -= 360.0D;
		}

		while(d1 < -180.0D) {
			d1 += 360.0D;
		}

		return (float)d1;
	}

	private boolean destroyBlocksInAABB(AxisAlignedBB axisAlignedBB1) {
		int i2 = MathHelper.floor_double(axisAlignedBB1.minX);
		int i3 = MathHelper.floor_double(axisAlignedBB1.minY);
		int i4 = MathHelper.floor_double(axisAlignedBB1.minZ);
		int i5 = MathHelper.floor_double(axisAlignedBB1.maxX);
		int i6 = MathHelper.floor_double(axisAlignedBB1.maxY);
		int i7 = MathHelper.floor_double(axisAlignedBB1.maxZ);
		boolean z8 = false;
		boolean z9 = false;

		for(int i10 = i2; i10 <= i5; ++i10) {
			for(int i11 = i3; i11 <= i6; ++i11) {
				for(int i12 = i4; i12 <= i7; ++i12) {
					int i13 = this.worldObj.getBlockId(i10, i11, i12);
					if(i13 != 0) {
						if(i13 != Block.obsidian.blockID && i13 != Block.whiteStone.blockID && i13 != Block.bedrock.blockID) {
							z9 = true;
							this.worldObj.setBlockWithNotify(i10, i11, i12, 0);
						} else {
							z8 = true;
						}
					}
				}
			}
		}

		if(z9) {
			double d16 = axisAlignedBB1.minX + (axisAlignedBB1.maxX - axisAlignedBB1.minX) * (double)this.rand.nextFloat();
			double d17 = axisAlignedBB1.minY + (axisAlignedBB1.maxY - axisAlignedBB1.minY) * (double)this.rand.nextFloat();
			double d14 = axisAlignedBB1.minZ + (axisAlignedBB1.maxZ - axisAlignedBB1.minZ) * (double)this.rand.nextFloat();
			this.worldObj.spawnParticle("largeexplode", d16, d17, d14, 0.0D, 0.0D, 0.0D);
		}

		return z8;
	}

	public boolean attackEntityFromPart(EntityDragonPart entityDragonPart1, DamageSource damageSource2, int i3) {
		if(entityDragonPart1 != this.dragonPartHead) {
			i3 = i3 / 4 + 1;
		}

		float f4 = this.rotationYaw * (float)Math.PI / 180.0F;
		float f5 = MathHelper.sin(f4);
		float f6 = MathHelper.cos(f4);
		this.targetX = this.posX + (double)(f5 * 5.0F) + (double)((this.rand.nextFloat() - 0.5F) * 2.0F);
		this.targetY = this.posY + (double)(this.rand.nextFloat() * 3.0F) + 1.0D;
		this.targetZ = this.posZ - (double)(f6 * 5.0F) + (double)((this.rand.nextFloat() - 0.5F) * 2.0F);
		this.target = null;
		if(damageSource2.getEntity() instanceof EntityPlayer || damageSource2 == DamageSource.explosion) {
			this.superAttackFrom(damageSource2, i3);
		}

		return true;
	}

	protected void onDeathUpdate() {
		++this.field_40158_r;
		if(this.field_40158_r >= 180 && this.field_40158_r <= 200) {
			float f1 = (this.rand.nextFloat() - 0.5F) * 8.0F;
			float f2 = (this.rand.nextFloat() - 0.5F) * 4.0F;
			float f3 = (this.rand.nextFloat() - 0.5F) * 8.0F;
			this.worldObj.spawnParticle("hugeexplosion", this.posX + (double)f1, this.posY + 2.0D + (double)f2, this.posZ + (double)f3, 0.0D, 0.0D, 0.0D);
		}

		int i4;
		int i5;
		if(!this.worldObj.isRemote && this.field_40158_r > 150 && this.field_40158_r % 5 == 0) {
			i4 = 1000;

			while(i4 > 0) {
				i5 = EntityXPOrb.getXPSplit(i4);
				i4 -= i5;
				this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, i5));
			}
		}

		this.moveEntity(0.0D, (double)0.1F, 0.0D);
		this.renderYawOffset = this.rotationYaw += 20.0F;
		if(this.field_40158_r == 200) {
			i4 = 10000;

			while(i4 > 0) {
				i5 = EntityXPOrb.getXPSplit(i4);
				i4 -= i5;
				this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, i5));
			}

			this.createEnderPortal(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
			this.onEntityDeath();
			this.setDead();
		}

	}

	private void createEnderPortal(int i1, int i2) {
		byte b3 = 64;
		BlockEndPortal.bossDefeated = true;
		byte b4 = 4;

		for(int i5 = b3 - 1; i5 <= b3 + 32; ++i5) {
			for(int i6 = i1 - b4; i6 <= i1 + b4; ++i6) {
				for(int i7 = i2 - b4; i7 <= i2 + b4; ++i7) {
					double d8 = (double)(i6 - i1);
					double d10 = (double)(i7 - i2);
					double d12 = (double)MathHelper.sqrt_double(d8 * d8 + d10 * d10);
					if(d12 <= (double)b4 - 0.5D) {
						if(i5 < b3) {
							if(d12 <= (double)(b4 - 1) - 0.5D) {
								this.worldObj.setBlockWithNotify(i6, i5, i7, Block.bedrock.blockID);
							}
						} else if(i5 > b3) {
							this.worldObj.setBlockWithNotify(i6, i5, i7, 0);
						} else if(d12 > (double)(b4 - 1) - 0.5D) {
							this.worldObj.setBlockWithNotify(i6, i5, i7, Block.bedrock.blockID);
						} else {
							this.worldObj.setBlockWithNotify(i6, i5, i7, Block.endPortal.blockID);
						}
					}
				}
			}
		}

		this.worldObj.setBlockWithNotify(i1, b3 + 0, i2, Block.bedrock.blockID);
		this.worldObj.setBlockWithNotify(i1, b3 + 1, i2, Block.bedrock.blockID);
		this.worldObj.setBlockWithNotify(i1, b3 + 2, i2, Block.bedrock.blockID);
		this.worldObj.setBlockWithNotify(i1 - 1, b3 + 2, i2, Block.torchWood.blockID);
		this.worldObj.setBlockWithNotify(i1 + 1, b3 + 2, i2, Block.torchWood.blockID);
		this.worldObj.setBlockWithNotify(i1, b3 + 2, i2 - 1, Block.torchWood.blockID);
		this.worldObj.setBlockWithNotify(i1, b3 + 2, i2 + 1, Block.torchWood.blockID);
		this.worldObj.setBlockWithNotify(i1, b3 + 3, i2, Block.bedrock.blockID);
		this.worldObj.setBlockWithNotify(i1, b3 + 4, i2, Block.dragonEgg.blockID);
		BlockEndPortal.bossDefeated = false;
	}

	protected void despawnEntity() {
	}

	public Entity[] getParts() {
		return this.dragonPartArray;
	}

	public boolean canBeCollidedWith() {
		return false;
	}
}
