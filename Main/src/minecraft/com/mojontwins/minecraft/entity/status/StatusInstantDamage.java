package com.mojontwins.minecraft.entity.status;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;

public class StatusInstantDamage extends Status {

	public StatusInstantDamage(int id, boolean isBadEffect) {
		super(id, isBadEffect);
		this.particleColor = 0xFF0000;
	}
	
	public void performEffect (EntityLiving entityLiving, int amplifier) {
		entityLiving.attackEntityFrom((Entity)null, 6);
	}
	
	public boolean isReady (int tick, int amplifier) {
		return true;
	}
	
	public boolean isOneShot() {
		return true;
	}
}
