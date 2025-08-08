package net.minecraft.client.model.twilight;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.src.MathHelper;

public class ModelTFMinoshroom extends ModelBiped {
	ModelRenderer body;
	ModelRenderer leg1;
	ModelRenderer leg2;
	ModelRenderer leg3;
	ModelRenderer leg4;
	ModelRenderer udders;
	ModelRenderer horn1a;
	ModelRenderer horn1b;
	ModelRenderer horn2a;
	ModelRenderer horn2b;
	ModelRenderer snout;

	public ModelTFMinoshroom() {
		this.textureWidth = 128;
		this.textureHeight = 32;
		this.bipedHead = new ModelRenderer(0, 0).setTextureSize(128, 32);
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
		this.bipedHead.setRotationPoint(0.0F, -6.0F, -9.0F);
		
		this.body = new ModelRenderer(18, 4).setTextureSize(128, 32);
		this.body.addBox(-6.0F, -10.0F, -7.0F, 12, 18, 10);
		this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
		this.setRotation(this.body, ((float) Math.PI / 2F), 0.0F, 0.0F);
		
		this.leg1 = new ModelRenderer(0, 16).setTextureSize(128, 32);
		this.leg1.addBox(-3.0F, 0.0F, -2.0F, 4, 12, 4);
		this.leg1.setRotationPoint(-3.0F, 12.0F, 7.0F);
		
		this.leg2 = new ModelRenderer(0, 16).setTextureSize(128, 32);
		this.leg2.addBox(-1.0F, 0.0F, -2.0F, 4, 12, 4);
		this.leg2.setRotationPoint(3.0F, 12.0F, 7.0F);
		
		this.leg3 = new ModelRenderer(0, 16).setTextureSize(128, 32);
		this.leg3.addBox(-3.0F, 0.0F, -3.0F, 4, 12, 4);
		this.leg3.setRotationPoint(-3.0F, 12.0F, -5.0F);
		
		this.leg4 = new ModelRenderer(0, 16).setTextureSize(128, 32);
		this.leg4.addBox(-1.0F, 0.0F, -3.0F, 4, 12, 4);
		this.leg4.setRotationPoint(3.0F, 12.0F, -5.0F);
		
		this.udders = new ModelRenderer(52, 0).setTextureSize(128, 32);
		this.udders.addBox(-2.0F, -3.0F, 0.0F, 4, 6, 2);
		this.udders.setRotationPoint(0.0F, 14.0F, 6.0F);
		this.setRotation(this.udders, ((float) Math.PI / 2F), 0.0F, 0.0F);
		
		this.bipedBody = new ModelRenderer(64, 0).setTextureSize(128, 32);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.5F, 8, 12, 5);
		this.bipedBody.setRotationPoint(0.0F, -6.0F, -9.0F);
		
		this.bipedLeftArm = new ModelRenderer(90, 0).setTextureSize(128, 32);
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4);
		this.bipedLeftArm.setRotationPoint(5.0F, -4.0F, -9.0F);
		this.bipedLeftArm.mirror = true;
		
		this.bipedRightArm = new ModelRenderer(90, 0).setTextureSize(128, 32);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4);
		this.bipedRightArm.setRotationPoint(-5.0F, -4.0F, -9.0F);
		
		this.horn1a = new ModelRenderer(24, 0).setTextureSize(128, 32);
		this.horn1a.addBox(0.0F, -1.0F, -1.0F, 3, 2, 2);
		this.horn1a.setRotationPoint(4.0F, -6.0F, 0.0F);
		this.horn1a.rotateAngleY = 0.2617994F;
		
		this.bipedHead.addChild(this.horn1a);
		
		this.horn1b = new ModelRenderer(24, 2).setTextureSize(128, 32);
		this.horn1b.addBox(-1.0F, -4.0F, -1.0F, 2, 4, 2);
		this.horn1b.setRotationPoint(2.75F, 0.0F, 0.0F);
		this.horn1b.rotateAngleX = 0.5235988F;
		this.horn1b.rotateAngleY = -0.5235988F;
		
		this.horn1a.addChild(this.horn1b);
		
		this.horn2a = new ModelRenderer(24, 0).setTextureSize(128, 32);
		this.horn2a.addBox(-3.0F, -1.0F, -1.0F, 3, 2, 2);
		this.horn2a.setRotationPoint(-4.0F, -6.0F, 0.0F);
		this.horn2a.rotateAngleY = -0.2617994F;
		
		this.bipedHead.addChild(this.horn2a);
		
		this.horn2b = new ModelRenderer(24, 2).setTextureSize(128, 32);
		this.horn2b.addBox(-1.0F, -4.0F, -1.0F, 2, 4, 2);
		this.horn2b.setRotationPoint(-2.75F, 0.0F, 0.0F);
		this.horn2b.rotateAngleX = 0.5235988F;
		this.horn2b.rotateAngleY = 0.5235988F;
		
		this.horn2a.addChild(this.horn2b);
		
		this.snout = new ModelRenderer(9, 12).setTextureSize(128, 32);
		this.snout.addBox(-2.0F, -1.0F, -1.0F, 4, 3, 1);
		this.snout.setRotationPoint(0.0F, -2.0F, -4.0F);
		
		this.bipedHead.addChild(this.snout);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(float var2, float var3, float var4, float var5, float var6, float var7) {
		this.setRotationAngles(var2, var3, var4, var5, var6, var7);
		this.bipedHead.render(var7);
		this.body.render(var7);
		this.leg1.render(var7);
		this.leg2.render(var7);
		this.leg3.render(var7);
		this.leg4.render(var7);
		this.udders.render(var7);
		this.bipedBody.render(var7);
		this.bipedLeftArm.render(var7);
		this.bipedRightArm.render(var7);
	}

	private void setRotation(ModelRenderer var1, float var2, float var3, float var4) {
		var1.rotateAngleX = var2;
		var1.rotateAngleY = var3;
		var1.rotateAngleZ = var4;
	}

	/**
	 * Sets the models various rotation angles.
	 */
	@Override
	public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.bipedHead.rotateAngleY = var4 / (180F / (float) Math.PI);
		this.bipedHead.rotateAngleX = var5 / (180F / (float) Math.PI);
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
		this.bipedRightArm.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float) Math.PI) * 2.0F * var2 * 0.5F;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 2.0F * var2 * 0.5F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float) Math.PI) * 1.4F * var2;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;

		if (this.heldItemLeft /*!= 0*/) {
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F
					- ((float) Math.PI / 10F) /** (float) this.heldItemLeft*/;
		}

		if (this.heldItemRight /*!= 0*/) {
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F
					- ((float) Math.PI / 10F) /** (float) this.heldItemRight*/;
		}

		this.bipedRightArm.rotateAngleZ += MathHelper.cos(var3 * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(var3 * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(var3 * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(var3 * 0.067F) * 0.05F;

		if (this.aimedBow) {
			float var7 = 0.0F;
			float var8 = 0.0F;
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleY = -(0.1F - var7 * 0.6F) + this.bipedHead.rotateAngleY;
			this.bipedLeftArm.rotateAngleY = 0.1F - var7 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
			this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
			this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
			this.bipedRightArm.rotateAngleX -= var7 * 1.2F - var8 * 0.4F;
			this.bipedLeftArm.rotateAngleX -= var7 * 1.2F - var8 * 0.4F;
			this.bipedRightArm.rotateAngleZ += MathHelper.cos(var3 * 0.09F) * 0.05F + 0.05F;
			this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(var3 * 0.09F) * 0.05F + 0.05F;
			this.bipedRightArm.rotateAngleX += MathHelper.sin(var3 * 0.067F) * 0.05F;
			this.bipedLeftArm.rotateAngleX -= MathHelper.sin(var3 * 0.067F) * 0.05F;
		}

		this.body.rotateAngleX = ((float) Math.PI / 2F);
		this.leg1.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
		this.leg2.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float) Math.PI) * 1.4F * var2;
		this.leg3.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float) Math.PI) * 1.4F * var2;
		this.leg4.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
	}
}
