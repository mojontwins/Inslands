package net.minecraft.src;

public class EntityZephyr extends EntityFlying implements IMob {
	private long checkTime = 0L;
	private final long checkTimeInterval = 3000L;
	private double checkX = 0.0D;
	private double checkY = 0.0D;
	private double checkZ = 0.0D;
	private final double minTraversalDist = 3.0D;
	private boolean isStuckWarning = false;
	public int courseChangeCooldown = 0;
	public double waypointX;
	public double waypointY;
	public double waypointZ;
	private Entity targetedEntity = null;
	private int aggroCooldown = 0;
	public int prevAttackCounter = 0;
	public int attackCounter = 0;

	public EntityZephyr(World world) {
		super(world);
		this.texture = "/aether/mobs/Zephyr.png";
		this.setSize(4.0F, 4.0F);
	}

	protected void updatePlayerActionState() {
		if(this.posY < -2.0D || this.posY > 130.0D) {
			this.func_27021_X();
		}

		this.prevAttackCounter = this.attackCounter;
		double d = this.waypointX - this.posX;
		double d1 = this.waypointY - this.posY;
		double d2 = this.waypointZ - this.posZ;
		double d3 = (double)MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
		if(d3 < 1.0D || d3 > 60.0D) {
			this.waypointX = this.posX + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.waypointY = this.posY + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
			this.waypointZ = this.posZ + (double)((this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F);
		}

		if(this.courseChangeCooldown-- <= 0) {
			this.courseChangeCooldown += this.rand.nextInt(5) + 2;
			if(this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3)) {
				this.motionX += d / d3 * 0.1D;
				this.motionY += d1 / d3 * 0.1D;
				this.motionZ += d2 / d3 * 0.1D;
			} else {
				this.waypointX = this.posX;
				this.waypointY = this.posY;
				this.waypointZ = this.posZ;
			}
		}

		if(this.targetedEntity != null && this.targetedEntity.isDead) {
			this.targetedEntity = null;
		}

		if(this.targetedEntity == null || this.aggroCooldown-- <= 0) {
			this.targetedEntity = this.worldObj.getClosestPlayerToEntity(this, 100.0D);
			if(this.targetedEntity != null) {
				this.aggroCooldown = 20;
			}
		}

		double d4 = 64.0D;
		if(this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < d4 * d4) {
			double d5 = this.targetedEntity.posX - this.posX;
			double d6 = this.targetedEntity.boundingBox.minY + (double)(this.targetedEntity.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
			double d7 = this.targetedEntity.posZ - this.posZ;
			this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(d5, d7)) * 180.0F / 3.141593F;
			if(this.canEntityBeSeen(this.targetedEntity)) {
				if(this.attackCounter == 10) {
					this.worldObj.playSoundAtEntity(this, "aether.sound.mobs.zephyr.zephyrCall", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
				}

				++this.attackCounter;
				if(this.attackCounter == 20) {
					this.worldObj.playSoundAtEntity(this, "aether.sound.mobs.zephyr.zephyrShoot", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
					EntityZephyrSnowball entitysnowball = new EntityZephyrSnowball(this.worldObj, this, d5, d6, d7);
					double d8 = 4.0D;
					Vec3D vec3d = this.getLook(1.0F);
					entitysnowball.posX = this.posX + vec3d.xCoord * d8;
					entitysnowball.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
					entitysnowball.posZ = this.posZ + vec3d.zCoord * d8;
					this.worldObj.entityJoinedWorld(entitysnowball);
					this.attackCounter = -40;
				}
			} else if(this.attackCounter > 0) {
				--this.attackCounter;
			}
		} else {
			this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / 3.141593F;
			if(this.attackCounter > 0) {
				--this.attackCounter;
			}
		}

		this.texture = this.attackCounter <= 10 ? "/aether/mobs/Zephyr.png" : "/aether/mobs/Zephyr.png";
		if(!this.worldObj.multiplayerWorld && this.worldObj.difficultySetting == 0) {
			this.setEntityDead();
		}

		this.checkForBeingStuck();
	}

	private boolean isCourseTraversable(double d, double d1, double d2, double d3) {
		double d4 = (this.waypointX - this.posX) / d3;
		double d5 = (this.waypointY - this.posY) / d3;
		double d6 = (this.waypointZ - this.posZ) / d3;
		AxisAlignedBB axisalignedbb = this.boundingBox.copy();

		for(int i = 1; (double)i < d3; ++i) {
			axisalignedbb.offset(d4, d5, d6);
			if(this.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).size() > 0) {
				return false;
			}
		}

		return true;
	}

	protected String getLivingSound() {
		return "aether.sound.mobs.zephyr.zephyrCall";
	}

	protected String getHurtSound() {
		return "aether.sound.mobs.zephyr.zephyrCall";
	}

	protected String getDeathSound() {
		return "aether.sound.mobs.zephyr.zephyrCall";
	}

	protected int getDropItemId() {
		return AetherBlocks.Aercloud.blockID;
	}

	public boolean canDespawn() {
		return true;
	}

	protected float getSoundVolume() {
		return 3.0F;
	}

	private void checkForBeingStuck() {
		long curtime = System.currentTimeMillis();
		if(curtime > this.checkTime + 3000L) {
			double diffx = this.posX - this.checkX;
			double diffy = this.posY - this.checkY;
			double diffz = this.posZ - this.checkZ;
			double distanceTravelled = Math.sqrt(diffx * diffx + diffy * diffy + diffz * diffz);
			if(distanceTravelled < 3.0D) {
				if(!this.isStuckWarning) {
					this.isStuckWarning = true;
				} else {
					this.setEntityDead();
				}
			}

			this.checkX = this.posX;
			this.checkY = this.posY;
			this.checkZ = this.posZ;
			this.checkTime = curtime;
		}

	}

	public boolean getCanSpawnHere() {
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.boundingBox.minY);
		int k = MathHelper.floor_double(this.posZ);
		return this.rand.nextInt(85) == 0 && this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox) && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.DungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LightDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LockedDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LockedLightDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.Holystone.blockID && this.worldObj.difficultySetting > 0;
	}

	public int getMaxSpawnedInChunk() {
		return 1;
	}
}
