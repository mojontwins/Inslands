package com.mojontwins.minecraft.entity.status;

import net.minecraft.src.EntityLiving;

public class StatusSlowness extends Status {

	public StatusSlowness(int id, boolean isBadEffect) {
		super(id, isBadEffect);
		this.particleColor = 0x8695C1;
	}

	public void performEffect (EntityLiving entityLiving, int amplifier) {
		// Decrease half a heart
		// Make slower somehow
		entityLiving.speedModifier = 0.2F;
	}
	
	public void onCompleted(EntityLiving entityLiving, int amplifier) {
		entityLiving.speedModifier = 1.0F;
	}
	
	public boolean isApplicableTo (EntityLiving entityLiving) {
		return true;
		//return !(entityLiving instanceof EntityAlphaWitch);
	}
}
