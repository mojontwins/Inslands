package net.minecraft.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.world.entity.EntityLiving;

public class RenderZombie extends RenderBiped {
	private String renderPassTexture;
	
	public RenderZombie(ModelBiped modelBase1, float f2, String type) {
		super(modelBase1, f2);
		this.setRenderPassModel(modelBase1);
		this.renderPassTexture = "/mob/" + type + "_eyes.png";
	}

	protected boolean setZombieEyeBrightness(EntityLiving entityZombie, int i2, float f3) {
		if (i2 == 4 && entityZombie.worldObj.worldInfo.isBloodMoon()) {
			this.setRenderPassModel(this.mainModel);
			this.loadTexture(this.renderPassTexture);
			float f4 = 1.0F;
			int i5 = 61680;
			int i6 = i5 % 65536;
			int i7 = i5 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i6 / 1.0F, (float)i7 / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f4);
			return true;
		} else return false;
	}
	
	protected boolean shouldRenderPass(EntityLiving entityLiving1, int i2, float f3) {
		// This renders biped's armor
		boolean superOk = super.shouldRenderPass(entityLiving1, i2, f3);
		
		// This adds pass 4 for eye brightness
		boolean thisOk = this.setZombieEyeBrightness(entityLiving1, i2, f3);

		return thisOk || superOk;
	}
}
