package com.mojang.minecraft.witch;

import net.minecraft.src.ModelRenderer;

public class ModelWitch extends ModelVillager
{
	public boolean isHoldingItem = false;
	private ModelRenderer witchWart = new ModelRenderer().setTextureSize(64, 128);
	private ModelRenderer witchCap;

	public ModelWitch(float par1) {
		super(par1, 0.0F, 64, 128);
		
		this.witchWart.setRotationPoint(0.0F, -2.0F, 0.0F);
		this.witchWart.setTextureOffset(0, 0).addBox(0.0F, 3.0F, -6.75F, 1, 1, 1, -0.25F);
		this.villagerNose.addChild(this.witchWart);
		
		// Cap & cap sections
		
		this.witchCap = new ModelRenderer().setTextureSize(64, 128);
		this.witchCap.setRotationPoint(-5.0F, -10.03125F, -5.0F);
		this.witchCap.setTextureOffset(0, 64).addBox(0.0F, 0.0F, 0.0F, 10, 2, 10);
		this.villagerHead.addChild(this.witchCap);
		
		ModelRenderer var2 = new ModelRenderer().setTextureSize(64, 128);
		var2.setRotationPoint(1.75F, -4.0F, 2.0F);
		var2.setTextureOffset(0, 76).addBox(0.0F, 0.0F, 0.0F, 7, 4, 7);
		var2.rotateAngleX = -0.05235988F;
		var2.rotateAngleZ = 0.02617994F;
		this.witchCap.addChild(var2);
		
		ModelRenderer var3 = new ModelRenderer().setTextureSize(64, 128);
		var3.setRotationPoint(1.75F, -4.0F, 2.0F);
		var3.setTextureOffset(0, 87).addBox(0.0F, 0.0F, 0.0F, 4, 4, 4);
		var3.rotateAngleX = -0.10471976F;
		var3.rotateAngleZ = 0.05235988F;
		var2.addChild(var3);
		
		ModelRenderer var4 = new ModelRenderer().setTextureSize(64, 128);
		var4.setRotationPoint(1.75F, -2.0F, 2.0F);
		var4.setTextureOffset(0, 95).addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.25F);
		var4.rotateAngleX = -0.20943952F;
		var4.rotateAngleZ = 0.10471976F;
		var3.addChild(var4);
	}

	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6) {
		super.setRotationAngles(par1, par2, par3, par4, par5, par6);
		this.villagerNose.translateX = this.villagerNose.translateY = this.villagerNose.translateZ = 0.0F;
		
		/*
		float var8 = 0.01F * (float)(par7Entity.entityId % 10);
		this.villagerNose.rotateAngleX = MathHelper.sin((float)par7Entity.ticksExisted * var8) * 4.5F * (float)Math.PI / 180.0F;
		this.villagerNose.rotateAngleY = 0.0F;
		this.villagerNose.rotateAngleZ = MathHelper.cos((float)par7Entity.ticksExisted * var8) * 2.5F * (float)Math.PI / 180.0F;
		*/

		if (this.isHoldingItem) {
			this.villagerNose.rotateAngleX = -0.9F;
			this.villagerNose.translateZ = -0.09375F;
			this.villagerNose.translateY = 0.1875F;
		} else {
			this.villagerNose.rotateAngleX = -0.0F;
			this.villagerNose.translateZ = -0.0F;
			this.villagerNose.translateY = 0.0F;
		}
	}

	public int modelId() {
		return 0;
	}
}
