package net.minecraft.src;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public abstract class EntityLiving extends Entity {
	public int heartsHalvesLife = 20;
	public float field_9098_aw;
	public float field_9096_ay;
	public float renderYawOffset = 0.0F;
	public float prevRenderYawOffset = 0.0F;
	public float rotationYawHead = 0.0F;
	public float prevRotationYawHead = 0.0F;
	protected float field_9124_aB;
	protected float field_9123_aC;
	protected float field_9122_aD;
	protected float field_9121_aE;
	protected boolean field_9120_aF = true;
	protected String texture = "/mob/char.png";
	protected boolean field_9118_aH = true;
	protected float field_9117_aI = 0.0F;
	protected String entityType = null;
	protected float field_9115_aK = 1.0F;
	protected int scoreValue = 0;
	protected float field_9113_aM = 0.0F;
	public float landMovementFactor = 0.1F;
	public float jumpMovementFactor = 0.02F;
	public float prevSwingProgress;
	public float swingProgress;
	protected int health = this.getMaxHealth();
	public int prevHealth;
	protected int carryoverDamage;
	private int livingSoundTime;
	public int hurtTime;
	public int maxHurtTime;
	public float attackedAtYaw = 0.0F;
	public int deathTime = 0;
	public int attackTime = 0;
	public float prevCameraPitch;
	public float cameraPitch;
	protected boolean dead = false;
	protected int experienceValue;
	public int field_9144_ba = -1;
	public float field_9143_bb = (float)(Math.random() * (double)0.9F + (double)0.1F);
	public float field_9142_bc;
	public float field_9141_bd;
	public float field_386_ba;
	protected EntityPlayer attackingPlayer = null;
	protected int recentlyHit = 0;
	private EntityLiving entityLivingToAttack = null;
	private int revengeTimer = 0;
	private EntityLiving lastAttackingEntity = null;
	public int arrowHitTempCounter = 0;
	public int arrowHitTimer = 0;
	protected HashMap activePotionsMap = new HashMap();
	private boolean potionsNeedUpdate = true;
	private int field_39003_c;
	private EntityLookHelper lookHelper;
	private EntityMoveHelper moveHelper;
	private EntityJumpHelper jumpHelper;
	private EntityBodyHelper bodyHelper;
	private PathNavigate navigator;
	protected EntityAITasks tasks = new EntityAITasks();
	protected EntityAITasks targetTasks = new EntityAITasks();
	private EntityLiving attackTarget;
	private EntitySenses field_48343_m;
	private float field_48340_n;
	private ChunkCoordinates homePosition = new ChunkCoordinates(0, 0, 0);
	private float maximumHomeDistance = -1.0F;
	protected int newPosRotationIncrements;
	protected double newPosX;
	protected double newPosY;
	protected double newPosZ;
	protected double newRotationYaw;
	protected double newRotationPitch;
	float field_9134_bl = 0.0F;
	protected int naturalArmorRating = 0;
	protected int entityAge = 0;
	protected float moveStrafing;
	protected float moveForward;
	protected float randomYawVelocity;
	protected boolean isJumping = false;
	protected float defaultPitch = 0.0F;
	protected float moveSpeed = 0.7F;
	private int jumpTicks = 0;
	private Entity currentTarget;
	protected int numTicksToChaseTarget = 0;

	public EntityLiving(World world1) {
		super(world1);
		this.preventEntitySpawning = true;
		this.lookHelper = new EntityLookHelper(this);
		this.moveHelper = new EntityMoveHelper(this);
		this.jumpHelper = new EntityJumpHelper(this);
		this.bodyHelper = new EntityBodyHelper(this);
		this.navigator = new PathNavigate(this, world1, 16.0F);
		this.field_48343_m = new EntitySenses(this);
		this.field_9096_ay = (float)(Math.random() + 1.0D) * 0.01F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.field_9098_aw = (float)Math.random() * 12398.0F;
		this.rotationYaw = (float)(Math.random() * (double)(float)Math.PI * 2.0D);
		this.rotationYawHead = this.rotationYaw;
		this.stepHeight = 0.5F;
	}

	public EntityLookHelper getLookHelper() {
		return this.lookHelper;
	}

	public EntityMoveHelper getMoveHelper() {
		return this.moveHelper;
	}

	public EntityJumpHelper getJumpHelper() {
		return this.jumpHelper;
	}

	public PathNavigate getNavigator() {
		return this.navigator;
	}

	public EntitySenses func_48318_al() {
		return this.field_48343_m;
	}

	public Random getRNG() {
		return this.rand;
	}

	public EntityLiving getAITarget() {
		return this.entityLivingToAttack;
	}

	public EntityLiving getLastAttackingEntity() {
		return this.lastAttackingEntity;
	}

	public void setLastAttackingEntity(Entity entity1) {
		if(entity1 instanceof EntityLiving) {
			this.lastAttackingEntity = (EntityLiving)entity1;
		}

	}

	public int getAge() {
		return this.entityAge;
	}

	public float func_48314_aq() {
		return this.rotationYawHead;
	}

	public float func_48332_ar() {
		return this.field_48340_n;
	}

	public void func_48320_d(float f1) {
		this.field_48340_n = f1;
		this.setMoveForward(f1);
	}

	public boolean attackEntityAsMob(Entity entity1) {
		this.setLastAttackingEntity(entity1);
		return false;
	}

	public EntityLiving getAttackTarget() {
		return this.attackTarget;
	}

	public void setAttackTarget(EntityLiving entityLiving1) {
		this.attackTarget = entityLiving1;
	}

	public boolean func_48336_a(Class class1) {
		return EntityCreeper.class != class1 && EntityGhast.class != class1;
	}

	public void eatGrassBonus() {
	}

	public boolean isWithinHomeDistanceCurrentPosition() {
		return this.isWithinHomeDistance(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
	}

	public boolean isWithinHomeDistance(int i1, int i2, int i3) {
		return this.maximumHomeDistance == -1.0F ? true : this.homePosition.getDistanceSquared(i1, i2, i3) < this.maximumHomeDistance * this.maximumHomeDistance;
	}

	public void setHomeArea(int i1, int i2, int i3, int i4) {
		this.homePosition.set(i1, i2, i3);
		this.maximumHomeDistance = (float)i4;
	}

	public ChunkCoordinates getHomePosition() {
		return this.homePosition;
	}

	public float getMaximumHomeDistance() {
		return this.maximumHomeDistance;
	}

	public void detachHome() {
		this.maximumHomeDistance = -1.0F;
	}

	public boolean hasHome() {
		return this.maximumHomeDistance != -1.0F;
	}

	public void setRevengeTarget(EntityLiving entityLiving1) {
		this.entityLivingToAttack = entityLiving1;
		this.revengeTimer = this.entityLivingToAttack != null ? 60 : 0;
	}

	protected void entityInit() {
		this.dataWatcher.addObject(8, this.field_39003_c);
	}

	public boolean canEntityBeSeen(Entity entity1) {
		return this.worldObj.rayTraceBlocks(Vec3D.createVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), Vec3D.createVector(entity1.posX, entity1.posY + (double)entity1.getEyeHeight(), entity1.posZ)) == null;
	}

	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public boolean canBePushed() {
		return !this.isDead;
	}

	public float getEyeHeight() {
		return this.height * 0.85F;
	}

	public int getTalkInterval() {
		return 80;
	}

	public void playLivingSound() {
		String string1 = this.getLivingSound();
		if(string1 != null) {
			this.worldObj.playSoundAtEntity(this, string1, this.getSoundVolume(), this.getSoundPitch());
		}

	}

	public void onEntityUpdate() {
		this.prevSwingProgress = this.swingProgress;
		super.onEntityUpdate();
		Profiler.startSection("mobBaseTick");
		if(this.isEntityAlive() && this.rand.nextInt(1000) < this.livingSoundTime++) {
			this.livingSoundTime = -this.getTalkInterval();
			this.playLivingSound();
		}

		if(this.isEntityAlive() && this.isEntityInsideOpaqueBlock() && this.attackEntityFrom(DamageSource.inWall, 1)) {
			;
		}

		if(this.isImmuneToFire() || this.worldObj.isRemote) {
			this.extinguish();
		}

		if(this.isEntityAlive() && this.isInsideOfMaterial(Material.water) && !this.canBreatheUnderwater() && !this.activePotionsMap.containsKey(Potion.waterBreathing.id)) {
			this.setAir(this.decreaseAirSupply(this.getAir()));
			if(this.getAir() == -20) {
				this.setAir(0);

				for(int i1 = 0; i1 < 8; ++i1) {
					float f2 = this.rand.nextFloat() - this.rand.nextFloat();
					float f3 = this.rand.nextFloat() - this.rand.nextFloat();
					float f4 = this.rand.nextFloat() - this.rand.nextFloat();
					this.worldObj.spawnParticle("bubble", this.posX + (double)f2, this.posY + (double)f3, this.posZ + (double)f4, this.motionX, this.motionY, this.motionZ);
				}

				this.attackEntityFrom(DamageSource.drown, 2);
			}

			this.extinguish();
		} else {
			this.setAir(300);
		}

		this.prevCameraPitch = this.cameraPitch;
		if(this.attackTime > 0) {
			--this.attackTime;
		}

		if(this.hurtTime > 0) {
			--this.hurtTime;
		}

		if(this.heartsLife > 0) {
			--this.heartsLife;
		}

		if(this.health <= 0) {
			this.onDeathUpdate();
		}

		if(this.recentlyHit > 0) {
			--this.recentlyHit;
		} else {
			this.attackingPlayer = null;
		}

		if(this.lastAttackingEntity != null && !this.lastAttackingEntity.isEntityAlive()) {
			this.lastAttackingEntity = null;
		}

		if(this.entityLivingToAttack != null) {
			if(!this.entityLivingToAttack.isEntityAlive()) {
				this.setRevengeTarget((EntityLiving)null);
			} else if(this.revengeTimer > 0) {
				--this.revengeTimer;
			} else {
				this.setRevengeTarget((EntityLiving)null);
			}
		}

		this.updatePotionEffects();
		this.field_9121_aE = this.field_9122_aD;
		this.prevRenderYawOffset = this.renderYawOffset;
		this.prevRotationYawHead = this.rotationYawHead;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		Profiler.endSection();
	}

	protected void onDeathUpdate() {
		++this.deathTime;
		if(this.deathTime == 20) {
			int i1;
			if(!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && !this.isChild()) {
				i1 = this.getExperiencePoints(this.attackingPlayer);

				while(i1 > 0) {
					int i2 = EntityXPOrb.getXPSplit(i1);
					i1 -= i2;
					this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, i2));
				}
			}

			this.onEntityDeath();
			this.setDead();

			for(i1 = 0; i1 < 20; ++i1) {
				double d8 = this.rand.nextGaussian() * 0.02D;
				double d4 = this.rand.nextGaussian() * 0.02D;
				double d6 = this.rand.nextGaussian() * 0.02D;
				this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d8, d4, d6);
			}
		}

	}

	protected int decreaseAirSupply(int i1) {
		return i1 - 1;
	}

	protected int getExperiencePoints(EntityPlayer entityPlayer1) {
		return this.experienceValue;
	}

	protected boolean isPlayer() {
		return false;
	}

	public void spawnExplosionParticle() {
		for(int i1 = 0; i1 < 20; ++i1) {
			double d2 = this.rand.nextGaussian() * 0.02D;
			double d4 = this.rand.nextGaussian() * 0.02D;
			double d6 = this.rand.nextGaussian() * 0.02D;
			double d8 = 10.0D;
			this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d2 * d8, this.posY + (double)(this.rand.nextFloat() * this.height) - d4 * d8, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d6 * d8, d2, d4, d6);
		}

	}

	public void updateRidden() {
		super.updateRidden();
		this.field_9124_aB = this.field_9123_aC;
		this.field_9123_aC = 0.0F;
		this.fallDistance = 0.0F;
	}

	public void onUpdate() {
		super.onUpdate();
		if(this.arrowHitTempCounter > 0) {
			if(this.arrowHitTimer <= 0) {
				this.arrowHitTimer = 60;
			}

			--this.arrowHitTimer;
			if(this.arrowHitTimer <= 0) {
				--this.arrowHitTempCounter;
			}
		}

		this.onLivingUpdate();
		double d1 = this.posX - this.prevPosX;
		double d3 = this.posZ - this.prevPosZ;
		float f5 = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
		float f6 = this.renderYawOffset;
		float f7 = 0.0F;
		this.field_9124_aB = this.field_9123_aC;
		float f8 = 0.0F;
		if(f5 > 0.05F) {
			f8 = 1.0F;
			f7 = f5 * 3.0F;
			f6 = (float)Math.atan2(d3, d1) * 180.0F / (float)Math.PI - 90.0F;
		}

		if(this.swingProgress > 0.0F) {
			f6 = this.rotationYaw;
		}

		if(!this.onGround) {
			f8 = 0.0F;
		}

		this.field_9123_aC += (f8 - this.field_9123_aC) * 0.3F;
		if(this.isAIEnabled()) {
			this.bodyHelper.func_48431_a();
		} else {
			float f9;
			for(f9 = f6 - this.renderYawOffset; f9 < -180.0F; f9 += 360.0F) {
			}

			while(f9 >= 180.0F) {
				f9 -= 360.0F;
			}

			this.renderYawOffset += f9 * 0.3F;

			float f10;
			for(f10 = this.rotationYaw - this.renderYawOffset; f10 < -180.0F; f10 += 360.0F) {
			}

			while(f10 >= 180.0F) {
				f10 -= 360.0F;
			}

			boolean z11 = f10 < -90.0F || f10 >= 90.0F;
			if(f10 < -75.0F) {
				f10 = -75.0F;
			}

			if(f10 >= 75.0F) {
				f10 = 75.0F;
			}

			this.renderYawOffset = this.rotationYaw - f10;
			if(f10 * f10 > 2500.0F) {
				this.renderYawOffset += f10 * 0.2F;
			}

			if(z11) {
				f7 *= -1.0F;
			}
		}

		while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
			this.prevRenderYawOffset -= 360.0F;
		}

		while(this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
			this.prevRenderYawOffset += 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch < -180.0F) {
			this.prevRotationPitch -= 360.0F;
		}

		while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while(this.rotationYawHead - this.prevRotationYawHead < -180.0F) {
			this.prevRotationYawHead -= 360.0F;
		}

		while(this.rotationYawHead - this.prevRotationYawHead >= 180.0F) {
			this.prevRotationYawHead += 360.0F;
		}

		this.field_9122_aD += f7;
	}

	protected void setSize(float f1, float f2) {
		super.setSize(f1, f2);
	}

	public void heal(int i1) {
		if(this.health > 0) {
			this.health += i1;
			if(this.health > this.getMaxHealth()) {
				this.health = this.getMaxHealth();
			}

			this.heartsLife = this.heartsHalvesLife / 2;
		}
	}

	public abstract int getMaxHealth();

	public int getHealth() {
		return this.health;
	}

	public void setEntityHealth(int i1) {
		this.health = i1;
		if(i1 > this.getMaxHealth()) {
			i1 = this.getMaxHealth();
		}

	}

	public boolean attackEntityFrom(DamageSource damageSource1, int i2) {
		if(this.worldObj.isRemote) {
			return false;
		} else {
			this.entityAge = 0;
			if(this.health <= 0) {
				return false;
			} else if(damageSource1.fireDamage() && this.isPotionActive(Potion.fireResistance)) {
				return false;
			} else {
				this.field_9141_bd = 1.5F;
				boolean z3 = true;
				if((float)this.heartsLife > (float)this.heartsHalvesLife / 2.0F) {
					if(i2 <= this.naturalArmorRating) {
						return false;
					}

					this.damageEntity(damageSource1, i2 - this.naturalArmorRating);
					this.naturalArmorRating = i2;
					z3 = false;
				} else {
					this.naturalArmorRating = i2;
					this.prevHealth = this.health;
					this.heartsLife = this.heartsHalvesLife;
					this.damageEntity(damageSource1, i2);
					this.hurtTime = this.maxHurtTime = 10;
				}

				this.attackedAtYaw = 0.0F;
				Entity entity4 = damageSource1.getEntity();
				if(entity4 != null) {
					if(entity4 instanceof EntityLiving) {
						this.setRevengeTarget((EntityLiving)entity4);
					}

					if(entity4 instanceof EntityPlayer) {
						this.recentlyHit = 60;
						this.attackingPlayer = (EntityPlayer)entity4;
					} else if(entity4 instanceof EntityWolf) {
						EntityWolf entityWolf5 = (EntityWolf)entity4;
						if(entityWolf5.isTamed()) {
							this.recentlyHit = 60;
							this.attackingPlayer = null;
						}
					}
				}

				if(z3) {
					this.worldObj.setEntityState(this, (byte)2);
					this.setBeenAttacked();
					if(entity4 != null) {
						double d9 = entity4.posX - this.posX;

						double d7;
						for(d7 = entity4.posZ - this.posZ; d9 * d9 + d7 * d7 < 1.0E-4D; d7 = (Math.random() - Math.random()) * 0.01D) {
							d9 = (Math.random() - Math.random()) * 0.01D;
						}

						this.attackedAtYaw = (float)(Math.atan2(d7, d9) * 180.0D / (double)(float)Math.PI) - this.rotationYaw;
						this.knockBack(entity4, i2, d9, d7);
					} else {
						this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
					}
				}

				if(this.health <= 0) {
					if(z3) {
						this.worldObj.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), this.getSoundPitch());
					}

					this.onDeath(damageSource1);
				} else if(z3) {
					this.worldObj.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), this.getSoundPitch());
				}

				return true;
			}
		}
	}

	private float getSoundPitch() {
		return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
	}

	public int getTotalArmorValue() {
		return 0;
	}

	protected void damageArmor(int i1) {
	}

	protected int applyArmorCalculations(DamageSource damageSource1, int i2) {
		if(!damageSource1.isUnblockable()) {
			int i3 = 25 - this.getTotalArmorValue();
			int i4 = i2 * i3 + this.carryoverDamage;
			this.damageArmor(i2);
			i2 = i4 / 25;
			this.carryoverDamage = i4 % 25;
		}

		return i2;
	}

	protected int applyPotionDamageCalculations(DamageSource damageSource1, int i2) {
		if(this.isPotionActive(Potion.resistance)) {
			int i3 = (this.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
			int i4 = 25 - i3;
			int i5 = i2 * i4 + this.carryoverDamage;
			i2 = i5 / 25;
			this.carryoverDamage = i5 % 25;
		}

		return i2;
	}

	protected void damageEntity(DamageSource damageSource1, int i2) {
		i2 = this.applyArmorCalculations(damageSource1, i2);
		i2 = this.applyPotionDamageCalculations(damageSource1, i2);
		this.health -= i2;
	}

	protected float getSoundVolume() {
		return 1.0F;
	}

	protected String getLivingSound() {
		return null;
	}

	protected String getHurtSound() {
		return "damage.hurtflesh";
	}

	protected String getDeathSound() {
		return "damage.hurtflesh";
	}

	public void knockBack(Entity entity1, int i2, double d3, double d5) {
		this.isAirBorne = true;
		float f7 = MathHelper.sqrt_double(d3 * d3 + d5 * d5);
		float f8 = 0.4F;
		this.motionX /= 2.0D;
		this.motionY /= 2.0D;
		this.motionZ /= 2.0D;
		this.motionX -= d3 / (double)f7 * (double)f8;
		this.motionY += (double)f8;
		this.motionZ -= d5 / (double)f7 * (double)f8;
		if(this.motionY > (double)0.4F) {
			this.motionY = (double)0.4F;
		}

	}

	public void onDeath(DamageSource damageSource1) {
		Entity entity2 = damageSource1.getEntity();
		if(this.scoreValue >= 0 && entity2 != null) {
			entity2.addToPlayerScore(this, this.scoreValue);
		}

		if(entity2 != null) {
			entity2.onKillEntity(this);
		}

		this.dead = true;
		if(!this.worldObj.isRemote) {
			int i3 = 0;
			if(entity2 instanceof EntityPlayer) {
				i3 = EnchantmentHelper.getLootingModifier(((EntityPlayer)entity2).inventory);
			}

			if(!this.isChild()) {
				this.dropFewItems(this.recentlyHit > 0, i3);
				if(this.recentlyHit > 0) {
					int i4 = this.rand.nextInt(200) - i3;
					if(i4 < 5) {
						this.dropRareDrop(i4 <= 0 ? 1 : 0);
					}
				}
			}
		}

		this.worldObj.setEntityState(this, (byte)3);
	}

	protected void dropRareDrop(int i1) {
	}

	protected void dropFewItems(boolean z1, int i2) {
		int i3 = this.getDropItemId();
		if(i3 > 0) {
			int i4 = this.rand.nextInt(3);
			if(i2 > 0) {
				i4 += this.rand.nextInt(i2 + 1);
			}

			for(int i5 = 0; i5 < i4; ++i5) {
				this.dropItem(i3, 1);
			}
		}

	}

	protected int getDropItemId() {
		return 0;
	}

	protected void fall(float f1) {
		super.fall(f1);
		int i2 = (int)Math.ceil((double)(f1 - 3.0F));
		if(i2 > 0) {
			if(i2 > 4) {
				this.worldObj.playSoundAtEntity(this, "damage.fallbig", 1.0F, 1.0F);
			} else {
				this.worldObj.playSoundAtEntity(this, "damage.fallsmall", 1.0F, 1.0F);
			}

			this.attackEntityFrom(DamageSource.fall, i2);
			int i3 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset), MathHelper.floor_double(this.posZ));
			if(i3 > 0) {
				StepSound stepSound4 = Block.blocksList[i3].stepSound;
				this.worldObj.playSoundAtEntity(this, stepSound4.getStepSound(), stepSound4.getVolume() * 0.5F, stepSound4.getPitch() * 0.75F);
			}
		}

	}

	public void moveEntityWithHeading(float f1, float f2) {
		double d3;
		if(this.isInWater()) {
			d3 = this.posY;
			this.moveFlying(f1, f2, this.isAIEnabled() ? 0.04F : 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= (double)0.8F;
			this.motionY *= (double)0.8F;
			this.motionZ *= (double)0.8F;
			this.motionY -= 0.02D;
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6F - this.posY + d3, this.motionZ)) {
				this.motionY = (double)0.3F;
			}
		} else if(this.handleLavaMovement()) {
			d3 = this.posY;
			this.moveFlying(f1, f2, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
			this.motionY -= 0.02D;
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6F - this.posY + d3, this.motionZ)) {
				this.motionY = (double)0.3F;
			}
		} else {
			float f8 = 0.91F;
			if(this.onGround) {
				f8 = 0.54600006F;
				int i4 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
				if(i4 > 0) {
					f8 = Block.blocksList[i4].slipperiness * 0.91F;
				}
			}

			float f9 = 0.16277136F / (f8 * f8 * f8);
			float f5;
			if(this.onGround) {
				if(this.isAIEnabled()) {
					f5 = this.func_48332_ar();
				} else {
					f5 = this.landMovementFactor;
				}

				f5 *= f9;
			} else {
				f5 = this.jumpMovementFactor;
			}

			this.moveFlying(f1, f2, f5);
			f8 = 0.91F;
			if(this.onGround) {
				f8 = 0.54600006F;
				int i6 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
				if(i6 > 0) {
					f8 = Block.blocksList[i6].slipperiness * 0.91F;
				}
			}

			if(this.isOnLadder()) {
				float f10 = 0.15F;
				if(this.motionX < (double)(-f10)) {
					this.motionX = (double)(-f10);
				}

				if(this.motionX > (double)f10) {
					this.motionX = (double)f10;
				}

				if(this.motionZ < (double)(-f10)) {
					this.motionZ = (double)(-f10);
				}

				if(this.motionZ > (double)f10) {
					this.motionZ = (double)f10;
				}

				this.fallDistance = 0.0F;
				if(this.motionY < -0.15D) {
					this.motionY = -0.15D;
				}

				boolean z7 = this.isSneaking() && this instanceof EntityPlayer;
				if(z7 && this.motionY < 0.0D) {
					this.motionY = 0.0D;
				}
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			if(this.isCollidedHorizontally && this.isOnLadder()) {
				this.motionY = 0.2D;
			}

			this.motionY -= 0.08D;
			this.motionY *= (double)0.98F;
			this.motionX *= (double)f8;
			this.motionZ *= (double)f8;
		}

		this.field_9142_bc = this.field_9141_bd;
		d3 = this.posX - this.prevPosX;
		double d11 = this.posZ - this.prevPosZ;
		float f12 = MathHelper.sqrt_double(d3 * d3 + d11 * d11) * 4.0F;
		if(f12 > 1.0F) {
			f12 = 1.0F;
		}

		this.field_9141_bd += (f12 - this.field_9141_bd) * 0.4F;
		this.field_386_ba += this.field_9141_bd;
	}

	public boolean isOnLadder() {
		int i1 = MathHelper.floor_double(this.posX);
		int i2 = MathHelper.floor_double(this.boundingBox.minY);
		int i3 = MathHelper.floor_double(this.posZ);
		int i4 = this.worldObj.getBlockId(i1, i2, i3);
		return i4 == Block.ladder.blockID || i4 == Block.vine.blockID;
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setShort("Health", (short)this.health);
		nBTTagCompound1.setShort("HurtTime", (short)this.hurtTime);
		nBTTagCompound1.setShort("DeathTime", (short)this.deathTime);
		nBTTagCompound1.setShort("AttackTime", (short)this.attackTime);
		if(!this.activePotionsMap.isEmpty()) {
			NBTTagList nBTTagList2 = new NBTTagList();
			Iterator iterator3 = this.activePotionsMap.values().iterator();

			while(iterator3.hasNext()) {
				PotionEffect potionEffect4 = (PotionEffect)iterator3.next();
				NBTTagCompound nBTTagCompound5 = new NBTTagCompound();
				nBTTagCompound5.setByte("Id", (byte)potionEffect4.getPotionID());
				nBTTagCompound5.setByte("Amplifier", (byte)potionEffect4.getAmplifier());
				nBTTagCompound5.setInteger("Duration", potionEffect4.getDuration());
				nBTTagList2.appendTag(nBTTagCompound5);
			}

			nBTTagCompound1.setTag("ActiveEffects", nBTTagList2);
		}

	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		if(this.health < -32768) {
			this.health = -32768;
		}

		this.health = nBTTagCompound1.getShort("Health");
		if(!nBTTagCompound1.hasKey("Health")) {
			this.health = this.getMaxHealth();
		}

		this.hurtTime = nBTTagCompound1.getShort("HurtTime");
		this.deathTime = nBTTagCompound1.getShort("DeathTime");
		this.attackTime = nBTTagCompound1.getShort("AttackTime");
		if(nBTTagCompound1.hasKey("ActiveEffects")) {
			NBTTagList nBTTagList2 = nBTTagCompound1.getTagList("ActiveEffects");

			for(int i3 = 0; i3 < nBTTagList2.tagCount(); ++i3) {
				NBTTagCompound nBTTagCompound4 = (NBTTagCompound)nBTTagList2.tagAt(i3);
				byte b5 = nBTTagCompound4.getByte("Id");
				byte b6 = nBTTagCompound4.getByte("Amplifier");
				int i7 = nBTTagCompound4.getInteger("Duration");
				this.activePotionsMap.put(Integer.valueOf(b5), new PotionEffect(b5, i7, b6));
			}
		}

	}

	public boolean isEntityAlive() {
		return !this.isDead && this.health > 0;
	}

	public boolean canBreatheUnderwater() {
		return false;
	}

	public void setMoveForward(float f1) {
		this.moveForward = f1;
	}

	public void setJumping(boolean z1) {
		this.isJumping = z1;
	}

	public void onLivingUpdate() {
		if(this.jumpTicks > 0) {
			--this.jumpTicks;
		}

		if(this.newPosRotationIncrements > 0) {
			double d1 = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
			double d3 = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
			double d5 = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;

			double d7;
			for(d7 = this.newRotationYaw - (double)this.rotationYaw; d7 < -180.0D; d7 += 360.0D) {
			}

			while(d7 >= 180.0D) {
				d7 -= 360.0D;
			}

			this.rotationYaw = (float)((double)this.rotationYaw + d7 / (double)this.newPosRotationIncrements);
			this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
			--this.newPosRotationIncrements;
			this.setPosition(d1, d3, d5);
			this.setRotation(this.rotationYaw, this.rotationPitch);
			List list9 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.contract(8.0D / 256D, 0.0D, 8.0D / 256D));
			if(list9.size() > 0) {
				double d10 = 0.0D;

				for(int i12 = 0; i12 < list9.size(); ++i12) {
					AxisAlignedBB axisAlignedBB13 = (AxisAlignedBB)list9.get(i12);
					if(axisAlignedBB13.maxY > d10) {
						d10 = axisAlignedBB13.maxY;
					}
				}

				d3 += d10 - this.boundingBox.minY;
				this.setPosition(d1, d3, d5);
			}
		}

		Profiler.startSection("ai");
		if(this.isMovementBlocked()) {
			this.isJumping = false;
			this.moveStrafing = 0.0F;
			this.moveForward = 0.0F;
			this.randomYawVelocity = 0.0F;
		} else if(this.isClientWorld()) {
			if(this.isAIEnabled()) {
				Profiler.startSection("newAi");
				this.updateAITasks();
				Profiler.endSection();
			} else {
				Profiler.startSection("oldAi");
				this.updateEntityActionState();
				Profiler.endSection();
				this.rotationYawHead = this.rotationYaw;
			}
		}

		Profiler.endSection();
		boolean z14 = this.isInWater();
		boolean z2 = this.handleLavaMovement();
		if(this.isJumping) {
			if(z14) {
				this.motionY += (double)0.04F;
			} else if(z2) {
				this.motionY += (double)0.04F;
			} else if(this.onGround && this.jumpTicks == 0) {
				this.jump();
				this.jumpTicks = 10;
			}
		} else {
			this.jumpTicks = 0;
		}

		this.moveStrafing *= 0.98F;
		this.moveForward *= 0.98F;
		this.randomYawVelocity *= 0.9F;
		float f15 = this.landMovementFactor;
		this.landMovementFactor *= this.getSpeedModifier();
		this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
		this.landMovementFactor = f15;
		Profiler.startSection("push");
		List list4 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)0.2F, 0.0D, (double)0.2F));
		if(list4 != null && list4.size() > 0) {
			for(int i16 = 0; i16 < list4.size(); ++i16) {
				Entity entity6 = (Entity)list4.get(i16);
				if(entity6.canBePushed()) {
					entity6.applyEntityCollision(this);
				}
			}
		}

		Profiler.endSection();
	}

	protected boolean isAIEnabled() {
		return false;
	}

	protected boolean isClientWorld() {
		return !this.worldObj.isRemote;
	}

	protected boolean isMovementBlocked() {
		return this.health <= 0;
	}

	public boolean isBlocking() {
		return false;
	}

	protected void jump() {
		this.motionY = (double)0.42F;
		if(this.isPotionActive(Potion.jump)) {
			this.motionY += (double)((float)(this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
		}

		if(this.isSprinting()) {
			float f1 = this.rotationYaw * 0.017453292F;
			this.motionX -= (double)(MathHelper.sin(f1) * 0.2F);
			this.motionZ += (double)(MathHelper.cos(f1) * 0.2F);
		}

		this.isAirBorne = true;
	}

	protected boolean canDespawn() {
		return true;
	}

	protected void despawnEntity() {
		EntityPlayer entityPlayer1 = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
		if(entityPlayer1 != null) {
			double d2 = entityPlayer1.posX - this.posX;
			double d4 = entityPlayer1.posY - this.posY;
			double d6 = entityPlayer1.posZ - this.posZ;
			double d8 = d2 * d2 + d4 * d4 + d6 * d6;
			if(this.canDespawn() && d8 > 16384.0D) {
				this.setDead();
			}

			if(this.entityAge > 600 && this.rand.nextInt(800) == 0 && d8 > 1024.0D && this.canDespawn()) {
				this.setDead();
			} else if(d8 < 1024.0D) {
				this.entityAge = 0;
			}
		}

	}

	protected void updateAITasks() {
		++this.entityAge;
		Profiler.startSection("checkDespawn");
		this.despawnEntity();
		Profiler.endSection();
		Profiler.startSection("sensing");
		this.field_48343_m.clearSensingCache();
		Profiler.endSection();
		Profiler.startSection("targetSelector");
		this.targetTasks.onUpdateTasks();
		Profiler.endSection();
		Profiler.startSection("goalSelector");
		this.tasks.onUpdateTasks();
		Profiler.endSection();
		Profiler.startSection("navigation");
		this.navigator.onUpdateNavigation();
		Profiler.endSection();
		Profiler.startSection("mob tick");
		this.updateAITick();
		Profiler.endSection();
		Profiler.startSection("controls");
		this.moveHelper.onUpdateMoveHelper();
		this.lookHelper.onUpdateLook();
		this.jumpHelper.doJump();
		Profiler.endSection();
	}

	protected void updateAITick() {
	}

	protected void updateEntityActionState() {
		++this.entityAge;
		this.despawnEntity();
		this.moveStrafing = 0.0F;
		this.moveForward = 0.0F;
		float f1 = 8.0F;
		if(this.rand.nextFloat() < 0.02F) {
			EntityPlayer entityPlayer2 = this.worldObj.getClosestPlayerToEntity(this, (double)f1);
			if(entityPlayer2 != null) {
				this.currentTarget = entityPlayer2;
				this.numTicksToChaseTarget = 10 + this.rand.nextInt(20);
			} else {
				this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
			}
		}

		if(this.currentTarget != null) {
			this.faceEntity(this.currentTarget, 10.0F, (float)this.getVerticalFaceSpeed());
			if(this.numTicksToChaseTarget-- <= 0 || this.currentTarget.isDead || this.currentTarget.getDistanceSqToEntity(this) > (double)(f1 * f1)) {
				this.currentTarget = null;
			}
		} else {
			if(this.rand.nextFloat() < 0.05F) {
				this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
			}

			this.rotationYaw += this.randomYawVelocity;
			this.rotationPitch = this.defaultPitch;
		}

		boolean z4 = this.isInWater();
		boolean z3 = this.handleLavaMovement();
		if(z4 || z3) {
			this.isJumping = this.rand.nextFloat() < 0.8F;
		}

	}

	public int getVerticalFaceSpeed() {
		return 40;
	}

	public void faceEntity(Entity entity1, float f2, float f3) {
		double d4 = entity1.posX - this.posX;
		double d8 = entity1.posZ - this.posZ;
		double d6;
		if(entity1 instanceof EntityLiving) {
			EntityLiving entityLiving10 = (EntityLiving)entity1;
			d6 = this.posY + (double)this.getEyeHeight() - (entityLiving10.posY + (double)entityLiving10.getEyeHeight());
		} else {
			d6 = (entity1.boundingBox.minY + entity1.boundingBox.maxY) / 2.0D - (this.posY + (double)this.getEyeHeight());
		}

		double d14 = (double)MathHelper.sqrt_double(d4 * d4 + d8 * d8);
		float f12 = (float)(Math.atan2(d8, d4) * 180.0D / (double)(float)Math.PI) - 90.0F;
		float f13 = (float)(-(Math.atan2(d6, d14) * 180.0D / (double)(float)Math.PI));
		this.rotationPitch = -this.updateRotation(this.rotationPitch, f13, f3);
		this.rotationYaw = this.updateRotation(this.rotationYaw, f12, f2);
	}

	private float updateRotation(float f1, float f2, float f3) {
		float f4;
		for(f4 = f2 - f1; f4 < -180.0F; f4 += 360.0F) {
		}

		while(f4 >= 180.0F) {
			f4 -= 360.0F;
		}

		if(f4 > f3) {
			f4 = f3;
		}

		if(f4 < -f3) {
			f4 = -f3;
		}

		return f1 + f4;
	}

	public void onEntityDeath() {
	}

	public boolean getCanSpawnHere() {
		return this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox);
	}

	protected void kill() {
		this.attackEntityFrom(DamageSource.outOfWorld, 4);
	}

	public Vec3D getLookVec() {
		return this.getLook(1.0F);
	}

	public Vec3D getLook(float f1) {
		float f2;
		float f3;
		float f4;
		float f5;
		if(f1 == 1.0F) {
			f2 = MathHelper.cos(-this.rotationYaw * 0.017453292F - (float)Math.PI);
			f3 = MathHelper.sin(-this.rotationYaw * 0.017453292F - (float)Math.PI);
			f4 = -MathHelper.cos(-this.rotationPitch * 0.017453292F);
			f5 = MathHelper.sin(-this.rotationPitch * 0.017453292F);
			return Vec3D.createVector((double)(f3 * f4), (double)f5, (double)(f2 * f4));
		} else {
			f2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * f1;
			f3 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * f1;
			f4 = MathHelper.cos(-f3 * 0.017453292F - (float)Math.PI);
			f5 = MathHelper.sin(-f3 * 0.017453292F - (float)Math.PI);
			float f6 = -MathHelper.cos(-f2 * 0.017453292F);
			float f7 = MathHelper.sin(-f2 * 0.017453292F);
			return Vec3D.createVector((double)(f5 * f6), (double)f7, (double)(f4 * f6));
		}
	}

	public int getMaxSpawnedInChunk() {
		return 4;
	}

	public boolean isPlayerSleeping() {
		return false;
	}

	protected void updatePotionEffects() {
		Iterator iterator1 = this.activePotionsMap.keySet().iterator();

		while(iterator1.hasNext()) {
			Integer integer2 = (Integer)iterator1.next();
			PotionEffect potionEffect3 = (PotionEffect)this.activePotionsMap.get(integer2);
			if(!potionEffect3.onUpdate(this) && !this.worldObj.isRemote) {
				iterator1.remove();
				this.onFinishedPotionEffect(potionEffect3);
			}
		}

		int i9;
		if(this.potionsNeedUpdate) {
			if(!this.worldObj.isRemote) {
				if(!this.activePotionsMap.isEmpty()) {
					i9 = PotionHelper.func_40553_a(this.activePotionsMap.values());
					this.dataWatcher.updateObject(8, i9);
				} else {
					this.dataWatcher.updateObject(8, 0);
				}
			}

			this.potionsNeedUpdate = false;
		}

		if(this.rand.nextBoolean()) {
			i9 = this.dataWatcher.getWatchableObjectInt(8);
			if(i9 > 0) {
				double d10 = (double)(i9 >> 16 & 255) / 255.0D;
				double d5 = (double)(i9 >> 8 & 255) / 255.0D;
				double d7 = (double)(i9 >> 0 & 255) / 255.0D;
				this.worldObj.spawnParticle("mobSpell", this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - (double)this.yOffset, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, d10, d5, d7);
			}
		}

	}

	public void clearActivePotions() {
		Iterator iterator1 = this.activePotionsMap.keySet().iterator();

		while(iterator1.hasNext()) {
			Integer integer2 = (Integer)iterator1.next();
			PotionEffect potionEffect3 = (PotionEffect)this.activePotionsMap.get(integer2);
			if(!this.worldObj.isRemote) {
				iterator1.remove();
				this.onFinishedPotionEffect(potionEffect3);
			}
		}

	}

	public Collection getActivePotionEffects() {
		return this.activePotionsMap.values();
	}

	public boolean isPotionActive(Potion potion1) {
		return this.activePotionsMap.containsKey(potion1.id);
	}

	public PotionEffect getActivePotionEffect(Potion potion1) {
		return (PotionEffect)this.activePotionsMap.get(potion1.id);
	}

	public void addPotionEffect(PotionEffect potionEffect1) {
		if(this.isPotionApplicable(potionEffect1)) {
			if(this.activePotionsMap.containsKey(potionEffect1.getPotionID())) {
				((PotionEffect)this.activePotionsMap.get(potionEffect1.getPotionID())).combine(potionEffect1);
				this.onChangedPotionEffect((PotionEffect)this.activePotionsMap.get(potionEffect1.getPotionID()));
			} else {
				this.activePotionsMap.put(potionEffect1.getPotionID(), potionEffect1);
				this.onNewPotionEffect(potionEffect1);
			}

		}
	}

	public boolean isPotionApplicable(PotionEffect potionEffect1) {
		if(this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
			int i2 = potionEffect1.getPotionID();
			if(i2 == Potion.regeneration.id || i2 == Potion.poison.id) {
				return false;
			}
		}

		return true;
	}

	public boolean isEntityUndead() {
		return this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
	}

	protected void onNewPotionEffect(PotionEffect potionEffect1) {
		this.potionsNeedUpdate = true;
	}

	protected void onChangedPotionEffect(PotionEffect potionEffect1) {
		this.potionsNeedUpdate = true;
	}

	protected void onFinishedPotionEffect(PotionEffect potionEffect1) {
		this.potionsNeedUpdate = true;
	}

	protected float getSpeedModifier() {
		float f1 = 1.0F;
		if(this.isPotionActive(Potion.moveSpeed)) {
			f1 *= 1.0F + 0.2F * (float)(this.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
		}

		if(this.isPotionActive(Potion.moveSlowdown)) {
			f1 *= 1.0F - 0.15F * (float)(this.getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
		}

		return f1;
	}

	public void setPositionAndUpdate(double d1, double d3, double d5) {
		this.setLocationAndAngles(d1, d3, d5, this.rotationYaw, this.rotationPitch);
	}

	public boolean isChild() {
		return false;
	}

	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}

	public void renderBrokenItemStack(ItemStack itemStack1) {
		this.worldObj.playSoundAtEntity(this, "random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);

		for(int i2 = 0; i2 < 5; ++i2) {
			Vec3D vec3D3 = Vec3D.createVector(((double)this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
			vec3D3.rotateAroundX(-this.rotationPitch * (float)Math.PI / 180.0F);
			vec3D3.rotateAroundY(-this.rotationYaw * (float)Math.PI / 180.0F);
			Vec3D vec3D4 = Vec3D.createVector(((double)this.rand.nextFloat() - 0.5D) * 0.3D, (double)(-this.rand.nextFloat()) * 0.6D - 0.3D, 0.6D);
			vec3D4.rotateAroundX(-this.rotationPitch * (float)Math.PI / 180.0F);
			vec3D4.rotateAroundY(-this.rotationYaw * (float)Math.PI / 180.0F);
			vec3D4 = vec3D4.addVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
			this.worldObj.spawnParticle("iconcrack_" + itemStack1.getItem().shiftedIndex, vec3D4.xCoord, vec3D4.yCoord, vec3D4.zCoord, vec3D3.xCoord, vec3D3.yCoord + 0.05D, vec3D3.zCoord);
		}

	}
}
