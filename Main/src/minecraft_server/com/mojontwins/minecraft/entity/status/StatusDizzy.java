package com.mojontwins.minecraft.entity.status;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;

public class StatusDizzy extends Status {

	public StatusDizzy(int id, boolean isBadEffect) {
		super(id, isBadEffect);
		this.particleColor = 0x98A739;
	}

	@Override
	public void performEffect (EntityLiving entityLiving, int amplifier, int duration) {
		if(entityLiving instanceof EntityPlayer) {
			EntityPlayer entityPlayer = (EntityPlayer)entityLiving;
			if(duration > 60) {
				entityPlayer.timeInPortal += 0.006666667F + 0.05F;
				if(entityPlayer.timeInPortal > 1.0F) entityPlayer.timeInPortal = 1.0F;
			} 
			
			// EntityPlayerSP will take care of decreasing `timeInPortal` by itself.
		}
	
		entityLiving.speedModifier = 1.5F;
	}
	
	@Override
	public void onCompleted(EntityLiving entityLiving, int amplifier) {
		entityLiving.speedModifier = 1.0F;
	}
}
