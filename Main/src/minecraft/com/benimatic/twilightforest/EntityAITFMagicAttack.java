package com.benimatic.twilightforest;

import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.EntityAIBase;

public class EntityAITFMagicAttack extends EntityAIBase {
	World worldObj;
	EntityLiving entityHost;
	EntityLiving attackTarget;
	int rangedAttackTime = 0;
	float field_48370_e;
	int field_48367_f = 0;
	int rangedAttackID;
	int maxRangedAttackTime;

	public EntityAITFMagicAttack(EntityLiving par1EntityLiving, float par2, int par3, int par4) {
		this.entityHost = par1EntityLiving;
		this.worldObj = par1EntityLiving.worldObj;
		this.field_48370_e = par2;
		this.rangedAttackID = par3;
		this.maxRangedAttackTime = par4;
		this.setMutexBits(3);
	}

	public boolean shouldExecute() {
		EntityLiving var1 = this.entityHost.getAttackTarget();
		if(var1 == null) {
			return false;
		} else {
			this.attackTarget = var1;
			return true;
		}
	}

	public boolean continueExecuting() {
		return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
	}

	public void resetTask() {
		this.attackTarget = null;
	}

	public void updateTask() {
		double var1 = 100.0D;
		double var3 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ);
		boolean var5 = this.entityHost.getEntitySenses().canSee(this.attackTarget);
		if(var5) {
			++this.field_48367_f;
		} else {
			this.field_48367_f = 0;
		}

		if(var3 <= var1 && this.field_48367_f >= 20) {
			this.entityHost.getNavigator().clearPathEntity();
		} else {
			this.entityHost.getNavigator().getPathToEntityWithSpeed(this.attackTarget, this.field_48370_e);
		}

		this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
		this.rangedAttackTime = Math.max(this.rangedAttackTime - 1, 0);
		if(this.rangedAttackTime <= 0 && var3 <= var1 && var5) {
			this.doRangedAttack();
			this.rangedAttackTime = this.maxRangedAttackTime;
		}

	}

	protected void doRangedAttack() {
		if(this.rangedAttackID == 1) {
			EntityTFNatureBolt projectile = new EntityTFNatureBolt(this.worldObj, this.entityHost);
			double tx = this.attackTarget.posX - this.entityHost.posX;
			double ty = this.attackTarget.posY + (double)this.attackTarget.getEyeHeight() - 1.100000023841858D - projectile.posY;
			double tz = this.attackTarget.posZ - this.entityHost.posZ;
			float heightOffset = MathHelper.sqrt_double(tx * tx + tz * tz) * 0.2F;
			projectile.setThrowableHeading(tx, ty + (double)heightOffset, tz, 0.6F, 6.0F);
			this.worldObj.playSoundAtEntity(this.entityHost, "mob.ghast.fireball", 1.0F, 1.0F / (this.entityHost.getRNG().nextFloat() * 0.4F + 0.8F));
			this.worldObj.spawnEntityInWorld(projectile);
		}

	}
}
