package net.minecraft.src;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityCloudParachute extends Entity {
	private EntityLiving entityUsing;
	private boolean justServerSpawned;
	private static Map cloudMap = new HashMap();
	private static final float MAX_FALL_SPEED = 0.25F;
	private static final double ANIM_RADIUS = 0.75D;
	public boolean gold;

	public EntityCloudParachute(World world) {
		super(world);
		this.justServerSpawned = false;
		this.setSize(1.0F, 1.0F);
	}

	public EntityCloudParachute(World world, double d, double d1, double d2) {
		this(world);
		this.setPositionAndRotation(d, d1, d2, this.rotationYaw, this.rotationPitch);
		this.justServerSpawned = true;
	}

	public EntityCloudParachute(World world, EntityLiving entityliving, boolean bool) {
		this(world);
		if(entityliving == null) {
			throw new IllegalArgumentException("entityliving cannot be null.");
		} else {
			this.entityUsing = entityliving;
			cloudMap.put(entityliving, this);
			this.prevPosX = this.posX;
			this.prevPosY = this.posY;
			this.prevPosZ = this.posZ;
			this.moveToEntityUsing();
			this.gold = bool;
		}
	}

	public static EntityCloudParachute getCloudBelongingToEntity(EntityLiving entity) {
		return (EntityCloudParachute)cloudMap.get(entity);
	}

	public World getWorld() {
		return this.worldObj;
	}

	public void die() {
		if(this.entityUsing != null) {
			if(cloudMap.containsKey(this.entityUsing)) {
				cloudMap.remove(this.entityUsing);
			}

			for(int i = 0; i < 32; ++i) {
				doCloudSmoke(this.worldObj, this.entityUsing);
			}

			this.worldObj.playSoundAtEntity(this.entityUsing, "cloud", 1.0F, 1.0F / (this.rand.nextFloat() * 0.1F + 0.95F));
		}

		this.entityUsing = null;
		this.isDead = true;
	}

	public static void doCloudSmoke(World world, EntityLiving entityliving) {
		double x = entityliving.posX + entityliving.rand.nextDouble() * 0.75D * 2.0D - 0.75D;
		double y = entityliving.boundingBox.minY - 0.5D + entityliving.rand.nextDouble() * 0.75D * 2.0D - 0.75D;
		double z = entityliving.posZ + entityliving.rand.nextDouble() * 0.75D * 2.0D - 0.75D;
		ModLoader.getMinecraftInstance().effectRenderer.addEffect(new EntityCloudSmokeFX(world, x, y, z, 0.0D, 0.0D, 0.0D, 2.5F, 1.0F, 1.0F, 1.0F));
	}

	public static boolean entityHasRoomForCloud(World world, EntityLiving entityliving) {
		AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(entityliving.posX - 0.5D, entityliving.boundingBox.minY - 1.0D, entityliving.posZ - 0.5D, entityliving.posX + 0.5D, entityliving.boundingBox.minY, entityliving.posZ + 0.5D);
		return world.getCollidingBoundingBoxes(entityliving, boundingBox).size() == 0 && !world.isAABBInMaterial(boundingBox, Material.water);
	}

	protected void entityInit() {
	}

	public boolean isInRangeToRenderDist(double d) {
		return this.entityUsing != null ? this.entityUsing.isInRangeToRenderDist(d) : super.isInRangeToRenderDist(d);
	}

	public boolean canBeCollidedWith() {
		return true;
	}

	public AxisAlignedBB getBoundingBox() {
		return this.boundingBox;
	}

	public void onUpdate() {
		if(!this.isDead) {
			if(this.entityUsing == null) {
				if(this.worldObj.multiplayerWorld && !this.justServerSpawned) {
					this.die();
					return;
				}

				this.justServerSpawned = false;
				this.entityUsing = this.findUser();
				if(this.entityUsing == null) {
					this.die();
					return;
				}

				cloudMap.put(this.entityUsing, this);
			}

			if(this.entityUsing.motionY < -0.25D) {
				this.entityUsing.motionY = -0.25D;
			}

			this.entityUsing.fallDistance = 0.0F;
			doCloudSmoke(this.worldObj, this.entityUsing);
			this.moveToEntityUsing();
		}
	}

	private EntityLiving findUser() {
		List entities = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, this.boundingBox.copy().offset(0.0D, 1.0D, 0.0D));
		double minDeltaSquared = -1.0D;
		EntityLiving entityliving = null;

		for(int i = 0; i < entities.size(); ++i) {
			EntityLiving entitylivingtemp = (EntityLiving)entities.get(i);
			if(entitylivingtemp.isEntityAlive()) {
				double xDelta = this.posX - entitylivingtemp.posX;
				double yDelta = this.boundingBox.maxY - entitylivingtemp.boundingBox.minY;
				double zDelta = this.posZ - entitylivingtemp.posZ;
				double deltaSquared = xDelta * xDelta + yDelta * yDelta + zDelta * zDelta;
				if(minDeltaSquared == -1.0D || deltaSquared < minDeltaSquared) {
					minDeltaSquared = deltaSquared;
					entityliving = entitylivingtemp;
				}
			}
		}

		return entityliving;
	}

	private void moveToEntityUsing() {
		this.setPositionAndRotation(this.entityUsing.posX, this.entityUsing.boundingBox.minY - (double)(this.height / 2.0F), this.entityUsing.posZ, this.entityUsing.rotationYaw, this.entityUsing.rotationPitch);
		this.motionX = this.entityUsing.motionX;
		this.motionY = this.entityUsing.motionY;
		this.motionZ = this.entityUsing.motionZ;
		this.rotationYaw = this.entityUsing.rotationYaw;
		if(this.isCollided()) {
			this.die();
		}

	}

	private boolean isCollided() {
		return this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).size() > 0 || this.worldObj.isAABBInMaterial(this.boundingBox, Material.water);
	}

	public void onCollideWithPlayer(EntityPlayer entityplayer) {
	}

	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}
}
