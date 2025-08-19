package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.item.Item;
import net.minecraft.game.world.World;

import util.MathHelper;

public class EntitySpider extends EntityMonster {
	public EntitySpider(World world1) {
		super(world1);
		this.texture = "/mob/spider.png";
		this.setSize(1.4F, 0.9F);
		this.moveSpeed = 0.8F;
	}

	protected final Entity findPlayerToAttack() {
		return this.getEntityBrightness(1.0F) < 0.5F && this.worldObj.playerEntity.getDistanceSqToEntity(this) < 256.0D ? this.worldObj.playerEntity : null;
	}

	protected final void attackEntity(Entity entity1, float f2) {
		if(this.getEntityBrightness(1.0F) > 0.5F && this.rand.nextInt(100) == 0) {
			this.playerToAttack = null;
		} else {
			if(f2 > 2.0F && f2 < 6.0F && this.rand.nextInt(10) == 0) {
				if(this.onGround) {
					double d4 = entity1.posX - this.posX;
					double d6 = entity1.posZ - this.posZ;
					float f8 = MathHelper.sqrt_double(d4 * d4 + d6 * d6);
					this.motionX = d4 / (double)f8 * 0.5D * (double)0.8F + this.motionX * (double)0.2F;
					this.motionZ = d6 / (double)f8 * 0.5D * (double)0.8F + this.motionZ * (double)0.2F;
					this.motionY = (double)0.4F;
					return;
				}
			} else {
				super.attackEntity(entity1, f2);
			}

		}
	}

	public final void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
	}

	public final void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
	}

	protected final int getDroppedItem() {
		return Item.silk.shiftedIndex;
	}
}