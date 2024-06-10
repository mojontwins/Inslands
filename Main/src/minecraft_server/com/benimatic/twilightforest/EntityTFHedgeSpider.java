package com.benimatic.twilightforest;

import net.minecraft.src.EntitySpider;
import net.minecraft.src.World;

public class EntityTFHedgeSpider extends EntitySpider {
	public EntityTFHedgeSpider(World world) {
		super(world);
		this.texture = "/mob/hedgespider.png";
	}

	public EntityTFHedgeSpider(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

}
