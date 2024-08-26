package net.minecraft.game.entity.monster;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.item.Item;
import net.minecraft.game.world.World;
import util.MathHelper;

public class EntitySpider extends EntityMob {
	private EntitySpider(World var1) {
		super(var1);
	}

	protected final Entity findPlayerToAttack() {
		float var1 = this.getBrightness(1.0F);
		if(var1 < 0.5F) {
			double var2 = this.worldObj.playerEntity.getDistanceToEntity(this);
			if(var2 < 256.0D) {
				return this.worldObj.playerEntity;
			}
		}

		return null;
	}

	protected final void attackEntity(Entity var1, float var2) {
		float var3 = this.getBrightness(1.0F);
		if(var3 > 0.5F && this.rand.nextInt(100) == 0) {
			this.playerToAttack = null;
		} else {
			if(var2 > 2.0F && var2 < 6.0F && this.rand.nextInt(10) == 0) {
				if(this.onGround) {
					double var4 = var1.posX - this.posX;
					double var6 = var1.posZ - this.posZ;
					float var8 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
					this.motionX = var4 / (double)var8 * 0.5D * (double)0.8F + this.motionX * (double)0.2F;
					this.motionZ = var6 / (double)var8 * 0.5D * (double)0.8F + this.motionZ * (double)0.2F;
					this.motionY = (double)0.4F;
					return;
				}
			} else {
				super.attackEntity(var1, var2);
			}

		}
	}

	protected final int getDropItemId() {
		return Item.silk.shiftedIndex;
	}
}
