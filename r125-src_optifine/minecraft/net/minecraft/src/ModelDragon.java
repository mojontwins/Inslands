package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class ModelDragon extends ModelBase {
	private ModelRenderer head;
	private ModelRenderer neck;
	private ModelRenderer jaw;
	private ModelRenderer body;
	private ModelRenderer rearLeg;
	private ModelRenderer frontLeg;
	private ModelRenderer rearLegTip;
	private ModelRenderer frontLegTip;
	private ModelRenderer rearFoot;
	private ModelRenderer frontFoot;
	private ModelRenderer wing;
	private ModelRenderer wingTip;
	private float field_40317_s;

	public ModelDragon(float f1) {
		this.textureWidth = 256;
		this.textureHeight = 256;
		this.setTextureOffset("body.body", 0, 0);
		this.setTextureOffset("wing.skin", -56, 88);
		this.setTextureOffset("wingtip.skin", -56, 144);
		this.setTextureOffset("rearleg.main", 0, 0);
		this.setTextureOffset("rearfoot.main", 112, 0);
		this.setTextureOffset("rearlegtip.main", 196, 0);
		this.setTextureOffset("head.upperhead", 112, 30);
		this.setTextureOffset("wing.bone", 112, 88);
		this.setTextureOffset("head.upperlip", 176, 44);
		this.setTextureOffset("jaw.jaw", 176, 65);
		this.setTextureOffset("frontleg.main", 112, 104);
		this.setTextureOffset("wingtip.bone", 112, 136);
		this.setTextureOffset("frontfoot.main", 144, 104);
		this.setTextureOffset("neck.box", 192, 104);
		this.setTextureOffset("frontlegtip.main", 226, 138);
		this.setTextureOffset("body.scale", 220, 53);
		this.setTextureOffset("head.scale", 0, 0);
		this.setTextureOffset("neck.scale", 48, 0);
		this.setTextureOffset("head.nostril", 112, 0);
		float f2 = -16.0F;
		this.head = new ModelRenderer(this, "head");
		this.head.addBox("upperlip", -6.0F, -1.0F, -8.0F + f2, 12, 5, 16);
		this.head.addBox("upperhead", -8.0F, -8.0F, 6.0F + f2, 16, 16, 16);
		this.head.mirror = true;
		this.head.addBox("scale", -5.0F, -12.0F, 12.0F + f2, 2, 4, 6);
		this.head.addBox("nostril", -5.0F, -3.0F, -6.0F + f2, 2, 2, 4);
		this.head.mirror = false;
		this.head.addBox("scale", 3.0F, -12.0F, 12.0F + f2, 2, 4, 6);
		this.head.addBox("nostril", 3.0F, -3.0F, -6.0F + f2, 2, 2, 4);
		this.jaw = new ModelRenderer(this, "jaw");
		this.jaw.setRotationPoint(0.0F, 4.0F, 8.0F + f2);
		this.jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
		this.head.addChild(this.jaw);
		this.neck = new ModelRenderer(this, "neck");
		this.neck.addBox("box", -5.0F, -5.0F, -5.0F, 10, 10, 10);
		this.neck.addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6);
		this.body = new ModelRenderer(this, "body");
		this.body.setRotationPoint(0.0F, 4.0F, 8.0F);
		this.body.addBox("body", -12.0F, 0.0F, -16.0F, 24, 24, 64);
		this.body.addBox("scale", -1.0F, -6.0F, -10.0F, 2, 6, 12);
		this.body.addBox("scale", -1.0F, -6.0F, 10.0F, 2, 6, 12);
		this.body.addBox("scale", -1.0F, -6.0F, 30.0F, 2, 6, 12);
		this.wing = new ModelRenderer(this, "wing");
		this.wing.setRotationPoint(-12.0F, 5.0F, 2.0F);
		this.wing.addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8);
		this.wing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
		this.wingTip = new ModelRenderer(this, "wingtip");
		this.wingTip.setRotationPoint(-56.0F, 0.0F, 0.0F);
		this.wingTip.addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4);
		this.wingTip.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
		this.wing.addChild(this.wingTip);
		this.frontLeg = new ModelRenderer(this, "frontleg");
		this.frontLeg.setRotationPoint(-12.0F, 20.0F, 2.0F);
		this.frontLeg.addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8);
		this.frontLegTip = new ModelRenderer(this, "frontlegtip");
		this.frontLegTip.setRotationPoint(0.0F, 20.0F, -1.0F);
		this.frontLegTip.addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6);
		this.frontLeg.addChild(this.frontLegTip);
		this.frontFoot = new ModelRenderer(this, "frontfoot");
		this.frontFoot.setRotationPoint(0.0F, 23.0F, 0.0F);
		this.frontFoot.addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16);
		this.frontLegTip.addChild(this.frontFoot);
		this.rearLeg = new ModelRenderer(this, "rearleg");
		this.rearLeg.setRotationPoint(-16.0F, 16.0F, 42.0F);
		this.rearLeg.addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16);
		this.rearLegTip = new ModelRenderer(this, "rearlegtip");
		this.rearLegTip.setRotationPoint(0.0F, 32.0F, -4.0F);
		this.rearLegTip.addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12);
		this.rearLeg.addChild(this.rearLegTip);
		this.rearFoot = new ModelRenderer(this, "rearfoot");
		this.rearFoot.setRotationPoint(0.0F, 31.0F, 4.0F);
		this.rearFoot.addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24);
		this.rearLegTip.addChild(this.rearFoot);
	}

	public void setLivingAnimations(EntityLiving entityLiving1, float f2, float f3, float f4) {
		this.field_40317_s = f4;
	}

	public void render(Entity entity1, float f2, float f3, float f4, float f5, float f6, float f7) {
		GL11.glPushMatrix();
		EntityDragon entityDragon8 = (EntityDragon)entity1;
		float f9 = entityDragon8.field_40173_aw + (entityDragon8.field_40172_ax - entityDragon8.field_40173_aw) * this.field_40317_s;
		this.jaw.rotateAngleX = (float)(Math.sin((double)(f9 * (float)Math.PI * 2.0F)) + 1.0D) * 0.2F;
		float f10 = (float)(Math.sin((double)(f9 * (float)Math.PI * 2.0F - 1.0F)) + 1.0D);
		f10 = (f10 * f10 * 1.0F + f10 * 2.0F) * 0.05F;
		GL11.glTranslatef(0.0F, f10 - 2.0F, -3.0F);
		GL11.glRotatef(f10 * 2.0F, 1.0F, 0.0F, 0.0F);
		float f11 = -30.0F;
		float f13 = 0.0F;
		float f14 = 1.5F;
		double[] d15 = entityDragon8.func_40160_a(6, this.field_40317_s);
		float f16 = this.updateRotations(entityDragon8.func_40160_a(5, this.field_40317_s)[0] - entityDragon8.func_40160_a(10, this.field_40317_s)[0]);
		float f17 = this.updateRotations(entityDragon8.func_40160_a(5, this.field_40317_s)[0] + (double)(f16 / 2.0F));
		f11 += 2.0F;
		float f18 = f9 * (float)Math.PI * 2.0F;
		f11 = 20.0F;
		float f12 = -12.0F;

		float f21;
		for(int i19 = 0; i19 < 5; ++i19) {
			double[] d20 = entityDragon8.func_40160_a(5 - i19, this.field_40317_s);
			f21 = (float)Math.cos((double)((float)i19 * 0.45F + f18)) * 0.15F;
			this.neck.rotateAngleY = this.updateRotations(d20[0] - d15[0]) * (float)Math.PI / 180.0F * f14;
			this.neck.rotateAngleX = f21 + (float)(d20[1] - d15[1]) * (float)Math.PI / 180.0F * f14 * 5.0F;
			this.neck.rotateAngleZ = -this.updateRotations(d20[0] - (double)f17) * (float)Math.PI / 180.0F * f14;
			this.neck.rotationPointY = f11;
			this.neck.rotationPointZ = f12;
			this.neck.rotationPointX = f13;
			f11 = (float)((double)f11 + Math.sin((double)this.neck.rotateAngleX) * 10.0D);
			f12 = (float)((double)f12 - Math.cos((double)this.neck.rotateAngleY) * Math.cos((double)this.neck.rotateAngleX) * 10.0D);
			f13 = (float)((double)f13 - Math.sin((double)this.neck.rotateAngleY) * Math.cos((double)this.neck.rotateAngleX) * 10.0D);
			this.neck.render(f7);
		}

		this.head.rotationPointY = f11;
		this.head.rotationPointZ = f12;
		this.head.rotationPointX = f13;
		double[] d22 = entityDragon8.func_40160_a(0, this.field_40317_s);
		this.head.rotateAngleY = this.updateRotations(d22[0] - d15[0]) * (float)Math.PI / 180.0F * 1.0F;
		this.head.rotateAngleZ = -this.updateRotations(d22[0] - (double)f17) * (float)Math.PI / 180.0F * 1.0F;
		this.head.render(f7);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-f16 * f14 * 1.0F, 0.0F, 0.0F, 1.0F);
		GL11.glTranslatef(0.0F, -1.0F, 0.0F);
		this.body.rotateAngleZ = 0.0F;
		this.body.render(f7);

		for(int i23 = 0; i23 < 2; ++i23) {
			GL11.glEnable(GL11.GL_CULL_FACE);
			f21 = f9 * (float)Math.PI * 2.0F;
			this.wing.rotateAngleX = 0.125F - (float)Math.cos((double)f21) * 0.2F;
			this.wing.rotateAngleY = 0.25F;
			this.wing.rotateAngleZ = (float)(Math.sin((double)f21) + 0.125D) * 0.8F;
			this.wingTip.rotateAngleZ = -((float)(Math.sin((double)(f21 + 2.0F)) + 0.5D)) * 0.75F;
			this.rearLeg.rotateAngleX = 1.0F + f10 * 0.1F;
			this.rearLegTip.rotateAngleX = 0.5F + f10 * 0.1F;
			this.rearFoot.rotateAngleX = 0.75F + f10 * 0.1F;
			this.frontLeg.rotateAngleX = 1.3F + f10 * 0.1F;
			this.frontLegTip.rotateAngleX = -0.5F - f10 * 0.1F;
			this.frontFoot.rotateAngleX = 0.75F + f10 * 0.1F;
			this.wing.render(f7);
			this.frontLeg.render(f7);
			this.rearLeg.render(f7);
			GL11.glScalef(-1.0F, 1.0F, 1.0F);
			if(i23 == 0) {
				GL11.glCullFace(GL11.GL_FRONT);
			}
		}

		GL11.glPopMatrix();
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glDisable(GL11.GL_CULL_FACE);
		float f24 = -((float)Math.sin((double)(f9 * (float)Math.PI * 2.0F))) * 0.0F;
		f18 = f9 * (float)Math.PI * 2.0F;
		f11 = 10.0F;
		f12 = 60.0F;
		f13 = 0.0F;
		d15 = entityDragon8.func_40160_a(11, this.field_40317_s);

		for(int i25 = 0; i25 < 12; ++i25) {
			d22 = entityDragon8.func_40160_a(12 + i25, this.field_40317_s);
			f24 = (float)((double)f24 + Math.sin((double)((float)i25 * 0.45F + f18)) * (double)0.05F);
			this.neck.rotateAngleY = (this.updateRotations(d22[0] - d15[0]) * f14 + 180.0F) * (float)Math.PI / 180.0F;
			this.neck.rotateAngleX = f24 + (float)(d22[1] - d15[1]) * (float)Math.PI / 180.0F * f14 * 5.0F;
			this.neck.rotateAngleZ = this.updateRotations(d22[0] - (double)f17) * (float)Math.PI / 180.0F * f14;
			this.neck.rotationPointY = f11;
			this.neck.rotationPointZ = f12;
			this.neck.rotationPointX = f13;
			f11 = (float)((double)f11 + Math.sin((double)this.neck.rotateAngleX) * 10.0D);
			f12 = (float)((double)f12 - Math.cos((double)this.neck.rotateAngleY) * Math.cos((double)this.neck.rotateAngleX) * 10.0D);
			f13 = (float)((double)f13 - Math.sin((double)this.neck.rotateAngleY) * Math.cos((double)this.neck.rotateAngleX) * 10.0D);
			this.neck.render(f7);
		}

		GL11.glPopMatrix();
	}

	public void setRotationAngles(float f1, float f2, float f3, float f4, float f5, float f6) {
		super.setRotationAngles(f1, f2, f3, f4, f5, f6);
	}

	private float updateRotations(double d1) {
		while(d1 >= 180.0D) {
			d1 -= 360.0D;
		}

		while(d1 < -180.0D) {
			d1 += 360.0D;
		}

		return (float)d1;
	}
}
