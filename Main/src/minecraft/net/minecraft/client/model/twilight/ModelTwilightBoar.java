package net.minecraft.client.model.twilight;

import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;

public class ModelTwilightBoar extends ModelQuadruped {
	public ModelRenderer snout;
	public ModelRenderer tusk1;
	public ModelRenderer tusk2;

	public ModelTwilightBoar() {
		super(6, 0.0F);
		this.head = new ModelRenderer(0, 0);
		this.head.addBox(-4.0F, -2.0F, -6.0F, 8, 7, 6, 0.0F);
		this.head.setRotationPoint(0.0F, 12.0F, -6.0F);
		this.body = new ModelRenderer(28, 10);
		this.body.addBox(-5.0F, -8.0F, -7.0F, 10, 14, 8, 0.0F);
		this.body.setRotationPoint(0.0F, 11.0F, 2.0F);
		this.body.rotateAngleX = 1.570796F;
		this.leg1 = new ModelRenderer(0, 16);
		this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg1.setRotationPoint(-3.0F, 18.0F, 7.0F);
		this.leg2 = new ModelRenderer(0, 16);
		this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg2.setRotationPoint(3.0F, 18.0F, 7.0F);
		this.leg3 = new ModelRenderer(0, 16);
		this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg3.setRotationPoint(-3.0F, 18.0F, -5.0F);
		this.leg4 = new ModelRenderer(0, 16);
		this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.0F);
		this.leg4.setRotationPoint(3.0F, 18.0F, -5.0F);
		this.snout = new ModelRenderer(28, 0);
		this.snout.addBox(-3.0F, 1.0F, -9.0F, 6, 4, 3, 0.0F);
		this.snout.setRotationPoint(0.0F, 12.0F, -6.0F);
		this.tusk1 = new ModelRenderer(17, 17);
		this.tusk1.addBox(4.0F, 1.0F, -9.0F, 1, 2, 1, 0.0F);
		this.tusk1.setRotationPoint(0.0F, 12.0F, -6.0F);
		this.tusk1.rotateAngleZ = 0.3490658F;
		this.tusk2 = new ModelRenderer(17, 17);
		this.tusk2.addBox(-5.0F, 1.0F, -9.0F, 1, 2, 1, 0.0F);
		this.tusk2.setRotationPoint(0.0F, 12.0F, -6.0F);
		this.tusk2.rotateAngleZ = -0.3490658F;
	}

	public void render(float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(f, f1, f2, f3, f4, f5);
		this.snout.render(f5);
		this.tusk1.render(f5);
		this.tusk2.render(f5);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5);
		this.snout.rotateAngleY = this.head.rotateAngleY;
		this.snout.rotateAngleX = this.head.rotateAngleX;
		this.tusk1.rotateAngleY = this.head.rotateAngleY;
		this.tusk1.rotateAngleX = this.head.rotateAngleX;
		this.tusk2.rotateAngleY = this.head.rotateAngleY;
		this.tusk2.rotateAngleX = this.head.rotateAngleX;
	}
}
