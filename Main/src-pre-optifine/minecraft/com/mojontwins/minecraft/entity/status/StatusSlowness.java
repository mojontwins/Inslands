package com.mojontwins.minecraft.entity.status;

import com.mojang.minecraft.witch.EntityAlphaWitch;

import net.minecraft.src.EntityLiving;

public class StatusSlowness extends Status {

	public StatusSlowness(int id, boolean isBadEffect) {
		super(id, isBadEffect);
		this.particleColor = 0x8695C1;
	}

	@Override
	public void performEffect (EntityLiving entityLiving, int amplifier, int duration) {
		// Decrease half a heart
		// Make slower somehow
		entityLiving.speedModifier = 0.2F;
	}
	
	@Override
	public void onCompleted(EntityLiving entityLiving, int amplifier) {
		entityLiving.speedModifier = 1.0F;
	}
	
	@Override
	public boolean isApplicableTo (EntityLiving entityLiving) {
		return !(entityLiving instanceof EntityAlphaWitch);
	}
}
