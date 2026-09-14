package net.minecraft.world.entity.animal.moc;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.Datawatchers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.item.EntityItem;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.stats.AchievementList;

public class EntityGoat extends EntityMocAnimalTameable {

	/*
	 * Adapted from https://github.com/DrZhark/mocreaturesdev/blob/master/src/drzhark/mocreatures/entity/passive/MoCEntityGoat.java
	 */

	public EntityLiving roper;
	private boolean bleat;
	private int attacking;
	private int movecount;
	private int earcount;
	private int tailcount;
	private int eatcount;
	private boolean swingLeg;
	private boolean swingEar;
	private boolean swingTail;
	private boolean eating;
	private int chargecount;
	private boolean hungry;
	private int bleatcount;
	private boolean looksWithInterest;

	public EntityGoat(World world) {
		super(world);
		this.setSize(1.4F, 0.9F);
		this.setEdad(70);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(Datawatchers.DW_UPSET, (byte) 0); // isUpset - 0 false 1 true
		this.dataWatcher.addObject(Datawatchers.DW_CHARGING, (byte) 0); // isCharging - 0 false 1 true

		this.selectType();
	}

	public void selectType() {
		/*
		 * type 1 = baby type 2 = female type 3 = female 2 type 4 = female 3 type 5 =
		 * male 1 type 6 = male 2 type 7 = male 3
		 */
		if (this.getType() == 0) {
			int roll = this.rand.nextInt(100);
			if (roll <= 15) {
				this.setType(1);
				this.setEdad(50);
			} else if (roll <= 30) {
				this.setType(2);
				this.setEdad(70);
			} else if (roll <= 45) {
				this.setType(3);
				this.setEdad(70);
			} else if (roll <= 60) {
				this.setType(4);
				this.setEdad(70);
			} else if (roll <= 75) {
				this.setType(5);
				this.setEdad(90);
			} else if (roll <= 90) {
				this.setType(6);
				this.setEdad(90);
			} else {
				this.setType(7);
				this.setEdad(90);
			}
		}
	}

	@Override
	public String getEntityTexture() {
		switch (this.getType()) {
		case 2:
			return "/mob/goat2.png";
		case 3:
			return "/mob/goat3.png";
		case 4:
			return "/mob/goat4.png";
		case 5:
			return "/mob/goat5.png";
		case 6:
			return "/mob/goat6.png";

		default:
			return "/mob/goat1.png";
		}
	}

	public void calm() {
		this.entityToAttack = null;
		this.setUpset(false);
		this.setCharging(false);
		this.moveSpeed = 0.7F;
		this.attacking = 0;
		this.chargecount = 0;
	}

	@Override
	protected void jump() {
		if (this.getType() == 1) {
			this.motionY = 0.41D;
		} else if (this.getType() < 5) {
			this.motionY = 0.45D;
		} else {
			this.motionY = 0.5D;
		}

		if (this.isSprinting()) {
			float yawRadians = this.rotationYaw * 0.01745329F;
			this.motionX -= MathHelper.sin(yawRadians) * 0.2F;
			this.motionZ += MathHelper.cos(yawRadians) * 0.2F;
		}
	}

	@Override
	protected void updateEntityActionState() {
		super.updateEntityActionState();

		Entity target = null;
		EntityPlayer closestPlayer = this.worldObj.getClosestPlayerToEntity(this, 8.0D);
		if (closestPlayer != null) {
			ItemStack heldItem = closestPlayer.inventory.getCurrentItem();

			// Start following player?
			if (heldItem != null && heldItem.itemID == Item.wheat.shiftedIndex) {
				target = closestPlayer;
			}
		}

		this.setTarget(target);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.rand.nextInt(100) == 0) {
			this.setSwingEar(true);
		}

		if (this.rand.nextInt(80) == 0) {
			this.setSwingTail(true);
		}

		if (this.rand.nextInt(50) == 0) {
			this.setEating(true);
		}

		if (this.hungry && this.rand.nextInt(20) == 0) {
			this.hungry = false;
		}

		if (this.getBleating()) {
			this.bleatcount++;
			if (this.bleatcount > 15) {
				this.bleatcount = 0;
				this.setBleating(false);
			}
		}

		if (!this.worldObj.isRemote && (this.getEdad() < 90 || this.getType() > 4 && this.getEdad() < 100)
				&& this.rand.nextInt(500) == 0) {
			this.setEdad(this.getEdad() + 1);
			if (this.getType() == 1 && this.getEdad() > 70) {
				this.setType(this.rand.nextInt(6) + 2);
			}
		}

		if (this.getUpset()) {
			this.attacking += this.rand.nextInt(4) + 2;
			if (this.attacking > 75) {
				this.attacking = 75;
			}

			if (this.rand.nextInt(500) == 0 || this.entityToAttack == null) {
				this.calm();
			}

			if (!this.getCharging() && this.rand.nextInt(35) == 0) {
				this.swingLeg();
			}

			if (!this.getCharging()) {
				this.setPathToEntity(null);
			}

			if (this.entityToAttack != null) {
				this.faceEntity(this.entityToAttack, 10F, 10F);
				if (this.rand.nextInt(80) == 0) {
					this.setCharging(true);
				}
			}
		}

		if (this.getCharging()) {
			this.chargecount++;
			if (this.chargecount > 120) {
				this.chargecount = 0;
				this.moveSpeed = 0.7F;
			} else {
				this.moveSpeed = 1.0F;
			}

			if (this.entityToAttack == null) {
				this.calm();
			}
		}

		if (!this.getUpset() && !this.getCharging()) {
			EntityPlayer nearbyPlayer = this.worldObj.getClosestPlayerToEntity(this, 24D);
			if (nearbyPlayer != null) {
				// Behaviour that happens only close to the player :)
				// Is there food around? Only check with the player nearby.
				EntityItem nearbyItem = MoCTools.getClosestEntityItem(this.worldObj, this, 10D);
				if (nearbyItem != null) {
					float distance = nearbyItem.getDistanceToEntity(this);
					if (distance > 2.0F) {
						int x = MathHelper.floor_double(nearbyItem.posX);
						int y = MathHelper.floor_double(nearbyItem.posY);
						int z = MathHelper.floor_double(nearbyItem.posZ);
						this.faceLocation(x, y, z, 30F);

						this.getMyOwnPath(nearbyItem, distance);
						return;
					}
					if (distance < 2.0F && this.deathTime == 0 && this.rand.nextInt(50) == 0) {
						this.worldObj.playSoundAtEntity(this, "mocreatures.goateating", 1.0F,
								1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
						this.setEating(true);
						nearbyItem.setEntityDead();
						return;
					}
				}

				// Find another goat to play!
				if (this.getType() > 4 && this.rand.nextInt(200) == 0) {
					EntityGoat targetGoat = (EntityGoat) this.getClosestEntityLiving(this, 14D);
					if (targetGoat != null) {
						this.setUpset(true);
						this.entityToAttack = targetGoat;
						targetGoat.setUpset(true);
						targetGoat.entityToAttack = this;
					}
				}

			}
		}

		if (this.hasCurrentTarget() && !this.hasPath()) {
			Entity currentTargetEntity = this.getCurrentTarget();
			if (currentTargetEntity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) currentTargetEntity;
				ItemStack heldItem = player.inventory.getCurrentItem();
				if (heldItem != null && heldItem.itemID == Item.wheat.shiftedIndex) {
					this.looksWithInterest = true;
				}
			}
		}
	}

	@Override
	public boolean isMyFavoriteFood(ItemStack itemStack) {
		Item item = itemStack != null ? itemStack.getItem() : null;
		return item != null && this.isItemEdible(item);
	}

	@Override
	public int getTalkInterval() {
		return this.hungry ? 20 : 120;
	}

	@Override
	public boolean entitiesToIgnore(Entity entity) {
		return !(entity instanceof EntityGoat) || ((EntityGoat) entity).getType() < 5 || ((EntityGoat) entity).roper != null;
	}

	@Override
	protected boolean isMovementCeased() {
		return this.getUpset() && !this.getCharging();
	}

	@Override
	protected void attackEntity(Entity entity, float distance) {
		if (this.attackTime <= 0 && distance < 3.0D && entity.boundingBox.maxY > this.boundingBox.minY
				&& entity.boundingBox.minY < this.boundingBox.maxY && this.attacking > 70) {
			this.attackTime = 30;
			this.attacking = 30;

			this.worldObj.playSoundAtEntity(this, "mocreatures.goatsmack", 1.0F,
					1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			if (entity instanceof EntityGoat) {
				MoCTools.bigsmack(this, entity, 0.4F);
				if (this.rand.nextInt(10) == 0) {
					this.calm();
					((EntityGoat) entity).calm();
				}
			} else {
				entity.attackEntityFrom(this, 3);
				MoCTools.bigsmack(this, entity, 0.8F);
				if (this.rand.nextInt(3) == 0) {
					this.calm();
				}
			}
		}
	}

	@Override
	public boolean isNotScared() {
		return this.getType() > 4;
	}

	private void swingLeg() {
		if (!this.getSwingLeg()) {
			this.setSwingLeg(true);
			this.movecount = 0;
		}
	}

	public boolean getUpset() {
		return this.dataWatcher.getWatchableObjectByte(Datawatchers.DW_UPSET) == 1;
	}

	public boolean getCharging() {
		return this.dataWatcher.getWatchableObjectByte(Datawatchers.DW_CHARGING) == 1;
	}

	public void setUpset(boolean upset) {
		this.dataWatcher.updateObject(Datawatchers.DW_UPSET, (byte) (upset ? 1 : 0));
	}

	public void setCharging(boolean charging) {
		this.dataWatcher.updateObject(Datawatchers.DW_CHARGING, (byte) (charging ? 1 : 0));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Upset", this.getUpset());
		nbttagcompound.setBoolean("Charging", this.getCharging());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setUpset(nbttagcompound.getBoolean("Upset"));
		this.setCharging(nbttagcompound.getBoolean("Charging"));
	}

	public boolean getBleating() {
		return this.bleat && this.getAttacking() == 0;
	}

	public void setBleating(boolean bleat) {
		this.bleat = bleat;
	}

	public int getAttacking() {
		return this.attacking;
	}

	public void setAttacking(int attacking) {
		this.attacking = attacking;
	}

	public int legMovement() {
		if (!this.getSwingLeg()) {
			return 0;
		}
		if (this.movecount < 21) {
			return this.movecount * -1;
		}
		if (this.movecount < 70) {
			return this.movecount - 40;
		}
		return -this.movecount + 100;
	}

	public int earMovement() {
		// 20 to 40 default = 30
		if (!this.getSwingEar()) {
			return 0;
		}
		if (this.earcount < 11) {
			return this.earcount + 30;
		}
		if (this.earcount < 31) {
			return -this.earcount + 50;
		}
		return this.earcount - 10;
	}

	public int tailMovement() {
		// 90 to -45
		if (!this.getSwingTail()) {
			return 90;
		}
		return this.tailcount - 45;
	}

	public int mouthMovement() {
		if (!this.getEating()) {
			return 0;
		}
		if (this.eatcount < 6) {
			return this.eatcount;
		}
		if (this.eatcount < 16) {
			return -this.eatcount + 10;
		}
		return this.eatcount - 20;
	}

	public boolean getSwingLeg() {
		return this.swingLeg;
	}

	public void setSwingLeg(boolean swingLeg) {
		this.swingLeg = swingLeg;
	}

	public boolean getSwingEar() {
		return this.swingEar;
	}

	public void setSwingEar(boolean swingEar) {
		this.swingEar = swingEar;
	}

	public boolean getSwingTail() {
		return this.swingTail;
	}

	public void setSwingTail(boolean swingTail) {
		this.swingTail = swingTail;
	}

	public boolean getEating() {
		return this.eating;
	}

	@Override
	public void setEating(boolean eating) {
		this.eating = eating;
	}

	@Override
	public boolean attackEntityFrom(Entity entity, int damage) {
		if (super.attackEntityFrom(entity, damage)) {
			if (entity != this && this.getType() > 4) {
				this.entityToAttack = entity;
				this.setUpset(true);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onUpdate() {
		if (this.getSwingLeg()) {
			this.movecount += 5;
			if (this.movecount == 30) {
				this.worldObj.playSoundAtEntity(this, "mocreatures.goatdigg", 1.0F,
						1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
			}
			if (this.movecount > 100) {
				this.setSwingLeg(false);
				this.movecount = 0;
			}
		}

		if (this.getSwingEar()) {
			this.earcount += 5;
			if (this.earcount > 40) {
				this.setSwingEar(false);
				this.earcount = 0;
			}
		}

		if (this.getSwingTail()) {
			this.tailcount += 15;
			if (this.tailcount > 135) {
				this.setSwingTail(false);
				this.tailcount = 0;
			}
		}

		if (this.getEating()) {
			this.eatcount += 1;
			if (this.eatcount == 2) {
				EntityPlayer nearbyPlayer = this.worldObj.getClosestPlayerToEntity(this, 3D);
				if (nearbyPlayer != null) {
					this.worldObj.playSoundAtEntity(this, "mocreatures.goateating", 1.0F,
							1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
				}
			}
			if (this.eatcount > 25) {
				this.setEating(false);
				this.eatcount = 0;
			}
		}

		super.onUpdate();

		if (this.looksWithInterest) {
			this.numTicksToChaseTarget = 10;
		}
	}

	@Override
	protected boolean fall(float distance) {
		return false;
	}

	@Override
	public boolean interact(EntityPlayer entityplayer) {
		if (super.interact(entityplayer)) {
			return false;
		}

		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (itemstack != null && itemstack.getItem() == Item.bucketEmpty) {
			if (this.getType() > 4) {
				this.setUpset(true);
				this.entityToAttack = entityplayer;
				return false;
			}
			if (this.getType() == 1) {
				return false;
			}

			entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem,
					new ItemStack(Item.bucketMilk));
			entityplayer.triggerAchievement(AchievementList.bucketMilkGoat);

			return true;
		}

		if (this.getIsTamed()) {
			if (itemstack != null && this.isItemEdible(itemstack.getItem())) {
				if (--itemstack.stackSize == 0) {
					entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
				}
				this.health = this.getFullHealth();
				this.worldObj.playSoundAtEntity(this, "mocreatures.goateating", 1.0F,
						1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
				return true;
			}
		}

		if (!this.worldObj.isRemote && !this.getIsTamed() && itemstack != null
				&& this.isItemEdible(itemstack.getItem())) {
			// TODO
			/*
			 * if (this.tameWithName(entityplayer, this)) { return true; }
			 */

			this.setIsTamed(true);
		}

		return false;
	}

	@Override
	protected String getHurtSound() {
		return "mocreatures.goathurt";
	}

	@Override
	protected String getLivingSound() {
		this.setBleating(true);
		if (this.getType() == 1) {
			return "mocreatures.goatkid";
		}
		if (this.getType() > 2 && this.getType() < 5) {
			return "mocreatures.goatfemale";
		}
		return "mocreatures.goatgrunt";
	}

	@Override
	protected String getDeathSound() {
		return "mocreatures.goatdying";
	}

	@Override
	protected int getDropItemId() {
		return Item.leather.shiftedIndex;
	}

}
