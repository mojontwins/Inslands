package net.minecraft.client.model.twilight;

import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;

public class ModelTwilightBighorn extends ModelQuadruped {
	public ModelRenderer horn1a;
	public ModelRenderer horn1b;
	public ModelRenderer horn1c;
	public ModelRenderer horn1d;
	public ModelRenderer horn1e;
	public ModelRenderer horn2a;
	public ModelRenderer horn2b;
	public ModelRenderer horn2c;
	public ModelRenderer horn2d;
	public ModelRenderer horn2e;

	public ModelTwilightBighorn() {
		super(12, 0.0F);
		this.head = new ModelRenderer(0, 0);
		this.head.addBox(-3.0F, -4.0F, -6.0F, 6, 6, 7, 0.0F);
		this.head.setRotationPoint(0.0F, 6.0F, -8.0F);
		this.body = new ModelRenderer(36, 10);
		this.body.addBox(-4.0F, -9.0F, -7.0F, 8, 15, 6, 0.0F);
		this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
		this.leg1 = new ModelRenderer(0, 16);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.leg1.setRotationPoint(-3.0F, 12.0F, 7.0F);
		this.leg2 = new ModelRenderer(0, 16);
		this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.leg2.setRotationPoint(3.0F, 12.0F, 7.0F);
		this.leg3 = new ModelRenderer(0, 16);
		this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.leg3.setRotationPoint(-3.0F, 12.0F, -5.0F);
		this.leg4 = new ModelRenderer(0, 16);
		this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.leg4.setRotationPoint(3.0F, 12.0F, -5.0F);
		this.horn1a = new ModelRenderer(28, 16);
		this.horn1a.addBox(-5.0F, -4.0F, -4.0F, 2, 2, 2, 0.0F);
		this.horn1a.setRotationPoint(0.0F, 6.0F, -8.0F);
		this.horn1b = new ModelRenderer(16, 13);
		this.horn1b.addBox(-6.0F, -5.0F, -3.0F, 2, 2, 4, 0.0F);
		this.horn1b.setRotationPoint(0.0F, 6.0F, -8.0F);
		this.horn1c = new ModelRenderer(16, 19);
		this.horn1c.addBox(-7.0F, -4.0F, 0.0F, 2, 5, 2, 0.0F);
		this.horn1c.setRotationPoint(0.0F, 6.0F, -8.0F);
		this.horn1d = new ModelRenderer(18, 27);
		this.horn1d.addBox(-8.0F, 0.0F, -2.0F, 2, 2, 3, 0.0F);
		this.horn1d.setRotationPoint(0.0F, 6.0F, -8.0F);
		this.horn1e = new ModelRenderer(28, 27);
		this.horn1e.addBox(-9.0F, -1.0F, -3.0F, 2, 2, 1, 0.0F);
		this.horn1e.setRotationPoint(0.0F, 6.0F, -8.0F);
		this.horn2a = new ModelRenderer(28, 16);
		this.horn2a.addBox(3.0F, -4.0F, -4.0F, 2, 2, 2, 0.0F);
		this.horn2a.setRotationPoint(0.0F, 6.0F, -8.0F);
		this.horn2b = new ModelRenderer(16, 13);
		this.horn2b.addBox(4.0F, -5.0F, -3.0F, 2, 2, 4, 0.0F);
		this.horn2b.setRotationPoint(0.0F, 6.0F, -8.0F);
		this.horn2c = new ModelRenderer(16, 19);
		this.horn2c.addBox(5.0F, -4.0F, 0.0F, 2, 5, 2, 0.0F);
		this.horn2c.setRotationPoint(0.0F, 6.0F, -8.0F);
		this.horn2d = new ModelRenderer(18, 27);
		this.horn2d.addBox(6.0F, 0.0F, -2.0F, 2, 2, 3, 0.0F);
		this.horn2d.setRotationPoint(0.0F, 6.0F, -8.0F);
		this.horn2e = new ModelRenderer(28, 27);
		this.horn2e.addBox(7.0F, -1.0F, -3.0F, 2, 2, 1, 0.0F);
		this.horn2e.setRotationPoint(0.0F, 6.0F, -8.0F);
	}

	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(f, f1, f2, f3, f4, f5);
		this.horn1a.render(f5);
		this.horn1b.render(f5);
		this.horn1c.render(f5);
		this.horn1d.render(f5);
		this.horn1e.render(f5);
		this.horn2a.render(f5);
		this.horn2b.render(f5);
		this.horn2c.render(f5);
		this.horn2d.render(f5);
		this.horn2e.render(f5);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5);
		this.horn1a.rotateAngleY = this.head.rotateAngleY;
		this.horn1a.rotateAngleX = this.head.rotateAngleX;
		this.horn1b.rotateAngleY = this.head.rotateAngleY;
		this.horn1b.rotateAngleX = this.head.rotateAngleX;
		this.horn1c.rotateAngleY = this.head.rotateAngleY;
		this.horn1c.rotateAngleX = this.head.rotateAngleX;
		this.horn1d.rotateAngleY = this.head.rotateAngleY;
		this.horn1d.rotateAngleX = this.head.rotateAngleX;
		this.horn1e.rotateAngleY = this.head.rotateAngleY;
		this.horn1e.rotateAngleX = this.head.rotateAngleX;
		this.horn2a.rotateAngleY = this.head.rotateAngleY;
		this.horn2a.rotateAngleX = this.head.rotateAngleX;
		this.horn2b.rotateAngleY = this.head.rotateAngleY;
		this.horn2b.rotateAngleX = this.head.rotateAngleX;
		this.horn2c.rotateAngleY = this.head.rotateAngleY;
		this.horn2c.rotateAngleX = this.head.rotateAngleX;
		this.horn2d.rotateAngleY = this.head.rotateAngleY;
		this.horn2d.rotateAngleX = this.head.rotateAngleX;
		this.horn2e.rotateAngleY = this.head.rotateAngleY;
		this.horn2e.rotateAngleX = this.head.rotateAngleX;
	}
}
