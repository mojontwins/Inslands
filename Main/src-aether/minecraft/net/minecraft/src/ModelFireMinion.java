package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class ModelFireMinion extends ModelBiped {
	public ModelRenderer bipedBody2;
	public ModelRenderer bipedBody3;
	public ModelRenderer bipedBody4;
	public ModelRenderer bipedRightArm2;
	public ModelRenderer bipedLeftArm2;
	public ModelRenderer bipedRightArm3;
	public ModelRenderer bipedLeftArm3;

	public ModelFireMinion() {
		this(0.0F);
	}

	public ModelFireMinion(float f) {
		this(f, 0.0F);
	}

	public ModelFireMinion(float f, float f1) {
		this.field_1279_h = false;
		this.field_1278_i = false;
		this.isSneak = false;
		this.bipedHead = new ModelRenderer(0, 0);
		this.bipedHead.addBox(-4.0F, -8.0F, -3.0F, 8, 5, 7, f);
		this.bipedHead.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.bipedHeadwear = new ModelRenderer(32, 0);
		this.bipedHeadwear.addBox(-4.0F, -3.0F, -4.0F, 8, 3, 8, f);
		this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.bipedBody = new ModelRenderer(0, 12);
		this.bipedBody.addBox(-5.0F, 0.0F, -2.5F, 10, 6, 5, f);
		this.bipedBody.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.bipedBody2 = new ModelRenderer(0, 23);
		this.bipedBody2.addBox(-4.5F, 6.0F, -2.0F, 9, 5, 4, f);
		this.bipedBody2.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.bipedBody3 = new ModelRenderer(30, 27);
		this.bipedBody3.addBox(-4.5F, 11.0F, -2.0F, 5, 1, 4, f + 0.5F);
		this.bipedBody3.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.bipedBody4 = new ModelRenderer(30, 27);
		this.bipedBody4.addBox(-0.5F, 11.0F, -2.0F, 5, 1, 4, f + 0.5F);
		this.bipedBody4.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.bipedRightArm = new ModelRenderer(30, 11);
		this.bipedRightArm.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5, f + 0.5F);
		this.bipedRightArm.setRotationPoint(-8.0F, 2.0F + f1, 0.0F);
		this.bipedRightArm2 = new ModelRenderer(30, 11);
		this.bipedRightArm2.addBox(-2.5F, 2.5F, -2.5F, 5, 10, 5, f);
		this.bipedRightArm2.setRotationPoint(-8.0F, 2.0F + f1, 0.0F);
		this.bipedRightArm3 = new ModelRenderer(30, 26);
		this.bipedRightArm3.addBox(-2.5F, 7.5F, -2.5F, 5, 1, 5, f + 0.25F);
		this.bipedRightArm3.setRotationPoint(-8.0F, 2.0F + f1, 0.0F);
		this.bipedLeftArm = new ModelRenderer(30, 11);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5, f + 0.5F);
		this.bipedLeftArm.setRotationPoint(8.0F, 2.0F + f1, 0.0F);
		this.bipedLeftArm2 = new ModelRenderer(30, 11);
		this.bipedLeftArm2.mirror = true;
		this.bipedLeftArm2.addBox(-2.5F, 2.5F, -2.5F, 5, 10, 5, f);
		this.bipedLeftArm2.setRotationPoint(8.0F, 2.0F + f1, 0.0F);
		this.bipedLeftArm3 = new ModelRenderer(30, 26);
		this.bipedLeftArm3.mirror = true;
		this.bipedLeftArm3.addBox(-2.5F, 7.5F, -2.5F, 5, 1, 5, f + 0.25F);
		this.bipedLeftArm3.setRotationPoint(8.0F, 2.0F + f1, 0.0F);
	}

	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glScalef(1.0F, 1.0F, 1.0F);
		GL11.glTranslatef(0.0F, -0.25F, 0.0F);
		this.setRotationAngles(f, f1, f2, f3, f4, f5);
		this.bipedHead.render(f5);
		this.bipedHeadwear.render(f5);
		this.bipedBody.render(f5);
		this.bipedBody2.render(f5);
		this.bipedBody3.render(f5);
		this.bipedBody4.render(f5);
		this.bipedRightArm.render(f5);
		this.bipedRightArm2.render(f5);
		this.bipedRightArm3.render(f5);
		this.bipedLeftArm.render(f5);
		this.bipedLeftArm2.render(f5);
		this.bipedLeftArm3.render(f5);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		this.bipedHead.rotateAngleY = f3 / 57.29578F;
		this.bipedHead.rotateAngleX = f4 / 57.29578F;
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
		this.bipedRightArm.rotateAngleX = 0.0F;
		this.bipedLeftArm.rotateAngleX = 0.0F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		if(this.field_1279_h) {
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - 0.3141593F;
		}

		if(this.field_1278_i) {
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.3141593F;
		}

		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedLeftArm.rotateAngleY = 0.0F;
		if(this.onGround > -9990.0F) {
			float f6 = this.onGround;
			this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * 3.141593F * 2.0F) * 0.2F;
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
			f6 = 1.0F - this.onGround;
			f6 *= f6;
			f6 *= f6;
			f6 = 1.0F - f6;
			float f7 = MathHelper.sin(f6 * 3.141593F);
			float f8 = MathHelper.sin(this.onGround * 3.141593F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
			this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)f7 * 1.2D + (double)f8));
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
			this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * 3.141593F) * -0.4F;
		}

		this.bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
		this.bipedBody4.rotateAngleX = this.bipedBody3.rotateAngleX = this.bipedBody2.rotateAngleX = this.bipedBody.rotateAngleX;
		this.bipedBody4.rotateAngleY = this.bipedBody3.rotateAngleY = this.bipedBody2.rotateAngleY = this.bipedBody.rotateAngleY;
		this.bipedLeftArm3.rotateAngleX = this.bipedLeftArm2.rotateAngleX = this.bipedLeftArm.rotateAngleX;
		this.bipedLeftArm3.rotateAngleY = this.bipedLeftArm2.rotateAngleY = this.bipedLeftArm.rotateAngleY;
		this.bipedLeftArm3.rotateAngleZ = this.bipedLeftArm2.rotateAngleZ = this.bipedLeftArm.rotateAngleZ;
		this.bipedRightArm3.rotateAngleX = this.bipedRightArm2.rotateAngleX = this.bipedRightArm.rotateAngleX;
		this.bipedRightArm3.rotateAngleY = this.bipedRightArm2.rotateAngleY = this.bipedRightArm.rotateAngleY;
		this.bipedRightArm3.rotateAngleZ = this.bipedRightArm2.rotateAngleZ = this.bipedRightArm.rotateAngleZ;
	}
}
