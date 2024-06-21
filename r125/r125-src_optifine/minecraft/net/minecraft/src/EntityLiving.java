package net.minecraft.src;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public abstract class EntityLiving extends Entity {
	public int heartsHalvesLife = 20;
	public float field_9365_p;
	public float field_9363_r;
	public float renderYawOffset = 0.0F;
	public float prevRenderYawOffset = 0.0F;
	public float rotationYawHead = 0.0F;
	public float prevRotationYawHead = 0.0F;
	protected float field_9362_u;
	protected float field_9361_v;
	protected float field_9360_w;
	protected float field_9359_x;
	protected boolean field_9358_y = true;
	protected String texture = "/mob/char.png";
	protected boolean field_9355_A = true;
	protected float field_9353_B = 0.0F;
	protected String entityType = null;
	protected float field_9349_D = 1.0F;
	protected int scoreValue = 0;
	protected float field_9345_F = 0.0F;
	public float landMovementFactor = 0.1F;
	public float jumpMovementFactor = 0.02F;
	public float prevSwingProgress;
	public float swingProgress;
	protected int health = this.getMaxHealth();
	public int prevHealth;
	public int carryoverDamage;
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
	public int field_9326_T = -1;
	public float field_9325_U = (float)(Math.random() * (double)0.9F + (double)0.1F);
	public float field_705_Q;
	public float field_704_R;
	public float field_703_S;
	protected EntityPlayer attackingPlayer = null;
	protected int recentlyHit = 0;
	private EntityLiving entityLivingToAttack = null;
	private int revengeTimer = 0;
	private EntityLiving lastAttackingEntity = null;
	public int arrowHitTempCounter = 0;
	public int arrowHitTimer = 0;
	protected HashMap activePotionsMap = new HashMap();
	private boolean potionsNeedUpdate = true;
	private int field_39002_c;
	private EntityLookHelper lookHelper;
	private EntityMoveHelper moveHelper;
	private EntityJumpHelper jumpHelper;
	private EntityBodyHelper bodyHelper;
	private PathNavigate navigator;
	protected EntityAITasks tasks = new EntityAITasks();
	protected EntityAITasks targetTasks = new EntityAITasks();
	private EntityLiving attackTarget;
	private EntitySenses field_48104_at;
	private float field_48111_au;
	private ChunkCoordinates homePosition = new ChunkCoordinates(0, 0, 0);
	private float maximumHomeDistance = -1.0F;
	protected int newPosRotationIncrements;
	protected double newPosX;
	protected double newPosY;
	protected double newPosZ;
	protected double newRotationYaw;
	protected double newRotationPitch;
	float field_9348_ae = 0.0F;
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
	public int persistentId = this.rand.nextInt(Integer.MAX_VALUE);

	public EntityLiving(World par1World) {
		super(par1World);
		this.preventEntitySpawning = true;
		this.lookHelper = new EntityLookHelper(this);
		this.moveHelper = new EntityMoveHelper(this);
		this.jumpHelper = new EntityJumpHelper(this);
		this.bodyHelper = new EntityBodyHelper(this);
		this.navigator = new PathNavigate(this, par1World, 16.0F);
		this.field_48104_at = new EntitySenses(this);
		this.field_9363_r = (float)(Math.random() + 1.0D) * 0.01F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.field_9365_p = (float)Math.random() * 12398.0F;
		this.rotationYaw = (float)(Math.random() * Math.PI * 2.0D);
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

	public EntitySenses func_48090_aM() {
		return this.field_48104_at;
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

	public void setLastAttackingEntity(Entity par1Entity) {
		if(par1Entity instanceof EntityLiving) {
			this.lastAttackingEntity = (EntityLiving)par1Entity;
		}

	}

	public int getAge() {
		return this.entityAge;
	}

	public void func_48079_f(float par1) {
		this.rotationYawHead = par1;
	}

	public float func_48101_aR() {
		return this.field_48111_au;
	}

	public void func_48098_g(float par1) {
		this.field_48111_au = par1;
		this.setMoveForward(par1);
	}

	public boolean attackEntityAsMob(Entity par1Entity) {
		this.setLastAttackingEntity(par1Entity);
		return false;
	}

	public EntityLiving getAttackTarget() {
		return this.attackTarget;
	}

	public void setAttackTarget(EntityLiving par1EntityLiving) {
		this.attackTarget = par1EntityLiving;
		if(Reflector.hasClass(9)) {
			Reflector.callVoid(90, new Object[]{this, par1EntityLiving});
		}

	}

	public boolean func_48100_a(Class par1Class) {
		return EntityCreeper.class != par1Class && EntityGhast.class != par1Class;
	}

	public void eatGrassBonus() {
	}

	public boolean isWithinHomeDistanceCurrentPosition() {
		return this.isWithinHomeDistance(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
	}

	public boolean isWithinHomeDistance(int par1, int par2, int par3) {
		return this.maximumHomeDistance == -1.0F ? true : this.homePosition.getDistanceSquared(par1, par2, par3) < this.maximumHomeDistance * this.maximumHomeDistance;
	}

	public void setHomeArea(int par1, int par2, int par3, int par4) {
		this.homePosition.set(par1, par2, par3);
		this.maximumHomeDistance = (float)par4;
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

	public void setRevengeTarget(EntityLiving par1EntityLiving) {
		this.entityLivingToAttack = par1EntityLiving;
		this.revengeTimer = this.entityLivingToAttack == null ? 0 : 60;
		if(Reflector.hasClass(9)) {
			Reflector.callVoid(90, new Object[]{this, par1EntityLiving});
		}

	}

	protected void entityInit() {
		this.dataWatcher.addObject(8, this.field_39002_c);
	}

	public boolean canEntityBeSeen(Entity par1Entity) {
		return this.worldObj.rayTraceBlocks(Vec3D.createVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), Vec3D.createVector(par1Entity.posX, par1Entity.posY + (double)par1Entity.getEyeHeight(), par1Entity.posZ)) == null;
	}

	public String getTexture() {
		return this.texture;
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
		String s = this.getLivingSound();
		if(s != null) {
			this.worldObj.playSoundAtEntity(this, s, this.getSoundVolume(), this.getSoundPitch());
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

		if(this.isEntityAlive() && this.isEntityInsideOpaqueBlock() && !this.attackEntityFrom(DamageSource.inWall, 1)) {
			;
		}

		if(this.isImmuneToFire() || this.worldObj.isRemote) {
			this.extinguish();
		}

		if(this.isEntityAlive() && this.isInsideOfMaterial(Material.water) && !this.canBreatheUnderwater() && !this.activePotionsMap.containsKey(Potion.waterBreathing.id)) {
			this.setAir(this.decreaseAirSupply(this.getAir()));
			if(this.getAir() == -20) {
				this.setAir(0);

				for(int i = 0; i < 8; ++i) {
					float f = this.rand.nextFloat() - this.rand.nextFloat();
					float f1 = this.rand.nextFloat() - this.rand.nextFloat();
					float f2 = this.rand.nextFloat() - this.rand.nextFloat();
					this.worldObj.spawnParticle("bubble", this.posX + (double)f, this.posY + (double)f1, this.posZ + (double)f2, this.motionX, this.motionY, this.motionZ);
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
		this.field_9359_x = this.field_9360_w;
		this.prevRenderYawOffset = this.renderYawOffset;
		this.prevRotationYawHead = this.rotationYawHead;
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		Profiler.endSection();
	}

	protected void onDeathUpdate() {
		++this.deathTime;
		if(this.deathTime == 20) {
			int j;
			if(!this.worldObj.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && !this.isChild()) {
				j = this.getExperiencePoints(this.attackingPlayer);

				while(j > 0) {
					int d = EntityXPOrb.getXPSplit(j);
					j -= d;
					this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, d));
				}
			}

			this.onEntityDeath();
			this.setDead();

			for(j = 0; j < 20; ++j) {
				double d8 = this.rand.nextGaussian() * 0.02D;
				double d1 = this.rand.nextGaussian() * 0.02D;
				double d2 = this.rand.nextGaussian() * 0.02D;
				this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d8, d1, d2);
			}
		}

	}

	protected int decreaseAirSupply(int par1) {
		return par1 - 1;
	}

	protected int getExperiencePoints(EntityPlayer par1EntityPlayer) {
		return this.experienceValue;
	}

	protected boolean isPlayer() {
		return false;
	}

	public void spawnExplosionParticle() {
		for(int i = 0; i < 20; ++i) {
			double d = this.rand.nextGaussian() * 0.02D;
			double d1 = this.rand.nextGaussian() * 0.02D;
			double d2 = this.rand.nextGaussian() * 0.02D;
			double d3 = 10.0D;
			this.worldObj.spawnParticle("explode", this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d * d3, this.posY + (double)(this.rand.nextFloat() * this.height) - d1 * d3, this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width - d2 * d3, d, d1, d2);
		}

	}

	public void updateRidden() {
		super.updateRidden();
		this.field_9362_u = this.field_9361_v;
		this.field_9361_v = 0.0F;
		this.fallDistance = 0.0F;
	}

	public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
		this.yOffset = 0.0F;
		this.newPosX = par1;
		this.newPosY = par3;
		this.newPosZ = par5;
		this.newRotationYaw = (double)par7;
		this.newRotationPitch = (double)par8;
		this.newPosRotationIncrements = par9;
	}

	public void onUpdate() {
		if(Reflector.hasClass(9)) {
			boolean d = Reflector.callBoolean(91, new Object[]{this});
			if(d) {
				return;
			}
		}

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
		double d12 = this.posX - this.prevPosX;
		double d1 = this.posZ - this.prevPosZ;
		float f = MathHelper.sqrt_double(d12 * d12 + d1 * d1);
		float f1 = this.renderYawOffset;
		float f2 = 0.0F;
		this.field_9362_u = this.field_9361_v;
		float f3 = 0.0F;
		if(f > 0.05F) {
			f3 = 1.0F;
			f2 = f * 3.0F;
			f1 = (float)Math.atan2(d1, d12) * 180.0F / (float)Math.PI - 90.0F;
		}

		if(this.swingProgress > 0.0F) {
			f1 = this.rotationYaw;
		}

		if(!this.onGround) {
			f3 = 0.0F;
		}

		this.field_9361_v += (f3 - this.field_9361_v) * 0.3F;
		if(this.isAIEnabled()) {
			this.bodyHelper.func_48650_a();
		} else {
			float f4;
			for(f4 = f1 - this.renderYawOffset; f4 < -180.0F; f4 += 360.0F) {
			}

			while(f4 >= 180.0F) {
				f4 -= 360.0F;
			}

			this.renderYawOffset += f4 * 0.3F;

			float f5;
			for(f5 = this.rotationYaw - this.renderYawOffset; f5 < -180.0F; f5 += 360.0F) {
			}

			while(f5 >= 180.0F) {
				f5 -= 360.0F;
			}

			boolean flag = f5 < -90.0F || f5 >= 90.0F;
			if(f5 < -75.0F) {
				f5 = -75.0F;
			}

			if(f5 >= 75.0F) {
				f5 = 75.0F;
			}

			this.renderYawOffset = this.rotationYaw - f5;
			if(f5 * f5 > 2500.0F) {
				this.renderYawOffset += f5 * 0.2F;
			}

			if(flag) {
				f2 *= -1.0F;
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

		this.field_9360_w += f2;
	}

	protected void setSize(float par1, float par2) {
		super.setSize(par1, par2);
	}

	public void heal(int par1) {
		if(this.health > 0) {
			this.health += par1;
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

	public void setEntityHealth(int par1) {
		this.health = par1;
		if(par1 > this.getMaxHealth()) {
			par1 = this.getMaxHealth();
		}

	}

	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		boolean flag;
		if(Reflector.hasClass(9)) {
			flag = Reflector.callBoolean(92, new Object[]{this, par1DamageSource, par2});
			if(flag) {
				return false;
			}
		}

		if(this.worldObj.isRemote) {
			return false;
		} else {
			this.entityAge = 0;
			if(this.health <= 0) {
				return false;
			} else if(par1DamageSource.fireDamage() && this.isPotionActive(Potion.fireResistance)) {
				return false;
			} else {
				this.field_704_R = 1.5F;
				flag = true;
				if((float)this.heartsLife > (float)this.heartsHalvesLife / 2.0F) {
					if(par2 <= this.naturalArmorRating) {
						return false;
					}

					this.damageEntity(par1DamageSource, par2 - this.naturalArmorRating);
					this.naturalArmorRating = par2;
					flag = false;
				} else {
					this.naturalArmorRating = par2;
					this.prevHealth = this.health;
					this.heartsLife = this.heartsHalvesLife;
					this.damageEntity(par1DamageSource, par2);
					this.hurtTime = this.maxHurtTime = 10;
				}

				this.attackedAtYaw = 0.0F;
				Entity entity = par1DamageSource.getEntity();
				if(entity != null) {
					if(entity instanceof EntityLiving) {
						this.setRevengeTarget((EntityLiving)entity);
					}

					if(entity instanceof EntityPlayer) {
						this.recentlyHit = 60;
						this.attackingPlayer = (EntityPlayer)entity;
					} else if(entity instanceof EntityWolf) {
						EntityWolf d = (EntityWolf)entity;
						if(d.isTamed()) {
							this.recentlyHit = 60;
							this.attackingPlayer = null;
						}
					}
				}

				if(flag) {
					this.worldObj.setEntityState(this, (byte)2);
					this.setBeenAttacked();
					if(entity != null) {
						double d1 = entity.posX - this.posX;

						double d1;
						for(d1 = entity.posZ - this.posZ; d1 * d1 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
							d1 = (Math.random() - Math.random()) * 0.01D;
						}

						this.attackedAtYaw = (float)(Math.atan2(d1, d1) * 180.0D / Math.PI) - this.rotationYaw;
						this.knockBack(entity, par2, d1, d1);
					} else {
						this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
					}
				}

				if(this.health <= 0) {
					if(flag) {
						this.worldObj.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), this.getSoundPitch());
					}

					this.onDeath(par1DamageSource);
				} else if(flag) {
					this.worldObj.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), this.getSoundPitch());
				}

				return true;
			}
		}
	}

	private float getSoundPitch() {
		return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
	}

	public void performHurtAnimation() {
		this.hurtTime = this.maxHurtTime = 10;
		this.attackedAtYaw = 0.0F;
	}

	public int getTotalArmorValue() {
		return 0;
	}

	protected void damageArmor(int i) {
	}

	protected int applyArmorCalculations(DamageSource par1DamageSource, int par2) {
		if(!par1DamageSource.isUnblockable()) {
			int i = 25 - this.getTotalArmorValue();
			int j = par2 * i + this.carryoverDamage;
			this.damageArmor(par2);
			par2 = j / 25;
			this.carryoverDamage = j % 25;
		}

		return par2;
	}

	protected int applyPotionDamageCalculations(DamageSource par1DamageSource, int par2) {
		if(this.isPotionActive(Potion.resistance)) {
			int i = (this.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
			int j = 25 - i;
			int k = par2 * j + this.carryoverDamage;
			par2 = k / 25;
			this.carryoverDamage = k % 25;
		}

		return par2;
	}

	protected void damageEntity(DamageSource par1DamageSource, int par2) {
		if(Reflector.hasClass(9)) {
			par2 = Reflector.callInt(93, new Object[]{this, par1DamageSource, par2});
			if(par2 == 0) {
				return;
			}
		}

		par2 = this.applyArmorCalculations(par1DamageSource, par2);
		par2 = this.applyPotionDamageCalculations(par1DamageSource, par2);
		this.health -= par2;
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

	public void knockBack(Entity par1Entity, int par2, double par3, double par5) {
		this.isAirBorne = true;
		float f = MathHelper.sqrt_double(par3 * par3 + par5 * par5);
		float f1 = 0.4F;
		this.motionX /= 2.0D;
		this.motionY /= 2.0D;
		this.motionZ /= 2.0D;
		this.motionX -= par3 / (double)f * (double)f1;
		this.motionY += (double)f1;
		this.motionZ -= par5 / (double)f * (double)f1;
		if(this.motionY > (double)0.4F) {
			this.motionY = (double)0.4F;
		}

	}

	public void onDeath(DamageSource par1DamageSource) {
		if(Reflector.hasClass(9)) {
			boolean entity = Reflector.callBoolean(94, new Object[]{this, par1DamageSource});
			if(entity) {
				return;
			}
		}

		Entity entity1 = par1DamageSource.getEntity();
		if(this.scoreValue >= 0 && entity1 != null) {
			entity1.addToPlayerScore(this, this.scoreValue);
		}

		if(entity1 != null) {
			entity1.onKillEntity(this);
		}

		this.dead = true;
		if(!this.worldObj.isRemote) {
			int i = 0;
			if(entity1 instanceof EntityPlayer) {
				i = EnchantmentHelper.getLootingModifier(((EntityPlayer)entity1).inventory);
			}

			List listCapturedDrops = null;
			if(Reflector.hasClass(9)) {
				Reflector.setFieldValue(this, 100, Boolean.TRUE);
				listCapturedDrops = (List)Reflector.getFieldValue(this, 101);
				Reflector.callVoid(listCapturedDrops, 110, (Object[])null);
			}

			int j = 0;
			if(!this.isChild()) {
				this.dropFewItems(this.recentlyHit > 0, i);
				if(this.recentlyHit > 0) {
					j = this.rand.nextInt(200) - i;
					if(j < 5) {
						this.dropRareDrop(j > 0 ? 0 : 1);
					}
				}
			}

			if(Reflector.hasClass(9)) {
				Reflector.setFieldValue(this, 100, Boolean.FALSE);
				Reflector.callVoid(95, new Object[]{this, par1DamageSource, listCapturedDrops, i, this.recentlyHit > 0, j});
				Iterator iter = listCapturedDrops.iterator();

				while(iter.hasNext()) {
					EntityItem item = (EntityItem)iter.next();
					this.worldObj.spawnEntityInWorld(item);
				}
			}
		}

		this.worldObj.setEntityState(this, (byte)3);
	}

	protected void dropRareDrop(int i) {
	}

	protected void dropFewItems(boolean par1, int par2) {
		int i = this.getDropItemId();
		if(i > 0) {
			int j = this.rand.nextInt(3);
			if(par2 > 0) {
				j += this.rand.nextInt(par2 + 1);
			}

			for(int k = 0; k < j; ++k) {
				this.dropItem(i, 1);
			}
		}

	}

	protected int getDropItemId() {
		return 0;
	}

	protected void fall(float par1) {
		if(Reflector.hasClass(9)) {
			boolean i = Reflector.callBoolean(96, new Object[]{this, par1});
			if(i) {
				return;
			}
		}

		super.fall(par1);
		int i1 = (int)Math.ceil((double)(par1 - 3.0F));
		if(i1 > 0) {
			if(i1 > 4) {
				this.worldObj.playSoundAtEntity(this, "damage.fallbig", 1.0F, 1.0F);
			} else {
				this.worldObj.playSoundAtEntity(this, "damage.fallsmall", 1.0F, 1.0F);
			}

			this.attackEntityFrom(DamageSource.fall, i1);
			int j = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - (double)0.2F - (double)this.yOffset), MathHelper.floor_double(this.posZ));
			if(j > 0) {
				StepSound stepsound = Block.blocksList[j].stepSound;
				this.worldObj.playSoundAtEntity(this, stepsound.getStepSound(), stepsound.getVolume() * 0.5F, stepsound.getPitch() * 0.75F);
			}
		}

	}

	public void moveEntityWithHeading(float par1, float par2) {
		double d2;
		if(this.isInWater()) {
			d2 = this.posY;
			this.moveFlying(par1, par2, this.isAIEnabled() ? 0.04F : 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= (double)0.8F;
			this.motionY *= (double)0.8F;
			this.motionZ *= (double)0.8F;
			this.motionY -= 0.02D;
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6F - this.posY + d2, this.motionZ)) {
				this.motionY = (double)0.3F;
			}
		} else if(this.handleLavaMovement()) {
			d2 = this.posY;
			this.moveFlying(par1, par2, 0.02F);
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.5D;
			this.motionY *= 0.5D;
			this.motionZ *= 0.5D;
			this.motionY -= 0.02D;
			if(this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + (double)0.6F - this.posY + d2, this.motionZ)) {
				this.motionY = (double)0.3F;
			}
		} else {
			float d21 = 0.91F;
			if(this.onGround) {
				d21 = 0.5460001F;
				int f1 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
				if(f1 > 0) {
					d21 = Block.blocksList[f1].slipperiness * 0.91F;
				}
			}

			float f11 = 0.1627714F / (d21 * d21 * d21);
			float d3;
			if(this.onGround) {
				if(this.isAIEnabled()) {
					d3 = this.func_48101_aR();
				} else {
					d3 = this.landMovementFactor;
				}

				d3 *= f11;
			} else {
				d3 = this.jumpMovementFactor;
			}

			this.moveFlying(par1, par2, d3);
			d21 = 0.91F;
			if(this.onGround) {
				d21 = 0.5460001F;
				int f3 = this.worldObj.getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
				if(f3 > 0) {
					d21 = Block.blocksList[f3].slipperiness * 0.91F;
				}
			}

			if(this.isOnLadder()) {
				float f31 = 0.15F;
				if(this.motionX < (double)(-f31)) {
					this.motionX = (double)(-f31);
				}

				if(this.motionX > (double)f31) {
					this.motionX = (double)f31;
				}

				if(this.motionZ < (double)(-f31)) {
					this.motionZ = (double)(-f31);
				}

				if(this.motionZ > (double)f31) {
					this.motionZ = (double)f31;
				}

				this.fallDistance = 0.0F;
				if(this.motionY < -0.15D) {
					this.motionY = -0.15D;
				}

				boolean f4 = this.isSneaking() && this instanceof EntityPlayer;
				if(f4 && this.motionY < 0.0D) {
					this.motionY = 0.0D;
				}
			}

			this.moveEntity(this.motionX, this.motionY, this.motionZ);
			if(this.isCollidedHorizontally && this.isOnLadder()) {
				this.motionY = 0.2D;
			}

			this.motionY -= 0.08D;
			this.motionY *= (double)0.98F;
			this.motionX *= (double)d21;
			this.motionZ *= (double)d21;
		}

		this.field_705_Q = this.field_704_R;
		d2 = this.posX - this.prevPosX;
		double d31 = this.posZ - this.prevPosZ;
		float f41 = MathHelper.sqrt_double(d2 * d2 + d31 * d31) * 4.0F;
		if(f41 > 1.0F) {
			f41 = 1.0F;
		}

		this.field_704_R += (f41 - this.field_704_R) * 0.4F;
		this.field_703_S += this.field_704_R;
	}

	public boolean isOnLadder() {
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.boundingBox.minY);
		int k = MathHelper.floor_double(this.posZ);
		int l = this.worldObj.getBlockId(i, j, k);
		if(Reflector.hasMethod(50)) {
			Block block = Block.blocksList[l];
			if(block != null) {
				return Reflector.callBoolean(block, 50, new Object[]{this.worldObj, i, j, k});
			}
		}

		return l == Block.ladder.blockID || l == Block.vine.blockID;
	}

	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		par1NBTTagCompound.setShort("Health", (short)this.health);
		par1NBTTagCompound.setShort("HurtTime", (short)this.hurtTime);
		par1NBTTagCompound.setShort("DeathTime", (short)this.deathTime);
		par1NBTTagCompound.setShort("AttackTime", (short)this.attackTime);
		if(!this.activePotionsMap.isEmpty()) {
			NBTTagList nbttaglist = new NBTTagList();
			Iterator iterator = this.activePotionsMap.values().iterator();

			while(iterator.hasNext()) {
				PotionEffect potioneffect = (PotionEffect)iterator.next();
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Id", (byte)potioneffect.getPotionID());
				nbttagcompound.setByte("Amplifier", (byte)potioneffect.getAmplifier());
				nbttagcompound.setInteger("Duration", potioneffect.getDuration());
				nbttaglist.appendTag(nbttagcompound);
			}

			par1NBTTagCompound.setTag("ActiveEffects", nbttaglist);
		}

		par1NBTTagCompound.setInteger("PersistentId", this.persistentId);
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		if(this.health < -32768) {
			this.health = -32768;
		}

		this.health = par1NBTTagCompound.getShort("Health");
		if(!par1NBTTagCompound.hasKey("Health")) {
			this.health = this.getMaxHealth();
		}

		this.hurtTime = par1NBTTagCompound.getShort("HurtTime");
		this.deathTime = par1NBTTagCompound.getShort("DeathTime");
		this.attackTime = par1NBTTagCompound.getShort("AttackTime");
		if(par1NBTTagCompound.hasKey("ActiveEffects")) {
			NBTTagList nbttaglist = par1NBTTagCompound.getTagList("ActiveEffects");

			for(int i = 0; i < nbttaglist.tagCount(); ++i) {
				NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);
				byte byte0 = nbttagcompound.getByte("Id");
				byte byte1 = nbttagcompound.getByte("Amplifier");
				int j = nbttagcompound.getInteger("Duration");
				this.activePotionsMap.put(Integer.valueOf(byte0), new PotionEffect(byte0, j, byte1));
			}
		}

		this.persistentId = par1NBTTagCompound.getInteger("PersistentId");
		if(this.persistentId == 0) {
			this.persistentId = this.rand.nextInt(Integer.MAX_VALUE);
		}

	}

	public boolean isEntityAlive() {
		return !this.isDead && this.health > 0;
	}

	public boolean canBreatheUnderwater() {
		return false;
	}

	public void setMoveForward(float par1) {
		this.moveForward = par1;
	}

	public void setJumping(boolean par1) {
		this.isJumping = par1;
	}

	public void onLivingUpdate() {
		if(this.jumpTicks > 0) {
			--this.jumpTicks;
		}

		if(this.newPosRotationIncrements > 0) {
			double flag = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
			double f = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
			double i = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;

			double d3;
			for(d3 = this.newRotationYaw - (double)this.rotationYaw; d3 < -180.0D; d3 += 360.0D) {
			}

			while(d3 >= 180.0D) {
				d3 -= 360.0D;
			}

			this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.newPosRotationIncrements);
			this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
			--this.newPosRotationIncrements;
			this.setPosition(flag, f, i);
			this.setRotation(this.rotationYaw, this.rotationPitch);
			List list1 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.contract(8.0D / 256D, 0.0D, 8.0D / 256D));
			if(list1.size() > 0) {
				double d4 = 0.0D;

				for(int j = 0; j < list1.size(); ++j) {
					AxisAlignedBB axisalignedbb = (AxisAlignedBB)list1.get(j);
					if(axisalignedbb.maxY > d4) {
						d4 = axisalignedbb.maxY;
					}
				}

				f += d4 - this.boundingBox.minY;
				this.setPosition(flag, f, i);
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
		boolean flag1 = this.handleLavaMovement();
		if(this.isJumping) {
			if(z14) {
				this.motionY += (double)0.04F;
			} else if(flag1) {
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
		List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand((double)0.2F, 0.0D, (double)0.2F));
		if(list != null && list.size() > 0) {
			for(int i16 = 0; i16 < list.size(); ++i16) {
				Entity entity = (Entity)list.get(i16);
				if(entity.canBePushed()) {
					entity.applyEntityCollision(this);
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
			float f = this.rotationYaw * 0.01745329F;
			this.motionX -= (double)(MathHelper.sin(f) * 0.2F);
			this.motionZ += (double)(MathHelper.cos(f) * 0.2F);
		}

		this.isAirBorne = true;
		if(Reflector.hasClass(9)) {
			Reflector.callVoid(97, new Object[]{this});
		}

	}

	protected boolean canDespawn() {
		return true;
	}

	protected void despawnEntity() {
		EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, -1.0D);
		if(entityplayer != null) {
			double d = entityplayer.posX - this.posX;
			double d1 = entityplayer.posY - this.posY;
			double d2 = entityplayer.posZ - this.posZ;
			double d3 = d * d + d1 * d1 + d2 * d2;
			if(this.canDespawn() && d3 > 16384.0D) {
				this.setDead();
			}

			if(this.entityAge > 600 && this.rand.nextInt(800) == 0 && d3 > 1024.0D && this.canDespawn()) {
				this.setDead();
			} else if(d3 < 1024.0D) {
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
		this.field_48104_at.clearSensingCache();
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
		float f = 8.0F;
		if(this.rand.nextFloat() < 0.02F) {
			EntityPlayer flag = this.worldObj.getClosestPlayerToEntity(this, (double)f);
			if(flag != null) {
				this.currentTarget = flag;
				this.numTicksToChaseTarget = 10 + this.rand.nextInt(20);
			} else {
				this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
			}
		}

		if(this.currentTarget != null) {
			this.faceEntity(this.currentTarget, 10.0F, (float)this.getVerticalFaceSpeed());
			if(this.numTicksToChaseTarget-- <= 0 || this.currentTarget.isDead || this.currentTarget.getDistanceSqToEntity(this) > (double)(f * f)) {
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
		boolean flag1 = this.handleLavaMovement();
		if(z4 || flag1) {
			this.isJumping = this.rand.nextFloat() < 0.8F;
		}

	}

	public int getVerticalFaceSpeed() {
		return 40;
	}

	public void faceEntity(Entity par1Entity, float par2, float par3) {
		double d = par1Entity.posX - this.posX;
		double d2 = par1Entity.posZ - this.posZ;
		double d1;
		if(par1Entity instanceof EntityLiving) {
			EntityLiving d3 = (EntityLiving)par1Entity;
			d1 = this.posY + (double)this.getEyeHeight() - (d3.posY + (double)d3.getEyeHeight());
		} else {
			d1 = (par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / 2.0D - (this.posY + (double)this.getEyeHeight());
		}

		double d31 = (double)MathHelper.sqrt_double(d * d + d2 * d2);
		float f = (float)(Math.atan2(d2, d) * 180.0D / Math.PI) - 90.0F;
		float f1 = (float)(-(Math.atan2(d1, d31) * 180.0D / Math.PI));
		this.rotationPitch = -this.updateRotation(this.rotationPitch, f1, par3);
		this.rotationYaw = this.updateRotation(this.rotationYaw, f, par2);
	}

	private float updateRotation(float par1, float par2, float par3) {
		float f;
		for(f = par2 - par1; f < -180.0F; f += 360.0F) {
		}

		while(f >= 180.0F) {
			f -= 360.0F;
		}

		if(f > par3) {
			f = par3;
		}

		if(f < -par3) {
			f = -par3;
		}

		return par1 + f;
	}

	public void onEntityDeath() {
	}

	public boolean getCanSpawnHere() {
		return this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.isAnyLiquid(this.boundingBox);
	}

	protected void kill() {
		this.attackEntityFrom(DamageSource.outOfWorld, 4);
	}

	public float getSwingProgress(float par1) {
		float f = this.swingProgress - this.prevSwingProgress;
		if(f < 0.0F) {
			++f;
		}

		return this.prevSwingProgress + f * par1;
	}

	public Vec3D getPosition(float par1) {
		if(par1 == 1.0F) {
			return Vec3D.createVector(this.posX, this.posY, this.posZ);
		} else {
			double d = this.prevPosX + (this.posX - this.prevPosX) * (double)par1;
			double d1 = this.prevPosY + (this.posY - this.prevPosY) * (double)par1;
			double d2 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double)par1;
			return Vec3D.createVector(d, d1, d2);
		}
	}

	public Vec3D getLookVec() {
		return this.getLook(1.0F);
	}

	public Vec3D getLook(float par1) {
		float f1;
		float f3;
		float f5;
		float f7;
		if(par1 == 1.0F) {
			f1 = MathHelper.cos(-this.rotationYaw * 0.01745329F - (float)Math.PI);
			f3 = MathHelper.sin(-this.rotationYaw * 0.01745329F - (float)Math.PI);
			f5 = -MathHelper.cos(-this.rotationPitch * 0.01745329F);
			f7 = MathHelper.sin(-this.rotationPitch * 0.01745329F);
			return Vec3D.createVector((double)(f3 * f5), (double)f7, (double)(f1 * f5));
		} else {
			f1 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * par1;
			f3 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * par1;
			f5 = MathHelper.cos(-f3 * 0.01745329F - (float)Math.PI);
			f7 = MathHelper.sin(-f3 * 0.01745329F - (float)Math.PI);
			float f8 = -MathHelper.cos(-f1 * 0.01745329F);
			float f9 = MathHelper.sin(-f1 * 0.01745329F);
			return Vec3D.createVector((double)(f7 * f8), (double)f9, (double)(f5 * f8));
		}
	}

	public float getRenderSizeModifier() {
		return 1.0F;
	}

	public MovingObjectPosition rayTrace(double par1, float par3) {
		Vec3D vec3d = this.getPosition(par3);
		Vec3D vec3d1 = this.getLook(par3);
		Vec3D vec3d2 = vec3d.addVector(vec3d1.xCoord * par1, vec3d1.yCoord * par1, vec3d1.zCoord * par1);
		return this.worldObj.rayTraceBlocks(vec3d, vec3d2);
	}

	public int getMaxSpawnedInChunk() {
		return 4;
	}

	public ItemStack getHeldItem() {
		return null;
	}

	public void handleHealthUpdate(byte par1) {
		if(par1 == 2) {
			this.field_704_R = 1.5F;
			this.heartsLife = this.heartsHalvesLife;
			this.hurtTime = this.maxHurtTime = 10;
			this.attackedAtYaw = 0.0F;
			this.worldObj.playSoundAtEntity(this, this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.attackEntityFrom(DamageSource.generic, 0);
		} else if(par1 == 3) {
			this.worldObj.playSoundAtEntity(this, this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			this.health = 0;
			this.onDeath(DamageSource.generic);
		} else {
			super.handleHealthUpdate(par1);
		}

	}

	public boolean isPlayerSleeping() {
		return false;
	}

	public int getItemIcon(ItemStack par1ItemStack, int par2) {
		return par1ItemStack.getIconIndex();
	}

	protected void updatePotionEffects() {
		Iterator iterator = this.activePotionsMap.keySet().iterator();

		while(iterator.hasNext()) {
			Integer j = (Integer)iterator.next();
			PotionEffect d = (PotionEffect)this.activePotionsMap.get(j);
			if(!d.onUpdate(this) && !this.worldObj.isRemote) {
				iterator.remove();
				this.onFinishedPotionEffect(d);
			}
		}

		int j1;
		if(this.potionsNeedUpdate) {
			if(!this.worldObj.isRemote) {
				if(!this.activePotionsMap.isEmpty()) {
					j1 = PotionHelper.func_40354_a(this.activePotionsMap.values());
					this.dataWatcher.updateObject(8, j1);
				} else {
					this.dataWatcher.updateObject(8, 0);
				}
			}

			this.potionsNeedUpdate = false;
		}

		if(this.rand.nextBoolean()) {
			j1 = this.dataWatcher.getWatchableObjectInt(8);
			if(j1 > 0) {
				double d1 = (double)(j1 >> 16 & 255) / 255.0D;
				double d1 = (double)(j1 >> 8 & 255) / 255.0D;
				double d2 = (double)(j1 >> 0 & 255) / 255.0D;
				this.worldObj.spawnParticle("mobSpell", this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - (double)this.yOffset, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, d1, d1, d2);
			}
		}

	}

	public void clearActivePotions() {
		Iterator iterator = this.activePotionsMap.keySet().iterator();

		while(iterator.hasNext()) {
			Integer integer = (Integer)iterator.next();
			PotionEffect potioneffect = (PotionEffect)this.activePotionsMap.get(integer);
			if(!this.worldObj.isRemote) {
				iterator.remove();
				this.onFinishedPotionEffect(potioneffect);
			}
		}

	}

	public Collection getActivePotionEffects() {
		return this.activePotionsMap.values();
	}

	public boolean isPotionActive(Potion par1Potion) {
		return this.activePotionsMap.containsKey(par1Potion.id);
	}

	public PotionEffect getActivePotionEffect(Potion par1Potion) {
		return (PotionEffect)this.activePotionsMap.get(par1Potion.id);
	}

	public void addPotionEffect(PotionEffect par1PotionEffect) {
		if(this.isPotionApplicable(par1PotionEffect)) {
			if(this.activePotionsMap.containsKey(par1PotionEffect.getPotionID())) {
				((PotionEffect)this.activePotionsMap.get(par1PotionEffect.getPotionID())).combine(par1PotionEffect);
				this.onChangedPotionEffect((PotionEffect)this.activePotionsMap.get(par1PotionEffect.getPotionID()));
			} else {
				this.activePotionsMap.put(par1PotionEffect.getPotionID(), par1PotionEffect);
				this.onNewPotionEffect(par1PotionEffect);
			}

		}
	}

	public boolean isPotionApplicable(PotionEffect par1PotionEffect) {
		if(this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
			int i = par1PotionEffect.getPotionID();
			if(i == Potion.regeneration.id || i == Potion.poison.id) {
				return false;
			}
		}

		return true;
	}

	public boolean isEntityUndead() {
		return this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
	}

	public void removePotionEffect(int par1) {
		this.activePotionsMap.remove(par1);
	}

	protected void onNewPotionEffect(PotionEffect par1PotionEffect) {
		this.potionsNeedUpdate = true;
	}

	protected void onChangedPotionEffect(PotionEffect par1PotionEffect) {
		this.potionsNeedUpdate = true;
	}

	protected void onFinishedPotionEffect(PotionEffect par1PotionEffect) {
		this.potionsNeedUpdate = true;
	}

	protected float getSpeedModifier() {
		float f = 1.0F;
		if(this.isPotionActive(Potion.moveSpeed)) {
			f *= 1.0F + 0.2F * (float)(this.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
		}

		if(this.isPotionActive(Potion.moveSlowdown)) {
			f *= 1.0F - 0.15F * (float)(this.getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
		}

		return f;
	}

	public void setPositionAndUpdate(double par1, double par3, double par5) {
		this.setLocationAndAngles(par1, par3, par5, this.rotationYaw, this.rotationPitch);
	}

	public boolean isChild() {
		return false;
	}

	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}

	public void renderBrokenItemStack(ItemStack par1ItemStack) {
		this.worldObj.playSoundAtEntity(this, "random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);

		for(int i = 0; i < 5; ++i) {
			Vec3D vec3d = Vec3D.createVector(((double)this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
			vec3d.rotateAroundX(-this.rotationPitch * (float)Math.PI / 180.0F);
			vec3d.rotateAroundY(-this.rotationYaw * (float)Math.PI / 180.0F);
			Vec3D vec3d1 = Vec3D.createVector(((double)this.rand.nextFloat() - 0.5D) * 0.3D, (double)(-this.rand.nextFloat()) * 0.6D - 0.3D, 0.6D);
			vec3d1.rotateAroundX(-this.rotationPitch * (float)Math.PI / 180.0F);
			vec3d1.rotateAroundY(-this.rotationYaw * (float)Math.PI / 180.0F);
			vec3d1 = vec3d1.addVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
			this.worldObj.spawnParticle("iconcrack_" + par1ItemStack.getItem().shiftedIndex, vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord + 0.05D, vec3d.zCoord);
		}

	}
}
