package net.minecraft.client.model.twilight;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.EntityLiving;

public class ModelTFKobold extends ModelBiped {
	ModelRenderer rightear;
	ModelRenderer leftear;
	ModelRenderer snout;
	ModelRenderer jaw;
	boolean isJumping = false;

	public ModelTFKobold() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.bipedHead = new ModelRenderer(0, 0);
		this.bipedHead.addBox(-3.5F, -7.0F, -3.0F, 7, 6, 6);
		this.bipedHead.setRotationPoint(0.0F, 13.0F, 0.0F);
		this.bipedBody = new ModelRenderer(12, 19);
		this.bipedBody.addBox(0.0F, 0.0F, 0.0F, 7, 7, 4);
		this.bipedBody.setRotationPoint(-3.5F, 12.0F, -2.0F);
		this.bipedRightArm = new ModelRenderer(36, 17);
		this.bipedRightArm.addBox(-3.0F, -1.0F, -1.5F, 3, 7, 3);
		this.bipedRightArm.setRotationPoint(-3.5F, 12.0F, 0.0F);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm = new ModelRenderer(36, 17);
		this.bipedLeftArm.addBox(0.0F, -1.0F, -1.5F, 3, 7, 3);
		this.bipedLeftArm.setRotationPoint(3.5F, 12.0F, 0.0F);
		this.bipedLeftArm.mirror = false;
		this.bipedRightLeg = new ModelRenderer(0, 20);
		this.bipedRightLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 3);
		this.bipedRightLeg.setRotationPoint(-2.0F, 19.0F, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(0, 20);
		this.bipedLeftLeg.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 3);
		this.bipedLeftLeg.setRotationPoint(2.0F, 19.0F, 0.0F);
		this.rightear = new ModelRenderer(48, 20);
		this.rightear.addBox(0.0F, -4.0F, 0.0F, 4, 4, 1);
		this.rightear.setRotationPoint(3.5F, -3.0F, -1.0F);
		this.rightear.rotateAngleY = 0.2617994F;
		this.rightear.rotateAngleZ = -0.3490659F;
		this.bipedHead.addChild(this.rightear);
		this.leftear = new ModelRenderer(48, 25);
		this.leftear.addBox(-4.0F, -4.0F, 0.0F, 4, 4, 1);
		this.leftear.setRotationPoint(-3.5F, -3.0F, -1.0F);
		this.leftear.rotateAngleY = -0.2617994F;
		this.leftear.rotateAngleZ = 0.3490659F;
		this.bipedHead.addChild(this.leftear);
		this.snout = new ModelRenderer(28, 0);
		this.snout.addBox(-1.5F, -2.0F, -2.0F, 3, 2, 3);
		this.snout.setRotationPoint(0.0F, -2.0F, -3.0F);
		this.bipedHead.addChild(this.snout);
		this.jaw = new ModelRenderer(28, 5);
		this.jaw.addBox(-1.5F, 0.0F, -2.0F, 3, 1, 3);
		this.jaw.setRotationPoint(0.0F, -2.0F, -3.0F);
		this.jaw.rotateAngleX = 0.20944F;
		this.bipedHead.addChild(this.jaw);
	}

	/**
	 * Sets the models various rotation angles.
	 */
	@Override
	public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6) {
		this.bipedHead.rotateAngleY = var4 / (180F / (float) Math.PI);
		this.bipedHead.rotateAngleX = var5 / (180F / (float) Math.PI);
		this.bipedRightArm.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float) Math.PI) * 2.0F * var2 * 0.5F;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 2.0F * var2 * 0.5F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightArm.rotateAngleX = -0.47123894F;
		this.bipedLeftArm.rotateAngleX = -0.47123894F;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(var1 * 0.6662F + (float) Math.PI) * 1.4F * var2;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;
		this.bipedRightArm.rotateAngleZ += MathHelper.cos(var3 * 0.19F) * 0.15F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(var3 * 0.19F) * 0.15F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(var3 * 0.267F) * 0.25F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(var3 * 0.267F) * 0.25F;

		if (this.isJumping) {
			this.jaw.rotateAngleX = 1.44F;
		} else {
			this.jaw.rotateAngleX = 0.20944F;
		}
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third
	 * float params here are the same second and third as in the setRotationAngles
	 * method.
	 */
	@Override
	public void setLivingAnimations(EntityLiving var1, float var2, float var3, float var4) {
		this.isJumping = var1.motionY > 0.0D;
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(float var2, float var3, float var4, float var5, float var6, float var7) {
		this.setRotationAngles(var2, var3, var4, var5, var6, var7);
		this.bipedHead.render(var7);
		this.bipedBody.render(var7);
		this.bipedRightArm.render(var7);
		this.bipedLeftArm.render(var7);
		this.bipedRightLeg.render(var7);
		this.bipedLeftLeg.render(var7);
	}
}
