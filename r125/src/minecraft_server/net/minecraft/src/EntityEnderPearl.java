package net.minecraft.src;

public class EntityEnderPearl extends EntityThrowable {
	public EntityEnderPearl(World world1) {
		super(world1);
	}

	public EntityEnderPearl(World world1, EntityLiving entityLiving2) {
		super(world1, entityLiving2);
	}

	public EntityEnderPearl(World world1, double d2, double d4, double d6) {
		super(world1, d2, d4, d6);
	}

	protected void onImpact(MovingObjectPosition movingObjectPosition1) {
		if(movingObjectPosition1.entityHit != null && movingObjectPosition1.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.thrower), 0)) {
			;
		}

		for(int i2 = 0; i2 < 32; ++i2) {
			this.worldObj.spawnParticle("portal", this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
		}

		if(!this.worldObj.isRemote) {
			if(this.thrower != null) {
				this.thrower.setPositionAndUpdate(this.posX, this.posY, this.posZ);
				this.thrower.fallDistance = 0.0F;
				this.thrower.attackEntityFrom(DamageSource.fall, 5);
			}

			this.setDead();
		}

	}
}
