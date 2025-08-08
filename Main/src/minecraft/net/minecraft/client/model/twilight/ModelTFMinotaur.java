package net.minecraft.client.model.twilight;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelTFMinotaur extends ModelBiped {
	ModelRenderer horn1a = new ModelRenderer(24, 0);
	ModelRenderer horn1b;
	ModelRenderer horn2a;
	ModelRenderer horn2b;
	ModelRenderer snout;

	public ModelTFMinotaur() {
		this.horn1a.addBox(0.0F, -1.0F, -1.0F, 3, 2, 2);
		this.horn1a.setRotationPoint(4.0F, -6.0F, 0.0F);
		this.horn1a.rotateAngleY = 0.2617994F;
		this.bipedHead.addChild(this.horn1a);
		this.horn1b = new ModelRenderer(24, 2);
		this.horn1b.addBox(-1.0F, -4.0F, -1.0F, 2, 4, 2);
		this.horn1b.setRotationPoint(2.75F, 0.0F, 0.0F);
		this.horn1b.rotateAngleX = 0.5235988F;
		this.horn1b.rotateAngleY = -0.5235988F;
		this.horn1a.addChild(this.horn1b);
		this.horn2a = new ModelRenderer(24, 0);
		this.horn2a.addBox(-3.0F, -1.0F, -1.0F, 3, 2, 2);
		this.horn2a.setRotationPoint(-4.0F, -6.0F, 0.0F);
		this.horn2a.rotateAngleY = -0.2617994F;
		this.bipedHead.addChild(this.horn2a);
		this.horn2b = new ModelRenderer(24, 2);
		this.horn2b.addBox(-1.0F, -4.0F, -1.0F, 2, 4, 2);
		this.horn2b.setRotationPoint(-2.75F, 0.0F, 0.0F);
		this.horn2b.rotateAngleX = 0.5235988F;
		this.horn2b.rotateAngleY = 0.5235988F;
		this.horn2a.addChild(this.horn2b);
		this.snout = new ModelRenderer(9, 12);
		this.snout.addBox(-2.0F, -1.0F, -1.0F, 4, 3, 1);
		this.snout.setRotationPoint(0.0F, -2.0F, -4.0F);
		this.bipedHead.addChild(this.snout);
	}
}
