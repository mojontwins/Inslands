package net.minecraft.world.entity.projectile;

import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.monster.EntitySnowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemBottle;
import net.minecraft.world.level.World;

public class EntityThrowableBottle extends EntitySnowball {
	public ItemBottle itemBottle;
	private static Item bottlesMap[] = new Item[] {
			Item.bottleEmpty,
			Item.bottleWater,
			Item.bottleSoup,
			Item.bottleAcid,
			Item.bottleGoo,
			Item.bottlePoison
	};

	public EntityThrowableBottle(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
	}

	public EntityThrowableBottle(World world, double xPos, double yPos, double zPos, ItemBottle itemBottle) {
		super(world, xPos, yPos, zPos);
		this.itemBottle = itemBottle;
	}
	
	public EntityThrowableBottle(World world, double xPos, double yPos, double zPos, int bottleAsIndex) {
		super(world, xPos, yPos, zPos);
		this.itemBottle = (ItemBottle) EntityThrowableBottle.bottlesMap[bottleAsIndex];
	}

	public EntityThrowableBottle(World world, EntityLiving entityLiving, ItemBottle itemBottle) {
		super(world, entityLiving);
		this.itemBottle = itemBottle;
	}

	public int getBottleAsIndex() {
		for(int i = 0; i < EntityThrowableBottle.bottlesMap.length; i ++) {
			if(EntityThrowableBottle.bottlesMap[i] == this.itemBottle) {
				return i;
			}
		}
		
		return 0;
	}
	
	public void setVelocity(double d1, double d3, double d5) {
		this.motionX = d1;
		this.motionY = d3;
		this.motionZ = d5;
		if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f7 = MathHelper.sqrt_double(d1 * d1 + d5 * d5);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(d1, d5) * 180.0D / (double)(float)Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(d3, (double)f7) * 180.0D / (double)(float)Math.PI);
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}

	}
	
	public void throwableHitEntity(MovingObjectPosition movingObjectPosition3) {
		if(
			movingObjectPosition3.entityHit instanceof EntityLiving && 
			movingObjectPosition3.entityHit != null &&
			this.itemBottle != null
		) {
			EntityLiving entityHit = (EntityLiving)movingObjectPosition3.entityHit;
			entityHit.attackEntityFrom(this.thrower, this.itemBottle.damage);
		}

		for(int i8 = 0; i8 < 8; ++i8) {
			this.worldObj.spawnParticle("crackedpotion", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
		}

		this.setEntityDead();
		
	}
}
