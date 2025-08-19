package net.minecraft.game.entity.monster;

import com.mojang.nbt.NBTTagCompound;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityCreature;
import net.minecraft.game.world.World;

public class EntityMonster extends EntityCreature {
	protected int attackStrength = 2;

	public EntityMonster(World world1) {
		super(world1);
		this.health = 20;
	}

	public void onLivingUpdate() {
		if(this.getEntityBrightness(1.0F) > 0.5F) {
			this.entityAge += 2;
		}

		super.onLivingUpdate();
	}

	public final void onUpdate() {
		super.onUpdate();
		if(this.worldObj.difficultySetting == 0) {
			super.isDead = true;
		}

	}

	protected Entity findPlayerToAttack() {
		return this.worldObj.playerEntity.getDistanceSqToEntity(this) < 256.0D && this.canEntityBeSeen(this.worldObj.playerEntity) ? this.worldObj.playerEntity : null;
	}

	public final boolean attackEntityFrom(Entity entity1, int i2) {
		if(super.attackEntityFrom(entity1, i2)) {
			if(entity1 != this) {
				this.playerToAttack = entity1;
			}

			return true;
		} else {
			return false;
		}
	}

	protected void attackEntity(Entity entity1, float f2) {
		if((double)f2 < 2.5D && entity1.boundingBox.maxY > this.boundingBox.minY && entity1.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			entity1.attackEntityFrom(this, this.attackStrength);
		}

	}

	protected float getBlockPathWeight(int i1, int i2, int i3) {
		return 0.5F - this.worldObj.getBrightness(i1, i2, i3);
	}

	public void writeEntityToNBT(NBTTagCompound nBTTagCompound1) {
		super.writeEntityToNBT(nBTTagCompound1);
	}

	public void readEntityFromNBT(NBTTagCompound nBTTagCompound1) {
		super.readEntityFromNBT(nBTTagCompound1);
	}

	public final boolean getCanSpawnHere(float f1, float f2, float f3) {
		return this.worldObj.getBlockLightValue((int)f1, (int)f2, (int)f3) <= this.rand.nextInt(8) && super.getCanSpawnHere(f1, f2, f3);
	}
}