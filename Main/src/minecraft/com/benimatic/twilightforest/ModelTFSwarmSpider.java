package com.benimatic.twilightforest;

import net.minecraft.src.ModelSpider;

public class ModelTFSwarmSpider extends ModelSpider {
	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(f, f1, f2, f3, f4, f5 * 0.5F);
	}
}
