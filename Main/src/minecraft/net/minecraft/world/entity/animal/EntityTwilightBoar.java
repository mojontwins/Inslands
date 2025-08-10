package net.minecraft.world.entity.animal;

import net.minecraft.src.World;

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
