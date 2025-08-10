package net.minecraft.world.entity.status;

import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.player.EntityPlayer;

public class StatusBlind extends Status {

	public StatusBlind(int id, boolean isBadEffect) {
		super(id, isBadEffect);
		this.particleColor = 0x222222;
	}
	
	@Override
	public void performEffect (EntityLiving entityLiving, int amplifier, int duration) {
		// Decrease half a heart
		// Make slower somehow
		entityLiving.isBlinded = true;
	}
	
	@Override
	public void onCompleted(EntityLiving entityLiving, int amplifier) {
		entityLiving.isBlinded = false;
	}

	@Override
	public boolean isApplicableTo (EntityLiving entityLiving) {
		return (entityLiving instanceof EntityPlayer);
	}
}
