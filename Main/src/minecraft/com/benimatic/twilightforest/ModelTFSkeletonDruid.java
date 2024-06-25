package com.benimatic.twilightforest;

import net.minecraft.src.ModelRenderer;
import net.minecraft.src.ModelSkeleton;

public class ModelTFSkeletonDruid extends ModelSkeleton {
	public ModelRenderer dress;

	public ModelTFSkeletonDruid() {
		float f = 0.0F;
		this.bipedBody = new ModelRenderer( 8, 16);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, f);
		this.bipedBody.setRotationPoint(0.0F, 0.0F + f, 0.0F);
		this.bipedRightArm = new ModelRenderer(0, 16);
		this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, f);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.bipedLeftArm = new ModelRenderer(0, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, f);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.dress = new ModelRenderer(40, 16);
		this.dress.addBox(-4.0F, 12.0F, -2.0F, 8, 12, 4, f);
		this.dress.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	@Override
	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(f, f1, f2, f3, f4, f5);
		this.dress.render(f5);
	}
}
