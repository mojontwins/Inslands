package com.mojontwins.minecraft.entity.status;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityZombie;

public class StatusAutoHealing extends Status {
	public StatusAutoHealing(int id, boolean isBadEffect) {
		super(id, isBadEffect);
		this.particleColor = 0xD24343;
	}
	
	public void performEffect (EntityLiving entityLiving, int amplifier) {
		// Increase half a heart - decrease for zombies!
		if(entityLiving instanceof EntityZombie) {
			if(entityLiving.health > 1) {
				entityLiving.attackEntityFrom((Entity)null, 1);
			}
		} else if(entityLiving.health < entityLiving.getFullHealth()) {
			entityLiving.heal(amplifier);
		}
	}
	
	public boolean isReady (int tick, int amplifier) {
		// Run every 10 ticks
		return (tick % 10) == 0;
	}

	public boolean isApplicableTo (EntityLiving entityLiving) {
		// Zombies can't be poisoned
		return !(entityLiving instanceof EntityZombie);
	}
}
