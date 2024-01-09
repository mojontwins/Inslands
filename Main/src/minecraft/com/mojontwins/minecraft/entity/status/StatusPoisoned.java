package com.mojontwins.minecraft.entity.status;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityZombie;

public class StatusPoisoned extends Status {
	public StatusPoisoned(int id, boolean isBadEffect) {
		super(id, true);
		this.particleColor = 0x70B433;
	}
	
	public void performEffect (EntityLiving entityLiving, int amplifier) {
		// Decrease half a heart
		if (entityLiving.health > 1) {
			entityLiving.attackEntityFrom((Entity)null, 1);
		}
	}
	
	public boolean isReady (int tick, int amplifier) {
		// Run every 5 ticks
		return (tick % 5) == 0;
	}

	public boolean isApplicableTo (EntityLiving entityLiving) {
		// Zombies can't be poisoned
		return !(entityLiving instanceof EntityZombie);
	}
}
