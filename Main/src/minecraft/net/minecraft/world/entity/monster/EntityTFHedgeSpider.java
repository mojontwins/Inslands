package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.IMob;
import net.minecraft.world.level.World;

public class EntityTFHedgeSpider extends EntitySpider implements IMob {
	public EntityTFHedgeSpider(World world) {
		super(world);
		this.texture = "/mob/hedgespider.png";
	}

	public EntityTFHedgeSpider(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

}
