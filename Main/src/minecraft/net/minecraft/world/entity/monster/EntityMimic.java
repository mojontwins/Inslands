package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.EntityPlayer;
import net.minecraft.world.level.World;
import net.minecraft.world.level.tile.Block;

public class EntityMimic extends EntityDungeonMob {
	public float mouth;
	public float legs;
	private float legsDirection = 1.0F;

	public EntityMimic(World world) {
		super(world);
		this.texture = "/mob/Mimic.png";
		this.yOffset = 0.0F;
		this.setSize(1.0F, 2.0F);
		this.health = 40;
		this.attackStrength = 5;
		this.entityToAttack = world.getClosestPlayerToEntity(this, 64.0D);
	}

	public void onUpdate() {
		super.onUpdate();
		this.mouth = (float)(Math.cos((double)((float)this.ticksExisted / 10.0F * (float)Math.PI)) + 1.0D) * 0.6F;
		this.legs *= 0.9F;
		if(this.motionX > 0.001D || this.motionX < -0.001D || this.motionZ > 0.001D || this.motionZ < -0.001D) {
			this.legs += this.legsDirection * 0.2F;
			if(this.legs > 1.0F) {
				this.legsDirection = -1.0F;
			}

			if(this.legs < -1.0F) {
				this.legsDirection = 1.0F;
			}
		}

	}

	public void applyEntityCollision(Entity entity) {
		if(!this.isDead && entity != null) {
			entity.attackEntityFrom(this, 4);
		}

	}

	public boolean attackEntityFrom(Entity entity, int i) {
		if(entity instanceof EntityPlayer) {
			this.faceEntity(entity, 10.0F, 10.0F);
			this.entityToAttack = (EntityPlayer)entity;
		}

		return super.attackEntityFrom(entity, i);
	}

	protected String getHurtSound() {
		return "mob.slime";
	}

	protected String getDeathSound() {
		return "mob.slime";
	}

	protected float getSoundVolume() {
		return 0.6F;
	}

	protected int getDropItemId() {
		return Block.chest.blockID;
	}
}
