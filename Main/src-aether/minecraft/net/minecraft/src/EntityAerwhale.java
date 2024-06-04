package net.minecraft.src;

public class EntityAerwhale extends EntityFlying implements IMob {
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
	private int aggroCooldown;
	public int prevAttackCounter;
	public int attackCounter;
	public double motionYaw;
	public double motionPitch;

	public EntityAerwhale(World world) {
		super(world);
		this.isImmuneToFire = true;
		this.aggroCooldown = 0;
		this.prevAttackCounter = 0;
		this.attackCounter = 0;
		this.texture = "/aether/mobs/Mob_Aerwhale.png";
		this.setSize(4.0F, 4.0F);
		this.moveSpeed = 0.5F;
		this.health = 20;
		this.rotationYaw = 360.0F * this.rand.nextFloat();
		this.rotationPitch = 90.0F * this.rand.nextFloat() - 45.0F;
		this.ignoreFrustumCheck = true;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, (byte)0);
	}

	public void onLivingUpdate() {
	}

	public void onUpdate() {
		byte byte0 = this.dataWatcher.getWatchableObjectByte(16);
		this.texture = byte0 != 1 ? "/aether/mobs/Mob_Aerwhale.png" : "/aether/mobs/Mob_Aerwhale.png";
		double[] distances = new double[]{this.openSpace(0.0F, 0.0F), this.openSpace(45.0F, 0.0F), this.openSpace(0.0F, 45.0F), this.openSpace(-45.0F, 0.0F), this.openSpace(0.0F, -45.0F)};
		int longest = 0;

		int i;
		for(i = 1; i < 5; ++i) {
			if(distances[i] > distances[longest]) {
				longest = i;
			}
		}

		switch(longest) {
		case 0:
			if(distances[0] == 50.0D) {
				this.motionYaw *= (double)0.9F;
				this.motionPitch *= (double)0.9F;
				if(this.posY > 100.0D) {
					this.motionPitch -= 2.0D;
				}

				if(this.posY < 20.0D) {
					this.motionPitch += 2.0D;
				}
			} else {
				this.rotationPitch = -this.rotationPitch;
				this.rotationYaw = -this.rotationYaw;
			}
			break;
		case 1:
			this.motionYaw += 5.0D;
			break;
		case 2:
			this.motionPitch -= 5.0D;
			break;
		case 3:
			this.motionYaw -= 5.0D;
			break;
		case 4:
			this.motionPitch += 5.0D;
		}

		this.motionYaw += (double)(2.0F * this.rand.nextFloat() - 1.0F);
		this.motionPitch += (double)(2.0F * this.rand.nextFloat() - 1.0F);
		this.rotationPitch = (float)((double)this.rotationPitch + 0.1D * this.motionPitch);
		this.rotationYaw = (float)((double)this.rotationYaw + 0.1D * this.motionYaw);
		if(this.rotationPitch < -60.0F) {
			this.rotationPitch = -60.0F;
		}

		if(this.rotationPitch > 60.0F) {
			this.rotationPitch = 60.0F;
		}

		this.rotationPitch = (float)((double)this.rotationPitch * 0.99D);
		this.motionX += 0.005D * Math.cos((double)this.rotationYaw / 180.0D * Math.PI) * Math.cos((double)this.rotationPitch / 180.0D * Math.PI);
		this.motionY += 0.005D * Math.sin((double)this.rotationPitch / 180.0D * Math.PI);
		this.motionZ += 0.005D * Math.sin((double)this.rotationYaw / 180.0D * Math.PI) * Math.cos((double)this.rotationPitch / 180.0D * Math.PI);
		this.motionX *= 0.98D;
		this.motionY *= 0.98D;
		this.motionZ *= 0.98D;
		i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.boundingBox.minY);
		int k = MathHelper.floor_double(this.posZ);
		if(this.motionX > 0.0D && this.worldObj.getBlockId(i + 1, j, k) != 0) {
			this.motionX = -this.motionX;
			this.motionYaw -= 10.0D;
		} else if(this.motionX < 0.0D && this.worldObj.getBlockId(i - 1, j, k) != 0) {
			this.motionX = -this.motionX;
			this.motionYaw += 10.0D;
		}

		if(this.motionY > 0.0D && this.worldObj.getBlockId(i, j + 1, k) != 0) {
			this.motionY = -this.motionY;
			this.motionPitch -= 10.0D;
		} else if(this.motionY < 0.0D && this.worldObj.getBlockId(i, j - 1, k) != 0) {
			this.motionY = -this.motionY;
			this.motionPitch += 10.0D;
		}

		if(this.motionZ > 0.0D && this.worldObj.getBlockId(i, j, k + 1) != 0) {
			this.motionZ = -this.motionZ;
			this.motionYaw -= 10.0D;
		} else if(this.motionZ < 0.0D && this.worldObj.getBlockId(i, j, k - 1) != 0) {
			this.motionZ = -this.motionZ;
			this.motionYaw += 10.0D;
		}

		this.fire = 0;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.checkForBeingStuck();
	}

	public double getSpeed() {
		return Math.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
	}

	private double openSpace(float rotationYawOffset, float rotationPitchOffset) {
		float yaw = this.rotationYaw + rotationYawOffset;
		float pitch = this.rotationYaw + rotationYawOffset;
		Vec3D vec3d = Vec3D.createVector(this.posX, this.posY, this.posZ);
		float f3 = MathHelper.cos(-yaw * 0.01745329F - 3.141593F);
		float f4 = MathHelper.sin(-yaw * 0.01745329F - 3.141593F);
		float f5 = MathHelper.cos(-pitch * 0.01745329F);
		float f6 = MathHelper.sin(-pitch * 0.01745329F);
		float f7 = f4 * f5;
		float f9 = f3 * f5;
		double d3 = 50.0D;
		Vec3D vec3d1 = vec3d.addVector((double)f7 * d3, (double)f6 * d3, (double)f9 * d3);
		MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks_do(vec3d, vec3d1, true);
		if(movingobjectposition == null) {
			return 50.0D;
		} else if(movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
			double i = (double)movingobjectposition.blockX - this.posX;
			double j = (double)movingobjectposition.blockY - this.posY;
			double k = (double)movingobjectposition.blockZ - this.posZ;
			return Math.sqrt(i * i + j * j + k * k);
		} else {
			return 50.0D;
		}
	}

	protected void updatePlayerActionState() {
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
		return "aether.sound.mobs.aerwhale.aerwhaleCall";
	}

	protected String getHurtSound() {
		return "aether.sound.mobs.aerwhale.aerwhaleDeath";
	}

	protected String getDeathSound() {
		return "aether.sound.mobs.aerwhale.aerwhaleDeath";
	}

	protected float getSoundVolume() {
		return 3.0F;
	}

	public int getMaxSpawnedInChunk() {
		return 1;
	}

	public boolean canDespawn() {
		return true;
	}

	public boolean getCanSpawnHere() {
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.boundingBox.minY);
		int k = MathHelper.floor_double(this.posZ);
		return this.rand.nextInt(65) == 0 && this.worldObj.checkIfAABBIsClear(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() == 0 && !this.worldObj.getIsAnyLiquid(this.boundingBox) && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.DungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LightDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LockedDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.LockedLightDungeonStone.blockID && this.worldObj.getBlockId(i, j - 1, k) != AetherBlocks.Holystone.blockID;
	}
}
