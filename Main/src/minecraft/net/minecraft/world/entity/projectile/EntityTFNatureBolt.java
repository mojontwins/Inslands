package net.minecraft.world.entity.projectile;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.status.Status;
import net.minecraft.world.entity.status.StatusEffect;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.phys.MovingObjectPosition;

public class EntityTFNatureBolt extends EntitySnowball {
	public EntityTFNatureBolt(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityTFNatureBolt(World world, EntityLiving thrower) {
		super(world, thrower);
	}

	public EntityTFNatureBolt(World world) {
		super(world);
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
	public void throwableHitEntity(MovingObjectPosition hitPos) {
		int i;
		if (hitPos.entityHit != null && hitPos.entityHit instanceof EntityLiving) {
			if (hitPos.entityHit.attackEntityFrom(this.thrower, 2)) {
				byte duration = (byte) (this.worldObj.difficultySetting == 0 ? 0 : (this.worldObj.difficultySetting == 2 ? 3 : 7));
				if (duration > 0) {
					((EntityLiving) hitPos.entityHit).addStatusEffect(new StatusEffect(Status.statusPoisoned.id, duration * 20, 0));
					System.out.println("Poisoning entityHit " + hitPos.entityHit);
				}
			}
		} else if (hitPos != null) {
			i = MathHelper.floor_double((double) hitPos.blockX);
			int dy = MathHelper.floor_double((double) hitPos.blockY);
			int dz = MathHelper.floor_double((double) hitPos.blockZ);
			if (this.worldObj.getBlockMaterial(i, dy, dz).isSolid()) {
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
