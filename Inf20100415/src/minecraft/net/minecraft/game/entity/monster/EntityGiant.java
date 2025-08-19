package net.minecraft.game.entity.monster;

import net.minecraft.game.world.World;

public class EntityGiant extends EntityMonster {
	public EntityGiant(World world1) {
		super(world1);
		this.texture = "/mob/zombie.png";
		this.moveSpeed = 0.5F;
		this.attackStrength = 50;
		this.health *= 10;
		this.yOffset *= 6.0F;
		this.setSize(this.width * 6.0F, this.height * 6.0F);
	}

	protected final float getBlockPathWeight(int i1, int i2, int i3) {
		return this.worldObj.getBrightness(i1, i2, i3) - 0.5F;
	}
}