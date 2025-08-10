package net.minecraft.client.model.betterdungeons;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.monster.EntityHumanBase;

public class ModelHuman extends ModelBase {
	public ModelRenderer bipedHead;
	public ModelRenderer bipedHeadwear;
	public ModelRenderer bipedBody;
	public ModelRenderer bipedRightArm;
	public ModelRenderer bipedLeftArm;
	public ModelRenderer bipedRightLeg;
	public ModelRenderer bipedLeftLeg;
	public ModelRenderer bipedEars;
	public ModelRenderer bipedCloak;
	public int heldItemLeft;
	public int heldItemRight;
	public boolean isSneak;
	public boolean aimedBow;

	public ModelHuman() {
		this(0.0F);
	}

	public ModelHuman(float f) {
		this(f, 0.0F);
	}

	public ModelHuman(float f, float f1) {
		this.heldItemLeft = 0;
		this.heldItemRight = 0;
		this.isSneak = false;
		this.aimedBow = false;
		this.bipedCloak = new ModelRenderer(0, 0);
		this.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, f);
		this.bipedEars = new ModelRenderer(24, 0);
		this.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, f);
		this.bipedHead = new ModelRenderer(0, 0);
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, f);
		this.bipedHead.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.bipedHeadwear = new ModelRenderer(32, 0);
		this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, f + 0.5F);
		this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.bipedBody = new ModelRenderer(16, 16);
		this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, f);
		this.bipedBody.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.bipedRightArm = new ModelRenderer(40, 16);
		this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, f);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + f1, 0.0F);
		this.bipedLeftArm = new ModelRenderer(40, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, f);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + f1, 0.0F);
		this.bipedRightLeg = new ModelRenderer(0, 16);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, f);
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F + f1, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(0, 16);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, f);
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + f1, 0.0F);
	}

	public void render(EntityLiving entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5);
		this.bipedHead.render(f5);
		this.bipedBody.render(f5);
		this.bipedRightArm.render(f5);
		this.bipedLeftArm.render(f5);
		this.bipedRightLeg.render(f5);
		this.bipedLeftLeg.render(f5);
		this.bipedHeadwear.render(f5);
	}
	
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		this.bipedHead.rotateAngleY = f3 / 57.29578F;
		this.bipedHead.rotateAngleX = f4 / 57.29578F;
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
		this.bipedRightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 2.0F * f1 * 0.5F;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;
		if(this.isRiding) {
			this.bipedRightArm.rotateAngleX += -0.6283185F;
			this.bipedLeftArm.rotateAngleX += -0.6283185F;
			this.bipedRightLeg.rotateAngleX = -1.256637F;
			this.bipedLeftLeg.rotateAngleX = -1.256637F;
			this.bipedRightLeg.rotateAngleY = 0.3141593F;
			this.bipedLeftLeg.rotateAngleY = -0.3141593F;
		}

		if(this.heldItemLeft != 0) {
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - 0.3141593F * (float)this.heldItemLeft;
		}

		if(this.heldItemRight != 0) {
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.3141593F * (float)this.heldItemRight;
		}

		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedLeftArm.rotateAngleY = 0.0F;
		float f6;
		float f7;
		if(this.swingProgress > -9990.0F) {
			f6 = this.swingProgress;
			this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * 3.141593F * 2.0F) * 0.2F;
			this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
			f6 = 1.0F - this.swingProgress;
			f6 *= f6;
			f6 *= f6;
			f6 = 1.0F - f6;
			f7 = MathHelper.sin(f6 * 3.141593F);
			float f10 = MathHelper.sin(this.swingProgress * 3.141593F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
			this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)f7 * 1.2D + (double)f10));
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
			this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.swingProgress * 3.141593F) * -0.4F;
		}

		if(this.isSneak) {
			this.bipedBody.rotateAngleX = 0.5F;
			this.bipedRightLeg.rotateAngleX -= 0.0F;
			this.bipedLeftLeg.rotateAngleX -= 0.0F;
			this.bipedRightArm.rotateAngleX += 0.4F;
			this.bipedLeftArm.rotateAngleX += 0.4F;
			this.bipedRightLeg.rotationPointZ = 4.0F;
			this.bipedLeftLeg.rotationPointZ = 4.0F;
			this.bipedRightLeg.rotationPointY = 9.0F;
			this.bipedLeftLeg.rotationPointY = 9.0F;
			this.bipedHead.rotationPointY = 1.0F;
		} else {
			this.bipedBody.rotateAngleX = 0.0F;
			this.bipedRightLeg.rotationPointZ = 0.0F;
			this.bipedLeftLeg.rotationPointZ = 0.0F;
			this.bipedRightLeg.rotationPointY = 12.0F;
			this.bipedLeftLeg.rotationPointY = 12.0F;
			this.bipedHead.rotationPointY = 0.0F;
		}

		this.bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
		if(this.aimedBow) {
			f6 = 0.0F;
			f7 = 0.0F;
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F) + this.bipedHead.rotateAngleY;
			this.bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
			this.bipedRightArm.rotateAngleX = -1.570796F + this.bipedHead.rotateAngleX;
			this.bipedLeftArm.rotateAngleX = -1.570796F + this.bipedHead.rotateAngleX;
			this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
			this.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
			this.bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
			this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
			this.bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
			this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
		}

		/*
		f6 = MathHelper.sin(this.swingProgress * 3.141593F);
		f7 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * 3.141593F);
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
		this.bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
		this.bipedRightArm.rotateAngleX = -1.570796F;
		this.bipedLeftArm.rotateAngleX = -1.570796F;
		this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
		this.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
		this.bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
		*/
	}

	public void renderArmor(EntityHumanBase z, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5);
		if(z.armor > 0) {
			GL11.glPushMatrix();
			GL11.glScalef(1.1F, 1.1F, 1.1F);
			this.bipedBody.render(f5);
			this.bipedRightArm.render(f5);
			this.bipedLeftArm.render(f5);
			GL11.glPopMatrix();
		}

		if(z.armor > 2) {
			GL11.glPushMatrix();
			GL11.glScalef(1.1F, 1.0F, 1.1F);
			this.bipedRightLeg.render(f5);
			this.bipedLeftLeg.render(f5);
			GL11.glPopMatrix();
		}

		if(z.armor > 3) {
			GL11.glPushMatrix();
			GL11.glScalef(1.2F, 1.1F, 1.3F);
			this.bipedHead.render(f5);
			this.bipedHeadwear.render(f5);
			GL11.glPopMatrix();
		}

	}

	public void renderArmorLegs(EntityHumanBase z, float f, float f1, float f2, float f3, float f4, float f5) {
		if(z.armor > 1) {
			GL11.glPushMatrix();
			this.bipedBody.render(f5);
			this.bipedRightLeg.render(f5);
			this.bipedLeftLeg.render(f5);
			GL11.glPopMatrix();
		}

	}
	
	public void renderEars(float f) {
		this.bipedEars.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedEars.rotateAngleX = this.bipedHead.rotateAngleX;
		this.bipedEars.rotationPointX = 0.0F;
		this.bipedEars.rotationPointY = 0.0F;
		this.bipedEars.render(f);
	}

	public void renderCloak(float f) {
		this.bipedCloak.render(f);
	}
}
