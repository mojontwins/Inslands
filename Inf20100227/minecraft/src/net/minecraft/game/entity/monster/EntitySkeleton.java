package net.minecraft.game.entity.monster;

import net.minecraft.game.entity.Entity;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.item.Item;
import net.minecraft.game.world.World;
import util.MathHelper;

public class EntitySkeleton extends EntityMob {
	private EntitySkeleton(World var1) {
		super(var1);
	}

	public final void updatePlayerActionState() {
		super.updatePlayerActionState();
	}

	protected final void attackEntity(Entity var1, float var2) {
		if(var2 < 10.0F) {
			double var3 = var1.posX - this.posX;
			double var5 = var1.posZ - this.posZ;
			if(this.attackTime == 0) {
				EntityArrow var11 = new EntityArrow(this.worldObj, this);
				var11.posY += (double)1.4F;
				double var8 = var1.posY - (double)0.2F - var11.posY;
				float var10 = MathHelper.sqrt_double(var3 * var3 + var5 * var5) * 0.2F;
				this.worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
				var11.setArrowHeading(var3, var8 + (double)var10, var5, 0.6F, 12.0F);
				this.attackTime = 30;
			}

			this.rotationYaw = (float)(Math.atan2(var5, var3) * 180.0D / (double)((float)Math.PI)) - 90.0F;
			this.hasAttacked = true;
		}

	}

	protected final int getDropItemId() {
		return Item.arrow.shiftedIndex;
	}
}
