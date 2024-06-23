package com.chocolatin.betterdungeons;

import net.minecraft.src.ModelRenderer;
import net.minecraft.src.ModelZombie;

public class ModelArcher extends ModelZombie {
	public ModelArcher() {
		float f = 0.0F;
		this.bipedRightArm = new ModelRenderer(40, 16);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, f);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.bipedLeftArm = new ModelRenderer(40, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, f);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.bipedRightLeg = new ModelRenderer(0, 16);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, f);
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(0, 16);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, f);
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		//this.field_40333_u = true;
		super.setRotationAngles(f, f1, f2, f3, f4, f5);
	}
}
