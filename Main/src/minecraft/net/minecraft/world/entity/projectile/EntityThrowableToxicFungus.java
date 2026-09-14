package net.minecraft.world.entity.projectile;

import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.entity.status.Status;
import net.minecraft.world.entity.status.StatusEffect;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;
import net.minecraft.world.phys.MovingObjectPosition;
import net.minecraft.world.stats.AchievementList;

public class EntityThrowableToxicFungus extends EntitySnowball {

	public EntityThrowableToxicFungus(World world) {
		super(world);
	}

	public EntityThrowableToxicFungus(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityThrowableToxicFungus(World world, EntityLiving thrower) {
		super(world, thrower);
	}

	public void onUpdate() {
		super.onUpdate();
		this.makeTrail();
	}

	public void makeTrail() {
		for(int i = 0; i < 5; ++i) {
			double dx = this.posX + 0.5D * (this.rand.nextDouble() - this.rand.nextDouble());
			double dy = this.posY + 0.5D * (this.rand.nextDouble() - this.rand.nextDouble());
			double dz = this.posZ + 0.5D * (this.rand.nextDouble() - this.rand.nextDouble());
			this.worldObj.spawnParticle(
					"status_effect", 
					dx, dy, dz, 
					0.439F, 0.705F, 0.2F);
		}

	}
	
	@Override
	public void throwableHitEntity(MovingObjectPosition hitPos) {
		int i;
		if (hitPos.entityHit != null && hitPos.entityHit instanceof EntityLiving) {
			EntityLiving entityHit = (EntityLiving) hitPos.entityHit;
			if (entityHit.attackEntityFrom(this.thrower, 1)) {
				byte time = (byte) (this.worldObj.difficultySetting == 0 ? 0 : 7);
				if (time > 0) {
					System.out.print("Adding statuses to " + entityHit);
					entityHit.addStatusEffect(new StatusEffect(Status.statusSlowness.id, time * 20, 0));

					if (entityHit instanceof EntityPlayer) {
						((EntityPlayer) entityHit).triggerAchievement(AchievementList.fungalInfection);
					}
				}
			}
		} else if (hitPos != null) {
			int x = MathHelper.floor_double((double) hitPos.blockX);
			int y = MathHelper.floor_double((double) hitPos.blockY);
			int z = MathHelper.floor_double((double) hitPos.blockZ);
			int blockID = this.worldObj.getBlockID(x, y, z);
			if(blockID == Block.dirt.blockID || blockID == Block.grass.blockID || blockID == Block.sand.blockID) {
				this.worldObj.setBlockWithNotify(x, y, z, Block.mycelium.blockID);
			}
		}

		for(i = 0; i < 8; ++i) {
			this.worldObj.spawnParticle("iconcrack_" + Block.mycelium.blockID, this.posX, this.posY, this.posZ, this.rand.nextGaussian() * 0.05D, this.rand.nextDouble() * 0.2D, this.rand.nextGaussian() * 0.05D);
		}

		if(!this.worldObj.isRemote) {
			this.setEntityDead();
		}

	}
}
