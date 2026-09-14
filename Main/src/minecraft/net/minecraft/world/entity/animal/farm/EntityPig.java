package net.minecraft.world.entity.animal.farm;

import com.mojang.nbt.NBTTagCompound;
import net.minecraft.world.entity.EntityLightningBolt;
import net.minecraft.world.entity.animal.EntityAnimal;
import net.minecraft.world.entity.mob.undead.EntityPigZombie;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.World;
import net.minecraft.world.stats.AchievementList;

public class EntityPig extends EntityAnimal {
	public EntityPig(World world) {
		super(world);
		this.texture = "/mob/pig.png";
		this.setSize(0.9F, 0.9F);
	}

	@Override
	protected void entityInit() {
		this.dataWatcher.addObject(16, (byte) 0);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Saddle", this.getSaddled());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.setSaddled(nbttagcompound.getBoolean("Saddle"));
	}

	@Override
	protected String getLivingSound() {
		return "mob.pig";
	}

	@Override
	protected String getHurtSound() {
		return "mob.pig";
	}

	@Override
	protected String getDeathSound() {
		return "mob.pigdeath";
	}

	@Override
	public boolean interact(EntityPlayer player) {
		if (super.interact(player)) {
			return false;
		}

		if (this.getSaddled() && !this.worldObj.isRemote && (this.riddenByEntity == null || this.riddenByEntity == player)) {
			player.mountEntity(this);
			return true;
		}
		return false;
	}

	@Override
	protected int getDropItemId() {
		return this.fire > 0 ? Item.porkCooked.shiftedIndex : Item.porkRaw.shiftedIndex;
	}

	public boolean getSaddled() {
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setSaddled(boolean saddled) {
		this.dataWatcher.updateObject(16, (byte) (saddled ? 1 : 0));
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt lightning) {
		if (!this.worldObj.isRemote) {
			EntityPigZombie pigZombie = new EntityPigZombie(this.worldObj);
			pigZombie.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.worldObj.spawnEntityInWorld(pigZombie);
			this.setEntityDead();
		}
	}

	@Override
	protected boolean fall(float distance) {
		boolean rebound = super.fall(distance);
		if (distance > 5.0F && this.riddenByEntity instanceof EntityPlayer) {
			((EntityPlayer) this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
		}
		return rebound;
	}
}
