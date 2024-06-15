package com.benimatic.twilightforest;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.RenderSpider;

public class RenderTFSwarmSpider extends RenderSpider {
	public RenderTFSwarmSpider() {
		this.mainModel = new ModelTFSwarmSpider();
		this.setRenderPassModel(this.mainModel);
		this.shadowSize = 0.5F;
	}

	public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
		super.doRenderLiving(entityliving, d, d1 - (double)0.7F, d2, f, f1);
	}
}
