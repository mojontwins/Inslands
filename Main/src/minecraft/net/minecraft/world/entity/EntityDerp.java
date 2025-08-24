package net.minecraft.world.entity;

import net.minecraft.world.level.World;

public class EntityDerp extends EntityCreature {

	public EntityDerp(World world1) {
		super(world1);
		this.texture = "/mob/derp.png";
		this.health = 100;
	}

	/*
	 * Use this to do init stuff when you know the entity is already in the world		
	 */
	public void justSpawned() {
		int x = (int)this.posX;
		int y = (int)this.posY;
		int z = (int)this.posZ;
		this.setHomeArea(x, y, z, 4);
		
		System.out.println("Derp home set at " + x + " " + y + " " + z);
	}
}
