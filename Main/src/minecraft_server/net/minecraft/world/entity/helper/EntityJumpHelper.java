package net.minecraft.world.entity.helper;

import net.minecraft.world.entity.EntityLiving;

public class EntityJumpHelper {
	private EntityLiving entity;
	private boolean isJumping = false;

	public EntityJumpHelper(EntityLiving entityLiving) {
		this.entity = entityLiving;
	}

	public void setJumping() {
		this.isJumping = true;
	}

	public void doJump() {
		this.entity.setJumping(this.isJumping);
		this.isJumping = false;
	}
}
