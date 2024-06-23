package com.benimatic.twilightforest;

import net.minecraft.src.ModelRenderer;
import net.minecraft.src.ModelZombie;

public class ModelTFWraith extends ModelZombie {
	public ModelRenderer dress;

	public ModelTFWraith() {
		float f = 0.0F;
		this.dress = new ModelRenderer(40, 16);
		this.dress.addBox(-4.0F, 12.0F, -2.0F, 8, 12, 4, f);
		this.dress.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5);
		this.bipedHead.render(f5);
		this.bipedBody.render(f5);
		this.bipedRightArm.render(f5);
		this.bipedLeftArm.render(f5);
		this.dress.render(f5);
	}
}
