package net.minecraft.game.entity.monster;

import net.minecraft.game.world.World;

public class EntityGiantZombie extends EntityMob {
	private EntityGiantZombie(World var1) {
		super(var1);
	}

	protected final float getBlockPathWeight(int var1, int var2, int var3) {
		return this.worldObj.getBrightness(var1, var2, var3) - 0.5F;
	}
}
