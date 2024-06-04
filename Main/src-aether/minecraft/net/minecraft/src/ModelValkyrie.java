package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class ModelValkyrie extends ModelBiped {
	public ModelRenderer bipedBody2;
	public ModelRenderer bipedRightArm2;
	public ModelRenderer bipedLeftArm2;
	public ModelRenderer wingLeft;
	public ModelRenderer wingRight;
	public ModelRenderer[] skirt;
	public ModelRenderer[] sword;
	public ModelRenderer[] strand;
	public ModelRenderer[] halo;
	public static final int swordParts = 5;
	public static final int skirtParts = 6;
	public static final int strandParts = 22;
	public static final int haloParts = 4;
	public float sinage;
	public boolean gonRound;
	public boolean halow;

	public ModelValkyrie() {
		this(0.0F);
	}

	public ModelValkyrie(float f) {
		this(f, 0.0F);
	}

	public ModelValkyrie(float f, float f1) {
		this.field_1279_h = false;
		this.field_1278_i = false;
		this.isSneak = false;
		this.bipedHead = new ModelRenderer(0, 0);
		this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, f);
		this.bipedHead.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.bipedBody = new ModelRenderer(12, 16);
		this.bipedBody.addBox(-3.0F, 0.0F, -1.5F, 6, 12, 3, f);
		this.bipedBody.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.bipedBody2 = new ModelRenderer(12, 16);
		this.bipedBody2.addBox(-3.0F, 0.5F, -1.25F, 6, 5, 3, f + 0.75F);
		this.bipedBody2.setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.bipedRightArm = new ModelRenderer(30, 16);
		this.bipedRightArm.addBox(-3.0F, -1.5F, -1.5F, 3, 12, 3, f);
		this.bipedRightArm.setRotationPoint(-4.0F, 1.5F + f1, 0.0F);
		this.bipedLeftArm = new ModelRenderer(30, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -1.5F, -1.5F, 3, 12, 3, f);
		this.bipedLeftArm.setRotationPoint(5.0F, 1.5F + f1, 0.0F);
		this.bipedRightArm2 = new ModelRenderer(30, 16);
		this.bipedRightArm2.addBox(-3.0F, -1.5F, -1.5F, 3, 3, 3, f + 0.75F);
		this.bipedRightArm2.setRotationPoint(-4.0F, 1.5F + f1, 0.0F);
		this.bipedLeftArm2 = new ModelRenderer(30, 16);
		this.bipedLeftArm2.mirror = true;
		this.bipedLeftArm2.addBox(-1.0F, -1.5F, -1.5F, 3, 3, 3, f + 0.75F);
		this.bipedLeftArm2.setRotationPoint(5.0F, 1.5F + f1, 0.0F);
		this.bipedRightLeg = new ModelRenderer(0, 16);
		this.bipedRightLeg.addBox(-2.0F, 0.0F, -1.5F, 3, 12, 3, f);
		this.bipedRightLeg.setRotationPoint(-1.0F, 12.0F + f1, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(0, 16);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-2.0F, 0.0F, -1.5F, 3, 12, 3, f);
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F + f1, 0.0F);
		this.sword = new ModelRenderer[5];
		this.sword[0] = new ModelRenderer(9, 16);
		this.sword[0].addBox(-2.5F, 8.0F, 1.5F, 2, 2, 1, f);
		this.sword[0].setRotationPoint(-4.0F, 1.5F + f1, 0.0F);
		this.sword[1] = new ModelRenderer(32, 10);
		this.sword[1].addBox(-3.0F, 6.5F, -2.75F, 3, 5, 1, f + 0.5F);
		this.sword[1].setRotationPoint(-4.0F, 1.5F + f1, 0.0F);
		this.sword[2] = new ModelRenderer(42, 18);
		this.sword[2].addBox(-2.0F, 7.5F, -12.5F, 1, 3, 10, f);
		this.sword[2].setRotationPoint(-4.0F, 1.5F + f1, 0.0F);
		this.sword[3] = new ModelRenderer(42, 18);
		this.sword[3].addBox(-2.0F, 7.5F, -22.5F, 1, 3, 10, f);
		this.sword[3].setRotationPoint(-4.0F, 1.5F + f1, 0.0F);
		this.sword[4] = new ModelRenderer(28, 17);
		this.sword[4].addBox(-2.0F, 8.5F, -23.5F, 1, 1, 1, f);
		this.sword[4].setRotationPoint(-4.0F, 1.5F + f1, 0.0F);
		this.wingLeft = new ModelRenderer(24, 31);
		this.wingLeft.addBox(0.0F, -4.5F, 0.0F, 19, 8, 1, f);
		this.wingLeft.setRotationPoint(0.5F, 4.5F + f1, 2.625F);
		this.wingRight = new ModelRenderer(24, 31);
		this.wingRight.mirror = true;
		this.wingRight.addBox(-19.0F, -4.5F, 0.0F, 19, 8, 1, f);
		this.wingRight.setRotationPoint(-0.5F, 4.5F + f1, 2.625F);
		this.skirt = new ModelRenderer[6];
		this.skirt[0] = new ModelRenderer(0, 0);
		this.skirt[0].addBox(0.0F, 0.0F, -1.0F, 3, 6, 1, f);
		this.skirt[0].setRotationPoint(-3.0F, 9.0F + f1, -1.5F);
		this.skirt[1] = new ModelRenderer(0, 0);
		this.skirt[1].addBox(0.0F, 0.0F, -1.0F, 3, 6, 1, f);
		this.skirt[1].setRotationPoint(0.0F, 9.0F + f1, -1.5F);
		this.skirt[2] = new ModelRenderer(0, 0);
		this.skirt[2].addBox(0.0F, 0.0F, 0.0F, 3, 6, 1, f);
		this.skirt[2].setRotationPoint(-3.0F, 9.0F + f1, 1.5F);
		this.skirt[3] = new ModelRenderer(0, 0);
		this.skirt[3].addBox(0.0F, 0.0F, 0.0F, 3, 6, 1, f);
		this.skirt[3].setRotationPoint(0.0F, 9.0F + f1, 1.5F);
		this.skirt[4] = new ModelRenderer(55, 19);
		this.skirt[4].addBox(-1.0F, 0.0F, 0.0F, 1, 6, 3, f);
		this.skirt[4].setRotationPoint(-3.0F, 9.0F + f1, -1.5F);
		this.skirt[5] = new ModelRenderer(55, 19);
		this.skirt[5].addBox(0.0F, 0.0F, 0.0F, 1, 6, 3, f);
		this.skirt[5].setRotationPoint(3.0F, 9.0F + f1, -1.5F);
		this.strand = new ModelRenderer[22];

		for(int i = 0; i < 22; ++i) {
			this.strand[i] = new ModelRenderer(42 + i % 7, 17);
		}

		this.strand[0].addBox(-5.0F, -7.0F, -4.0F, 1, 3, 1, f);
		this.strand[0].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[1].addBox(4.0F, -7.0F, -4.0F, 1, 3, 1, f);
		this.strand[1].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[2].addBox(-5.0F, -7.0F, -3.0F, 1, 4, 1, f);
		this.strand[2].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[3].addBox(4.0F, -7.0F, -3.0F, 1, 4, 1, f);
		this.strand[3].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[4].addBox(-5.0F, -7.0F, -2.0F, 1, 4, 1, f);
		this.strand[4].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[5].addBox(4.0F, -7.0F, -2.0F, 1, 4, 1, f);
		this.strand[5].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[6].addBox(-5.0F, -7.0F, -1.0F, 1, 5, 1, f);
		this.strand[6].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[7].addBox(4.0F, -7.0F, -1.0F, 1, 5, 1, f);
		this.strand[7].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[8].addBox(-5.0F, -7.0F, 0.0F, 1, 5, 1, f);
		this.strand[8].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[9].addBox(4.0F, -7.0F, 0.0F, 1, 5, 1, f);
		this.strand[9].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[10].addBox(-5.0F, -7.0F, 1.0F, 1, 6, 1, f);
		this.strand[10].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[11].addBox(4.0F, -7.0F, 1.0F, 1, 6, 1, f);
		this.strand[11].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[12].addBox(-5.0F, -7.0F, 2.0F, 1, 7, 1, f);
		this.strand[12].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[13].addBox(4.0F, -7.0F, 2.0F, 1, 7, 1, f);
		this.strand[13].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[14].addBox(-5.0F, -7.0F, 3.0F, 1, 8, 1, f);
		this.strand[14].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[15].addBox(4.0F, -7.0F, 3.0F, 1, 8, 1, f);
		this.strand[15].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[16].addBox(-4.0F, -7.0F, 4.0F, 1, 9, 1, f);
		this.strand[16].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[17].addBox(3.0F, -7.0F, 4.0F, 1, 9, 1, f);
		this.strand[17].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[18] = new ModelRenderer(42, 17);
		this.strand[18].addBox(-3.0F, -7.0F, 4.0F, 3, 10, 1, f);
		this.strand[18].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[19] = new ModelRenderer(43, 17);
		this.strand[19].addBox(0.0F, -7.0F, 4.0F, 3, 10, 1, f);
		this.strand[19].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[20].addBox(-1.0F, -7.0F, -5.0F, 1, 2, 1, f);
		this.strand[20].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.strand[21].addBox(0.0F, -7.0F, -5.0F, 1, 3, 1, f);
		this.strand[21].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.halo = new ModelRenderer[4];
		this.halo[0] = new ModelRenderer(43, 9);
		this.halo[0].addBox(-2.5F, -11.0F, -3.5F, 5, 1, 1, f);
		this.halo[0].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.halo[1] = new ModelRenderer(43, 9);
		this.halo[1].addBox(-2.5F, -11.0F, 2.5F, 5, 1, 1, f);
		this.halo[1].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.halo[2] = new ModelRenderer(42, 11);
		this.halo[2].addBox(-3.5F, -11.0F, -2.5F, 1, 1, 5, f);
		this.halo[2].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
		this.halo[3] = new ModelRenderer(42, 11);
		this.halo[3].addBox(2.5F, -11.0F, -2.5F, 1, 1, 5, f);
		this.halo[3].setRotationPoint(0.0F, 0.0F + f1, 0.0F);
	}

	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5);
		this.bipedHead.render(f5);
		this.bipedBody.render(f5);
		this.bipedRightArm.render(f5);
		this.bipedLeftArm.render(f5);
		this.bipedRightLeg.render(f5);
		this.bipedLeftLeg.render(f5);
		this.bipedBody2.render(f5);
		this.bipedRightArm2.render(f5);
		this.bipedLeftArm2.render(f5);
		this.wingLeft.render(f5);
		this.wingRight.render(f5);

		int i;
		for(i = 0; i < 5; ++i) {
			this.sword[i].render(f5);
		}

		for(i = 0; i < 6; ++i) {
			this.skirt[i].render(f5);
		}

		for(i = 0; i < 22; ++i) {
			this.strand[i].render(f5);
		}

		if(this.halow) {
			GL11.glEnable(GL11.GL_NORMALIZE);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			for(i = 0; i < 4; ++i) {
				this.halo[i].render(f5);
			}

			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}

	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		this.bipedHead.rotateAngleY = f3 / 57.29578F;
		this.bipedHead.rotateAngleX = f4 / 57.29578F;
		this.bipedRightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 2.0F * f1 * 0.5F;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
		this.bipedRightArm.rotateAngleZ = 0.05F;
		this.bipedLeftArm.rotateAngleZ = -0.05F;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;

		int i;
		for(i = 0; i < 22; ++i) {
			this.strand[i].rotateAngleY = this.bipedHead.rotateAngleY;
			this.strand[i].rotateAngleX = this.bipedHead.rotateAngleX;
		}

		for(i = 0; i < 4; ++i) {
			this.halo[i].rotateAngleY = this.bipedHead.rotateAngleY;
			this.halo[i].rotateAngleX = this.bipedHead.rotateAngleX;
		}

		if(this.isRiding) {
			this.bipedRightArm.rotateAngleX += -0.6283185F;
			this.bipedLeftArm.rotateAngleX += -0.6283185F;
			this.bipedRightLeg.rotateAngleX = -1.256637F;
			this.bipedLeftLeg.rotateAngleX = -1.256637F;
			this.bipedRightLeg.rotateAngleY = 0.3141593F;
			this.bipedLeftLeg.rotateAngleY = -0.3141593F;
		}

		if(this.field_1279_h) {
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - 0.3141593F;
		}

		if(this.field_1278_i) {
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.3141593F;
		}

		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedLeftArm.rotateAngleY = 0.0F;
		if(this.onGround > -9990.0F) {
			float f10 = this.onGround;
			this.bipedBody2.rotateAngleY = this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f10) * 3.141593F * 2.0F) * 0.2F;
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
			f10 = 1.0F - this.onGround;
			f10 *= f10;
			f10 *= f10;
			f10 = 1.0F - f10;
			float f7 = MathHelper.sin(f10 * 3.141593F);
			float f8 = MathHelper.sin(this.onGround * 3.141593F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
			this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)f7 * 1.2D + (double)f8));
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
			this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.onGround * 3.141593F) * -0.4F;
		}

		this.bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;

		for(i = 0; i < 5; ++i) {
			this.sword[i].rotateAngleZ = this.bipedRightArm.rotateAngleZ;
			this.sword[i].rotateAngleY = this.bipedRightArm.rotateAngleY;
			this.sword[i].rotateAngleX = this.bipedRightArm.rotateAngleX;
		}

		this.bipedRightArm2.rotateAngleZ = this.bipedRightArm.rotateAngleZ;
		this.bipedRightArm2.rotateAngleY = this.bipedRightArm.rotateAngleY;
		this.bipedRightArm2.rotateAngleX = this.bipedRightArm.rotateAngleX;
		this.bipedLeftArm2.rotateAngleZ = this.bipedLeftArm.rotateAngleZ;
		this.bipedLeftArm2.rotateAngleX = this.bipedLeftArm.rotateAngleX;
		this.wingLeft.rotateAngleY = -0.2F;
		this.wingRight.rotateAngleY = 0.2F;
		this.wingLeft.rotateAngleZ = -0.125F;
		this.wingRight.rotateAngleZ = 0.125F;
		this.wingLeft.rotateAngleY = (float)((double)this.wingLeft.rotateAngleY + Math.sin((double)this.sinage) / 6.0D);
		this.wingRight.rotateAngleY = (float)((double)this.wingRight.rotateAngleY - Math.sin((double)this.sinage) / 6.0D);
		this.wingLeft.rotateAngleZ = (float)((double)this.wingLeft.rotateAngleZ + Math.cos((double)this.sinage) / (double)(this.gonRound ? 8.0F : 3.0F));
		this.wingRight.rotateAngleZ = (float)((double)this.wingRight.rotateAngleZ - Math.cos((double)this.sinage) / (double)(this.gonRound ? 8.0F : 3.0F));
		this.skirt[0].rotateAngleX = -0.2F;
		this.skirt[1].rotateAngleX = -0.2F;
		this.skirt[2].rotateAngleX = 0.2F;
		this.skirt[3].rotateAngleX = 0.2F;
		this.skirt[4].rotateAngleZ = 0.2F;
		this.skirt[5].rotateAngleZ = -0.2F;
		if(this.bipedLeftLeg.rotateAngleX < -0.3F) {
			this.skirt[1].rotateAngleX += this.bipedLeftLeg.rotateAngleX + 0.3F;
			this.skirt[2].rotateAngleX -= this.bipedLeftLeg.rotateAngleX + 0.3F;
		}

		if(this.bipedLeftLeg.rotateAngleX > 0.3F) {
			this.skirt[3].rotateAngleX += this.bipedLeftLeg.rotateAngleX - 0.3F;
			this.skirt[0].rotateAngleX -= this.bipedLeftLeg.rotateAngleX - 0.3F;
		}

	}
}
