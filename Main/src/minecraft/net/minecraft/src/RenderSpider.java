package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderSpider extends RenderLiving {
	public RenderSpider() {
		super(new ModelSpider(), 1.0F);
		this.setRenderPassModel(new ModelSpider());
	}

	protected float setSpiderDeathMaxRotation(EntitySpiderBase entitySpider1) {
		return 180.0F;
	}

	protected boolean setSpiderEyeBrightness(EntitySpiderBase entitySpider1, int i2, float f3) {
		if(i2 != 0) {
			return false;
		} else {
			this.loadTexture("/mob/spider_eyes.png"); 
			float f4 = 1.0F;
			GL11.glEnable(GL11.GL_BLEND);
			//GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			int i5 = 61680;
			int i6 = i5 % 65536;
			int i7 = i5 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i6 / 1.0F, (float)i7 / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f4);
			return true;
		}
	}

	protected void scaleSpider(EntitySpiderBase entitySpider1, float f2) {
		float f3 = entitySpider1.spiderScaleAmount();
		GL11.glScalef(f3, f3, f3);
	}
	
	protected void preRenderCallback(EntityLiving entityLiving1, float f2) {
		this.scaleSpider((EntitySpiderBase)entityLiving1, f2);
	}
	
	protected float getDeathMaxRotation(EntityLiving entityLiving1) {
		return this.setSpiderDeathMaxRotation((EntitySpiderBase)entityLiving1);
	}

	protected boolean shouldRenderPass(EntityLiving entityLiving1, int i2, float f3) {
		return this.setSpiderEyeBrightness((EntitySpiderBase)entityLiving1, i2, f3);
	}
}
