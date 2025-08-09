package com.mojontwins.minecraft.monsters;

import com.mojontwins.minecraft.entity.status.Status;
import com.mojontwins.minecraft.entity.status.StatusEffect;

import net.minecraft.src.AchievementList;
import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.monster.EntitySnowball;
import net.minecraft.world.entity.player.EntityPlayer;

public class EntityThrowableToxicFungus extends EntitySnowball {

	public EntityThrowableToxicFungus(World world1) {
		super(world1);
	}

	public EntityThrowableToxicFungus(World world1, double d2, double d4, double d6) {
		super(world1, d2, d4, d6);
	}

	public EntityThrowableToxicFungus(World world1, EntityLiving entityLiving2) {
		super(world1, entityLiving2);
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
	public void throwableHitEntity(MovingObjectPosition par1MovingObjectPosition) {
		int i;
		if(par1MovingObjectPosition.entityHit != null && par1MovingObjectPosition.entityHit instanceof EntityLiving) {
			
			EntityLiving comoCamote = ((EntityLiving)par1MovingObjectPosition.entityHit);
			
			if(comoCamote.attackEntityFrom(this.thrower, 1)) {
				byte time = (byte)(this.worldObj.difficultySetting == 0 ? 0 : 7);
				if(time > 0) {
					System.out.print("Adding statuses to " + comoCamote);
					comoCamote.addStatusEffect(new StatusEffect(Status.statusSlowness.id, time * 20, 0));

					if(comoCamote instanceof EntityPlayer) {
						((EntityPlayer)comoCamote).triggerAchievement(AchievementList.fungalInfection);
					}
				}
			}
		} else if(par1MovingObjectPosition != null) {
			int x = MathHelper.floor_double((double)par1MovingObjectPosition.blockX);
			int y = MathHelper.floor_double((double)par1MovingObjectPosition.blockY);
			int z = MathHelper.floor_double((double)par1MovingObjectPosition.blockZ);
			int blockID = this.worldObj.getBlockId(x, y, z);
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
