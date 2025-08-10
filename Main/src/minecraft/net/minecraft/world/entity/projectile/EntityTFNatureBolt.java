package net.minecraft.world.entity.projectile;

import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.monster.EntitySnowball;
import net.minecraft.world.entity.status.Status;
import net.minecraft.world.entity.status.StatusEffect;

public class EntityTFNatureBolt extends EntitySnowball {
	public EntityTFNatureBolt(World par1World, double par2, double par4, double par6) {
		super(par1World, par2, par4, par6);
	}

	public EntityTFNatureBolt(World par1World, EntityLiving par2EntityLiving) {
		super(par1World, par2EntityLiving);
	}

	public EntityTFNatureBolt(World par1World) {
		super(par1World);
	}

	public void onUpdate() {
		super.onUpdate();
		this.makeTrail();
	}

	protected float func_40075_e() {
		return 0.003F;
	}

	public void makeTrail() {
		for(int i = 0; i < 5; ++i) {
			double dx = this.posX + 0.5D * (this.rand.nextDouble() - this.rand.nextDouble());
			double dy = this.posY + 0.5D * (this.rand.nextDouble() - this.rand.nextDouble());
			double dz = this.posZ + 0.5D * (this.rand.nextDouble() - this.rand.nextDouble());
			this.worldObj.spawnParticle("glowdust", dx, dy, dz, 0.0D, 0.0D, 0.0D);
		}

	}

	@Override
	public void throwableHitEntity(MovingObjectPosition par1MovingObjectPosition) {
		int i;
		if(par1MovingObjectPosition.entityHit != null && par1MovingObjectPosition.entityHit instanceof EntityLiving) {
			if(par1MovingObjectPosition.entityHit.attackEntityFrom(this.thrower, 2)) {
				byte b5 = (byte)(this.worldObj.difficultySetting == 0 ? 0 : (this.worldObj.difficultySetting == 2 ? 3 : 7));
				if(b5 > 0) {
					((EntityLiving)par1MovingObjectPosition.entityHit).addStatusEffect(new StatusEffect(Status.statusPoisoned.id, b5 * 20, 0));
					System.out.println("Poisoning entityHit " + par1MovingObjectPosition.entityHit);
				}
			}
		} else if(par1MovingObjectPosition != null) {
			i = MathHelper.floor_double((double)par1MovingObjectPosition.blockX);
			int dy = MathHelper.floor_double((double)par1MovingObjectPosition.blockY);
			int dz = MathHelper.floor_double((double)par1MovingObjectPosition.blockZ);
			if(this.worldObj.getBlockMaterial(i, dy, dz).isSolid()) {
				this.worldObj.setBlockAndMetadataWithNotify(i, dy, dz, Block.leaves.blockID, 2);
			}
		}

		for(i = 0; i < 8; ++i) {
			this.worldObj.spawnParticle("iconcrack_" + Block.leaves.blockID, this.posX, this.posY, this.posZ, this.rand.nextGaussian() * 0.05D, this.rand.nextDouble() * 0.2D, this.rand.nextGaussian() * 0.05D);
		}

		if(!this.worldObj.isRemote) {
			this.setEntityDead();
		}

	}
}
