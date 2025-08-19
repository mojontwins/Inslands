package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.item.Item;
import net.minecraft.game.world.World;

import util.MathHelper;

public class EntitySkeleton extends EntityMonster {
	public EntitySkeleton(World world1) {
		super(world1);
		this.texture = "/mob/skeleton.png";
	}

	public final void onLivingUpdate() {
		float f1;
		if(this.worldObj.isDaytime() && (f1 = this.getEntityBrightness(1.0F)) > 0.5F && this.worldObj.canBlockSeeTheSky((int)this.posX, (int)this.posY, (int)this.posZ) && this.rand.nextFloat() * 30.0F < (f1 - 0.4F) * 2.0F) {
			this.fire = 300;
		}

		super.onLivingUpdate();
	}

	protected final void attackEntity(Entity entity1, float f2) {
		if(f2 < 10.0F) {
			double d3 = entity1.posX - this.posX;
			double d5 = entity1.posZ - this.posZ;
			if(this.attackTime == 0) {
				EntityArrow entityArrow11;
				++(entityArrow11 = new EntityArrow(this.worldObj, this)).posY;
				double d8 = entity1.posY - (double)0.2F - entityArrow11.posY;
				float f10 = MathHelper.sqrt_double(d3 * d3 + d5 * d5) * 0.2F;
				this.worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
				this.worldObj.spawnEntityInWorld(entityArrow11);
				entityArrow11.setArrowHeading(d3, d8 + (double)f10, d5, 0.6F, 12.0F);
				this.attackTime = 30;
			}

			this.rotationYaw = (float)(Math.atan2(d5, d3) * 180.0D / (double)(float)Math.PI) - 90.0F;
			this.hasAttacked = true;
		}

	}

	public final void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
	}

	public final void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
	}

	protected final int getDroppedItem() {
		return Item.arrow.shiftedIndex;
	}
}