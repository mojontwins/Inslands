package net.minecraft.src;

public class EntityMiniCloud extends EntityFlying {
	public int shotTimer;
	public int lifeSpan;
	public boolean gotPlayer;
	public boolean toLeft;
	public EntityLiving dude;
	public double targetX;
	public double targetY;
	public double targetZ;

	public EntityMiniCloud(World world) {
		super(world);
		this.texture = "/aether/mobs/minicloud.png";
		this.setSize(0.0F, 0.0F);
		this.noClip = true;
		this.entityCollisionReduction = 1.75F;
	}

	public EntityMiniCloud(World world, EntityPlayer ep, boolean flag) {
		super(world);
		this.texture = "/aether/mobs/minicloud.png";
		this.setSize(0.5F, 0.45F);
		this.dude = ep;
		this.toLeft = flag;
		this.lifeSpan = 3600;
		this.getTargetPos();
		this.setPosition(this.targetX, this.targetY, this.targetZ);
		this.rotationPitch = this.dude.rotationPitch;
		this.rotationYaw = this.dude.rotationYaw;
		this.entityCollisionReduction = 1.75F;
		this.spawnExplosionParticle();
	}

	public boolean isInRangeToRenderDist(double d) {
		return true;
	}

	public void getTargetPos() {
		if(this.getDistanceToEntity(this.dude) > 2.0F) {
			this.targetX = this.dude.posX;
			this.targetY = this.dude.posY - (double)0.1F;
			this.targetZ = this.dude.posZ;
		} else {
			double angle = (double)this.dude.rotationYaw;
			if(this.toLeft) {
				angle -= 90.0D;
			} else {
				angle += 90.0D;
			}

			angle /= -57.29577319531843D;
			this.targetX = this.dude.posX + Math.sin(angle) * 1.05D;
			this.targetY = this.dude.posY - (double)0.1F;
			this.targetZ = this.dude.posZ + Math.cos(angle) * 1.05D;
		}

	}

	public boolean atShoulder() {
		double a = this.posX - this.targetX;
		double b = this.posY - this.targetY;
		double c = this.posZ - this.targetZ;
		return Math.sqrt(a * a + b * b + c * c) < 0.3D;
	}

	public void approachTarget() {
		double a = this.targetX - this.posX;
		double b = this.targetY - this.posY;
		double c = this.targetZ - this.posZ;
		double d = Math.sqrt(a * a + b * b + c * c) * 3.25D;
		this.motionX = (this.motionX + a / d) / 2.0D;
		this.motionY = (this.motionY + b / d) / 2.0D;
		this.motionZ = (this.motionZ + c / d) / 2.0D;
		Math.atan2(a, c);
	}

	protected Entity findPlayer() {
		EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16.0D);
		return entityplayer != null && this.canEntityBeSeen(entityplayer) && !GuiMainMenu.mmactive ? entityplayer : null;
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setShort("LifeSpan", (short)this.lifeSpan);
		nbttagcompound.setShort("ShotTimer", (short)this.shotTimer);
		this.gotPlayer = this.dude != null;
		nbttagcompound.setBoolean("GotPlayer", this.gotPlayer);
		nbttagcompound.setBoolean("ToLeft", this.toLeft);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.lifeSpan = nbttagcompound.getShort("LifeSpan");
		this.shotTimer = nbttagcompound.getShort("ShotTimer");
		this.gotPlayer = nbttagcompound.getBoolean("GotPlayer");
		this.toLeft = nbttagcompound.getBoolean("ToLeft");
	}

	public void updatePlayerActionState() {
		super.updatePlayerActionState();
		--this.lifeSpan;
		if(this.lifeSpan <= 0) {
			this.spawnExplosionParticle();
			this.isDead = true;
		} else {
			if(this.shotTimer > 0) {
				--this.shotTimer;
			}

			if(this.gotPlayer && this.dude == null) {
				this.gotPlayer = false;
				this.dude = (EntityLiving)this.findPlayer();
			}

			if(this.dude != null && !this.dude.isDead) {
				this.getTargetPos();
				if(this.atShoulder()) {
					this.motionX *= 0.65D;
					this.motionY *= 0.65D;
					this.motionZ *= 0.65D;
					this.rotationYaw = this.dude.rotationYaw + (this.toLeft ? 1.0F : -1.0F);
					this.rotationPitch = this.dude.rotationPitch;
					if(this.shotTimer <= 0 && this.dude instanceof EntityPlayer && ((EntityPlayer)this.dude).isSwinging) {
						float spanish = this.rotationYaw - (this.toLeft ? 1.0F : -1.0F);
						double a = this.posX + Math.sin((double)spanish / -57.29577319531843D) * 1.6D;
						double b = this.posY - 0.25D;
						double c = this.posZ + Math.cos((double)spanish / -57.29577319531843D) * 1.6D;
						EntityFiroBall eh = new EntityFiroBall(this.worldObj, a, b, c, true, true);
						this.worldObj.entityJoinedWorld(eh);
						Vec3D vec3d = this.getLookVec();
						if(vec3d != null) {
							eh.smotionX = vec3d.xCoord * 1.5D;
							eh.smotionY = vec3d.yCoord * 1.5D;
							eh.smotionZ = vec3d.zCoord * 1.5D;
						}

						eh.smacked = true;
						this.worldObj.playSoundAtEntity(this, "mob.zephyr.zephyrshoot", 0.75F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
						this.shotTimer = 40;
					}
				} else {
					this.approachTarget();
				}

			} else {
				this.spawnExplosionParticle();
				this.isDead = true;
			}
		}
	}

	public boolean attackEntityFrom(Entity e, int i) {
		return e != null && e == this.dude ? false : super.attackEntityFrom(e, i);
	}
}
