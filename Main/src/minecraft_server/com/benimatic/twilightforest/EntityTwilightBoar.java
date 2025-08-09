package com.benimatic.twilightforest;

import net.minecraft.src.World;
import net.minecraft.world.entity.animal.EntityPig;

public class EntityTwilightBoar extends EntityPig {
	public EntityTwilightBoar(World world) {
		super(world);
		this.texture = "/mob/wildboar.png";
		this.setSize(0.9F, 0.9F);
	}
	
	public boolean needsLitBlockToSpawn() {
		return false;
	}
}
