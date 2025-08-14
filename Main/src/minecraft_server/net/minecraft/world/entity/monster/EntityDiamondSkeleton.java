package net.minecraft.world.entity.monster;

import net.minecraft.src.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.World;

public class EntityDiamondSkeleton extends EntitySkeleton {

	public EntityDiamondSkeleton(World var1) {
		super(var1);
		this.texture = "/mob/diamond_skeleton.png";
		this.health = 800;
	}
    
    @Override
	protected void dropFewItems() {
    	// TODO : CHANGE!
		this.dropItem(Item.pirateSigil.shiftedIndex, 1);

	}
    
    @Override
	protected void attackEntity(Entity entity1, float f2) {
		if(f2 < 15.0F) {
			double d3 = entity1.posX - this.posX;
			double d5 = entity1.posZ - this.posZ;
			if(this.attackTime == 0) {
				EntityArrow entityArrow7 = new EntityArrow(this.worldObj, this);
				++entityArrow7.posY;
				double d8 = entity1.posY + (double)entity1.getEyeHeight() - (double)0.2F - entityArrow7.posY;
				float f10 = MathHelper.sqrt_double(d3 * d3 + d5 * d5) * 0.2F;
				this.worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (this.rand.nextFloat() * 0.4F + 0.8F));
				this.worldObj.spawnEntityInWorld(entityArrow7);
				entityArrow7.setArrowHeading(d3, d8 + (double)f10, d5, 0.6F, 12.0F);
				this.attackTime = 25;
			}

			this.rotationYaw = (float)(Math.atan2(d5, d3) * 180.0D / (double)(float)Math.PI) - 90.0F;
			this.hasAttacked = true;
		}

	}
}
