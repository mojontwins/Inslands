package net.minecraft.game.entity.monster;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.EntityCreature;
import net.minecraft.game.world.World;

public class EntityMob extends EntityCreature {
	protected int attackStrength = 2;

	public EntityMob(World var1) {
		super(var1);
		this.health = 20;
	}

	public void updatePlayerActionState() {
		float var1 = this.getBrightness(1.0F);
		if(var1 > 0.5F) {
			this.entityAge += 2;
		}

		super.updatePlayerActionState();
	}

	public final void onUpdate() {
		super.onUpdate();
		if(this.worldObj.difficultySetting == 0) {
			this.setEntityDead();
		}

	}

	protected Entity findPlayerToAttack() {
		double var1 = this.worldObj.playerEntity.getDistanceToEntity(this);
		return var1 < 256.0D && this.updateEntityActionState(this.worldObj.playerEntity) ? this.worldObj.playerEntity : null;
	}

	public final boolean attackEntityFrom(Entity var1, int var2) {
		if(super.attackEntityFrom(var1, var2)) {
			if(var1 != this) {
				this.playerToAttack = var1;
			}

			return true;
		} else {
			return false;
		}
	}

	protected void attackEntity(Entity var1, float var2) {
		if((double)var2 < 2.5D && var1.boundingBox.maxY > this.boundingBox.minY && var1.boundingBox.minY < this.boundingBox.maxY) {
			this.attackTime = 20;
			var1.attackEntityFrom(this, this.attackStrength);
		}

	}

	protected float getBlockPathWeight(int var1, int var2, int var3) {
		return 0.5F - this.worldObj.getBrightness(var1, var2, var3);
	}
}
