package net.minecraft.world.entity.block;

import net.minecraft.src.World;
import net.minecraft.world.entity.Entity;

public class EntityMeatBlock extends EntityBlockEntity {
	public EntityMeatBlock(World world) {
		super(world);
		this.texture = "/mob/invisible.png";
	}

	public void onUpdate() {
	}
	
	public boolean attackEntityFrom(Entity entity, int damage) {
		return false;
	}
}
